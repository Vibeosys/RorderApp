package com.vibeosys.rorderapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.RestaurantDbDTO;
import com.vibeosys.rorderapp.data.RestaurantDbDTOList;
import com.vibeosys.rorderapp.util.DeviceBuildInfo;
import com.vibeosys.rorderapp.util.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 * Created by kiran on 21-01-2016.
 */
public class SelectRestaurantActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext=this;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_restaurent);
        Button btnOk=(Button)findViewById(R.id.btnOk);
        getRestaurant("http://192.168.1.6/rorderwebapp/api/v1/getRestaurant");
        btnOk.setOnClickListener(this);
    }

    private void downloadDatabase(File internalfile) {
        HttpURLConnection urlConnection = null;
        OutputStream myOutput = null;
        byte[] buffer = null;
        InputStream inputStream = null;

        String buildInfo64Based = getBuild64BasedInfo();
        UUID uuid = UUID.randomUUID();
        mSessionManager.setUserId(uuid.toString());
        String downloadDBURL = "http://192.168.1.6/rorderwebapp/api/v1/downloadDb";/*mSessionManager.getDownloadDbUrl(mSessionManager.getUserId()) + "&info=" + buildInfo64Based;*/
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
        int id=v.getId();
        if(id==R.id.btnOk)
        {
            if (NetworkUtils.isActiveNetworkAvailable(this)) {
                ContextWrapper ctw = new ContextWrapper(getApplicationContext());
                File directory = ctw.getDir(mSessionManager.getDatabaseDirPath(), Context.MODE_PRIVATE);
                File dbFile = new File(directory, mSessionManager.getDatabaseFileName());
                if (!dbFile.exists()) {
                    downloadDatabase(dbFile);
                } else if (dbFile.exists() && (mSessionManager.getUserId() == null || mSessionManager.getUserId().isEmpty())) {
                    downloadDatabase(dbFile);
                }
            }
            Intent intentLogin=new Intent(getApplicationContext(),LoginActivity.class);
            intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentLogin);
            finish();
        }
    }
    private void getRestaurant(String Url) {
        RequestQueue vollyRequest = Volley.newRequestQueue(mContext);
        progress=ProgressDialog.show(mContext,"Loading","Wait");
        StringRequest restoRequest = new StringRequest(Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                displayRestaurant(response);
                progress.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
            }
        });
        restoRequest.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        vollyRequest.add(restoRequest);
    }

    private void displayRestaurant(String result) {
       /* Gson gson = new Gson();
        try {
            JSONArray arr=new JSONArray(result);
            //for(int i=0;i<)
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RestaurantDbDTO[] restaurantList= gson.fromJson(result,RestaurantDbDTO[].class);*/
        //Log.i(TAG,"##"+restaurantList.getList().toString());
    }
}
