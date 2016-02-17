package com.vibeosys.rorderapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.RestaurantListAdapter;
import com.vibeosys.rorderapp.data.RestaurantDbDTO;
import com.vibeosys.rorderapp.util.DeviceBuildInfo;
import com.vibeosys.rorderapp.util.NetworkUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by kiran on 21-01-2016.
 */
public class SelectRestaurantActivity extends BaseActivity implements View.OnClickListener {

    private ArrayList<RestaurantDbDTO> mRestaurantList;
    private Context mContext = this;
    private ProgressDialog mProgress;
    private RestaurantListAdapter mAdapter;
    private TextView mTxtRestaurant;
    private int mSelectedRestoId;
    private String mSelectedRestaurantName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_restaurent);
        Button btnOk = (Button) findViewById(R.id.btnOk);
        TextView aboutUs = (TextView) findViewById(R.id.about_us);
        //listResto=(ListView)findViewById(R.id.listView);
        getRestaurant(mSessionManager.getRestaurantUrl());
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(),AboutUsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);

            }
        });

        mTxtRestaurant = (TextView) findViewById(R.id.txtRestaurantId);
        btnOk.setOnClickListener(this);
    }

    private void downloadDatabase(File internalfile) {
        HttpURLConnection urlConnection = null;
        OutputStream myOutput = null;
        byte[] buffer = null;
        InputStream inputStream = null;

        String buildInfo64Based = getBuild64BasedInfo();
        UUID uuid = UUID.randomUUID();
        mSessionManager.setDeviceId(uuid.toString());
        String downloadDBURL = mSessionManager.getDownloadDbUrl(mSelectedRestoId);/*mSessionManager.getDownloadDbUrl(mSessionManager.getUserId()) + "&info=" + buildInfo64Based;*/
        Log.i(TAG, "##" + downloadDBURL);
        try {
            URL url = new URL(downloadDBURL);
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.d("STATUS", "##Request Sent...");
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(20000);
            urlConnection.setReadTimeout(10000);
            urlConnection.connect();

               /* OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(data);
                wr.flush();
               */
            int Http_Result = urlConnection.getResponseCode();
            String res = urlConnection.getResponseMessage();
            Log.d("ResponseMessage", res);
            Log.e("RESPONSE CODE", String.valueOf(Http_Result));
            if (Http_Result == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                buffer = new byte[1024];
                myOutput = new FileOutputStream(internalfile);
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                myOutput.flush();
                myOutput.close();
                inputStream.close();
            }

        } catch (Exception ex) {
            Log.e("DbDownloadException", "##ROrder while downloading database" + ex.toString());
        }

       /* boolean userCreated = mDbRepository.createUserId(mSessionManager.getUserId());
        if (!userCreated)
            Log.e("UserCreation", "##New user could not be created in DB");*/
    }

    private String getBuild64BasedInfo() {
        String buildInfo64Based;
        DeviceBuildInfo buildInfo = DeviceBuildInfo.GetDeviceInfo();
        String serializedBuildInfo = buildInfo.serializeString();
        byte[] serializedBytes = serializedBuildInfo.getBytes();
        buildInfo64Based = Base64.encodeToString(serializedBytes, Base64.NO_WRAP);
        return buildInfo64Based;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnOk) {
            if (NetworkUtils.isActiveNetworkAvailable(getApplicationContext()))
                getDataBase();
            else
                startActivityForResult(new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS), 0);
        }
    }

    private void getDataBase() {
        mTxtRestaurant.setError(null);
        String strRestId = mTxtRestaurant.getText().toString();

        if (strRestId != null && !strRestId.isEmpty()) {
            try {
                mSelectedRestoId = Integer.parseInt(strRestId);
            } catch (NumberFormatException e) {
                mTxtRestaurant.setError(getResources().getString(R.string.error_select_restaurant));
                Log.e(TAG, "Error at select Restaurant Id" + e.toString());
            }
        } else {
            mTxtRestaurant.setError(getResources().getString(R.string.error_select_restaurant_id));
        }
        mSessionManager.setUserRestaurantName(mSelectedRestaurantName);
        if (NetworkUtils.isActiveNetworkAvailable(this)) {
            ContextWrapper ctw = new ContextWrapper(getApplicationContext());
            File directory = ctw.getDir(mSessionManager.getDatabaseDirPath(), Context.MODE_PRIVATE);
            File dbFile = new File(directory, mSessionManager.getDatabaseFileName());
            if (!dbFile.exists()) {
                downloadDatabase(dbFile);
            } else if (dbFile.exists() && (mSessionManager.getUserId() == 0)) {
                downloadDatabase(dbFile);
            }
        }
        Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
        intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentLogin);
        finish();
    }

    private void getRestaurant(String Url) {
        RequestQueue vollyRequest = Volley.newRequestQueue(mContext);
        mProgress = ProgressDialog.show(mContext, "Loading", "Wait");
        StringRequest restoRequest = new StringRequest(Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // displayRestaurant(response);
                mProgress.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgress.dismiss();
            }
        });
        restoRequest.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        vollyRequest.add(restoRequest);
    }

  /*  private void displayRestaurant(String result) {
        RestaurantDbDTO restaurantDbDTO=new RestaurantDbDTO();
        mRestaurantList=restaurantDbDTO.getArrayList(result);
        mAdapter =new RestaurantListAdapter(mRestaurantList,this);
        mTxtRestaurant.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        Log.i(TAG,"##"+mRestaurantList.toString());
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        RestaurantDbDTO restaurant= (RestaurantDbDTO) mAdapter.getItem(position);
        mSelectedRestoId=restaurant.getRestaurantId();
        mSelectedRestaurantName=restaurant.getTitle();
        Log.i(TAG,"##"+mSelectedRestoId);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }*/
}
