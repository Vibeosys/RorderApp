package com.vibeosys.quickserve.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vibeosys.quickserve.R;
import com.vibeosys.quickserve.adaptors.RestaurantListAdapter;
import com.vibeosys.quickserve.data.RestaurantDbDTO;
import com.vibeosys.quickserve.util.DeviceBuildInfo;
import com.vibeosys.quickserve.util.NetworkUtils;
import com.vibeosys.quickserve.util.PropertyFileReader;

import org.json.JSONException;
import org.json.JSONObject;

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

    private final static String screenName = "Restaurant registration";
    private ArrayList<RestaurantDbDTO> mRestaurantList;
    private Context mContext = this;
    private ProgressDialog mProgress;
    private RestaurantListAdapter mAdapter;
    private TextView mTxtRestaurant;
    private int mSelectedRestoId;
    private String mSelectedRestaurantName;
    private PropertyFileReader mpropertyFileReader;
    private LinearLayout mSelectRestoView;
    private ProgressBar mProgressBar;
    private String message = "";
    private static final int MAC_ID_LOCATION_PERMISSION = 101;
    private static final int IMEI_PERMISSION = 102;

    @Override
    protected String getScreenName() {
        return screenName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_restaurent);
        Button btnOk = (Button) findViewById(R.id.btnOk);
        TextView aboutUs = (TextView) findViewById(R.id.about_us);
        TextView companyInfo = (TextView) findViewById(R.id.abt_Us_info);
        companyInfo.setText(Html.fromHtml(getResources().getString(R.string.company_info)));
        TextView versionNo = (TextView) findViewById(R.id.versionId);
        mpropertyFileReader = new PropertyFileReader();
        float demo = mpropertyFileReader.getVersion();
        versionNo.append("" + demo);
        if (!NetworkUtils.isActiveNetworkAvailable(this)) {
            String stringTitle = getResources().getString(R.string.error_msg_title_for_network);
            String stringMessage = getResources().getString(R.string.error_msg_for_select_restaurant);
            customAlterDialog(stringTitle, stringMessage);
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            // Marshmallow+
            checkMacAdrressPermission();
            checkIMEINumber();
        } else {
            //below Marshmallow

            mSessionManager.setImei(getImei());
            mSessionManager.setMac(getMacAddress());
        }

        //listResto=(ListView)findViewById(R.id.listView);
        getRestaurant(mSessionManager.getRestaurantUrl());
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AboutUsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);

            }
        });
        mProgressBar = (ProgressBar) findViewById(R.id.select_reto_progress);
        mSelectRestoView = (LinearLayout) findViewById(R.id.select_rest_view);
        mTxtRestaurant = (TextView) findViewById(R.id.txtRestaurantId);
        btnOk.setOnClickListener(this);
    }

    private void checkIMEINumber() {
        String[] PermissionArray = {Manifest.permission.READ_PHONE_STATE};
        ActivityCompat.requestPermissions(SelectRestaurantActivity.this, PermissionArray, IMEI_PERMISSION);
    }

    private void checkMacAdrressPermission() {
        String[] PermissionArray = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        ActivityCompat.requestPermissions(SelectRestaurantActivity.this, PermissionArray, MAC_ID_LOCATION_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MAC_ID_LOCATION_PERMISSION) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    proceedForMacIdPermission();
                } else {
                    Log.d("TAG", "TAG");
                }

            }
        } else {
            //   Toast.makeText(getApplicationContext(), "Permission for mac address is denied", Toast.LENGTH_LONG).show();
        }
        if (requestCode == IMEI_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                proceedIMEINumberPermission();
            }
        } else {
            //    Toast.makeText(getApplicationContext(), "Permission for IMEI number is denied", Toast.LENGTH_LONG).show();
        }
    }

    private void proceedIMEINumberPermission() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String IMEINUmber = telephonyManager.getDeviceId();
        mSessionManager.setImei(IMEINUmber);
        Log.d("TAG", "TAG");
        Log.d("TAG", "TAG");
    }

    private void proceedForMacIdPermission() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        String macAddress = wInfo.getMacAddress();
        mSessionManager.setMac(macAddress);
        Log.d("TAG", "TAG");
        Log.d("TAG", "TAG");


    }

    private boolean downloadDatabase() {
        boolean flag = false;
        HttpURLConnection urlConnection = null;
        OutputStream myOutput = null;
        byte[] buffer = null;
        InputStream inputStream = null;

        String buildInfo64Based = getBuild64BasedInfo();
        UUID uuid = UUID.randomUUID();
        mSessionManager.setDeviceId(uuid.toString());
        String downloadDBURL = mSessionManager.getDownloadDbUrl(mSelectedRestoId) + "&info=" + buildInfo64Based + "&imei=" + mSessionManager.getImei() + "&macId=" + mSessionManager.getMac();/*mSessionManager.getDownloadDbUrl(mSessionManager.getUserId()) + "&info=" + buildInfo64Based;*/
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
                String contentType = urlConnection.getContentType();
                inputStream = urlConnection.getInputStream();
                Log.i(TAG, "##" + contentType);
                if (contentType.equals("application/octet-stream")) {
                    ContextWrapper ctw = new ContextWrapper(getApplicationContext());
                    File directory = ctw.getDir(mSessionManager.getDatabaseDirPath(), Context.MODE_PRIVATE);
                    String dataBaseDirectory = mSessionManager.getDatabaseDirPath();
                    String dataBaseName = mSessionManager.getDatabaseFileName();
                    Log.d("TAG", "TAG");
                    File dbFile = new File(directory, mSessionManager.getDatabaseFileName());
                    buffer = new byte[1024];
                    myOutput = new FileOutputStream(dbFile);
                    int length;
                    try {
                        while ((length = inputStream.read(buffer)) > 0) {
                            myOutput.write(buffer, 0, length);
                        }
                    } catch (Exception e) {
                        Log.d("TAG", "TAG");
                    }

                    myOutput.flush();
                    myOutput.close();
                    inputStream.close();
                    flag = true;
                } else if (contentType.equals("application/json; charset=UTF-8")) {
                    flag = false;
                    String responce = convertStreamToString(inputStream);
                    Log.i(TAG, "##" + responce);

                    try {
                        JSONObject jsResponce = new JSONObject(responce);
                        message = jsResponce.getString("message");
                    } catch (JSONException e) {
                        addError(screenName, "Json error in downloadDatabase", e.getMessage());
                        Log.e(TAG, e.toString());
                    }
                }
            }

        } catch (Exception ex) {
            Log.e("DbDownloadException", "##ROrder while downloading database" + ex.toString());
            addError(screenName, "downloadDatabase", ex.getMessage());
        }

       /* boolean userCreated = mDbRepository.createUserId(mSessionManager.getUserId());
        if (!userCreated)
            Log.e("UserCreation", "##New user could not be created in DB");*/
        return flag;
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mSelectRestoView.setVisibility(show ? View.GONE : View.VISIBLE);
            mSelectRestoView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSelectRestoView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mSelectRestoView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
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
            if (NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {
                getDataBase();

            } else
                startActivityForResult(new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS), 0);
        }
    }

    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath("/data/data/com.vibeosys.quickserve/app_databases/db");
        return dbFile.exists();
    }

    private void getDataBase() {
        mTxtRestaurant.setError(null);
        String strRestId = mTxtRestaurant.getText().toString();

        if (strRestId != null && !strRestId.isEmpty() && strRestId.length() == 6) {
            try {
                mSelectedRestoId = Integer.parseInt(strRestId);
                mSessionManager.setUserRestaurantId(mSelectedRestoId);
                callDatabaseDownload();
            } catch (NumberFormatException e) {
                addError(screenName, "getDatabase", e.getMessage());
                mTxtRestaurant.setError(getResources().getString(R.string.error_select_restaurant));
                Log.e(TAG, "Error at select Restaurant Id" + e.toString());
            }
        } else if (strRestId.length() < 6 || strRestId.length() > 6) {
            mTxtRestaurant.setError(getResources().getString(R.string.error_select_restaurant_id_length));
        } else {
            mTxtRestaurant.setError(getResources().getString(R.string.error_select_restaurant_id));
        }

        //  mSessionManager.setUserRestaurantName(mSelectedRestaurantName);

    }

    private void callDatabaseDownload() {
        if (NetworkUtils.isActiveNetworkAvailable(this)) {
        /*    ContextWrapper ctw = new ContextWrapper(getApplicationContext());
            File directory = ctw.getDir(mSessionManager.getDatabaseDirPath(), Context.MODE_PRIVATE);
            File dbFile = new File(directory, mSessionManager.getDatabaseFileName());
            if (!dbFile.exists()) {
                downloadDatabase(dbFile);
            } else if (dbFile.exists() && (mSessionManager.getUserId() == 0)) {
                downloadDatabase(dbFile);
            }*/
            DownloadAndProceed down = new DownloadAndProceed();
            down.execute();

        } else {
            showMyDialog(mContext);
        }
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

    @Override
    protected void onResume() {
        if (!NetworkUtils.isActiveNetworkAvailable(this)) {
            String stringTitle = getResources().getString(R.string.error_msg_title_for_network);
            String stringMessage = getResources().getString(R.string.error_msg_for_select_restaurant);
            customAlterDialog(stringTitle, stringMessage);
        }
        super.onResume();

    }

    private class DownloadAndProceed extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean downFlag = downloadDatabase();
            return downFlag;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            showProgress(false);
            boolean test = doesDatabaseExist(getApplicationContext(), mSessionManager.getDatabaseDirPath());

            Log.d("TAG", "TAG");
            boolean test1 = doesDatabaseExist(getApplicationContext(), mSessionManager.getDatabaseFileName());
            Log.d("TAG", "TAG");
            Log.d("TAG", "TAG");

            if (aBoolean) {
                mSelectedRestaurantName = mDbRepository.getRestaurantName(mSelectedRestoId);
                mSessionManager.setUserRestaurantName(mSelectedRestaurantName);
                Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentLogin);
                finish();
            } else if (!aBoolean) {
                customAlterDialog(getResources().getString(R.string.error_dialog_title_registration)
                        , message);
            }
        }
    }
}
