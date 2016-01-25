package com.vibeosys.rorderapp.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.vibeosys.rorderapp.activities.LoginActivity;
import com.vibeosys.rorderapp.data.UploadUser;
import com.vibeosys.rorderapp.data.UserDTO;
import com.vibeosys.rorderapp.database.DbRepository;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by anand on 02-11-2015.
 */
public class UserAuth {

    private OnUpdateUserResultReceived mOnUpdateUserResultReceived;

    public static boolean isUserLoggedIn(Context context, String userName) {
        if ( userName == null || userName == "") {
            Intent theLoginIntent = new Intent(context, LoginActivity.class);
            //theLoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            theLoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            theLoginIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(theLoginIntent);
            return false;
        }
        return true;
    }

    public static boolean isUserLoggedIn(Context context) {
        //String theUserEmailId = SessionManager.Instance().getUserEmailId();
        String theUserName = SessionManager.Instance().getUserName();
        return isUserLoggedIn(context, theUserName);
    }

    public static boolean isUserLoggedIn() {
        String theUserEmailId = SessionManager.Instance().getUserEmailId();
        String theUserName = SessionManager.Instance().getUserName();
        //String theUserPhotoURL = SessionManager.Instance().getUserPhotoUrl();

        if (theUserName == null || theUserName == "") {
            return false;
        }
        return true;
    }

    public void saveAuthenticationInfo(UserDTO userInfo, final Context context) {
        if (userInfo == null)
            return;

        if (userInfo.getmUserName() == null || userInfo.getmUserName() == "")
            return;

        SessionManager theSessionManager = SessionManager.getInstance(context);
        theSessionManager.setUserName(userInfo.getmUserName());
        theSessionManager.setUserActive(userInfo.ismActive());
        theSessionManager.setUserRollId(userInfo.getmRoleId());
        theSessionManager.setUserRestaurantId(userInfo.getmRestaurantId());
        theSessionManager.setUserId(String.valueOf(userInfo.getmUserId()));
        //theSessionManager.setUserRegdApiKey(userInfo.getApiKey());

        //updateUserDetailsOnServer(context, userInfo);
    }

    private boolean postUserUpdate(UserDTO userInfo, Context context) {
        DbRepository newDataBase = new DbRepository(context, SessionManager.getInstance(context));
       // boolean isRecordUpdated = newDataBase.updateUserAuthenticationInfo(userInfo);
        //boolean isRecordAddedToAllUsers = newDataBase.addOrUpdateUserToAllUsers(userInfo);
        return true; //isRecordUpdated && isRecordAddedToAllUsers;
    }

    public static boolean CleanAuthenticationInfo() {

        SessionManager theSessionManager = SessionManager.Instance();
        theSessionManager.setUserName(null);
        theSessionManager.setUserId(null);
        theSessionManager.setUserActive(false);
        theSessionManager.setUserRollId(0);
        theSessionManager.setUserRestaurantId(0);
       // theSessionManager.setUserPhotoUrl(null);
        //theSessionManager.setUserLoginRegdSource(RegistrationSourceTypes.NONE);
        theSessionManager.setUserRegdApiKey(null);

        return true;
    }

    /*private void updateUserDetailsOnServer(final Context context, final UserDTO userInfo) {
        Gson gson = new Gson();
        UploadUser uploadUser = new UploadUserOtp(
                userInfo.getUserId(),
                userInfo.getEmailId(),
                userInfo.getUserName(),
                userInfo.getPassword());
        final String encodedString = gson.toJson(uploadUser);
        //RequestQueue rq = Volley.newRequestQueue(this);
        String updateUsersDetailsUrl = SessionManager.Instance().getUpdateUserDetailsUrl();
        Log.d("Encoded String", encodedString);
        RequestQueue rq = Volley.newRequestQueue(context);
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST,
                updateUsersDetailsUrl, encodedString, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                String resultantCode = null;
                try {
                    resultantCode = response.getString("errorCode");
                } catch (JSONException e) {
                    Log.e("JSON Exception", e.toString());
                }

                Integer errorCode = Integer.parseInt(resultantCode);
                if (errorCode == 0) {
                    postUserUpdate(userInfo, context);
                }

                if (mOnUpdateUserResultReceived != null)
                    mOnUpdateUserResultReceived.onUpdateUserResult(errorCode);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("UPLOADUSERDETAILSERROR", "TravelAppError [" + error.getMessage() + "]");
            }

        });

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        rq.add(jsonArrayRequest);
    }
*/
    public void setOnUpdateUserResultReceived(OnUpdateUserResultReceived onUpdateUserResultReceived) {
        this.mOnUpdateUserResultReceived = onUpdateUserResultReceived;
    }

    public interface OnUpdateUserResultReceived {
        void onUpdateUserResult(int errorCode);
    }
}
