package com.maker.contenttools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.maker.contenttools.Constants.App;

/**
 * Created by Daniil on 13.02.2015.
 */
public class Tools {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static boolean isCorrectEmail(String email) {
        if ((email != null && email.length() == 0) || email == null) {
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false;
        }
        return true;
    }

    public static boolean isCorrectPassword(String password) {
        if ((password != null && password.length() == 0) || password == null) {
            return false;
        }
        if (password.length() < 5) {
            return false;
        }
        if (password.contains(" ")) {
            return false;
        }
        return true;
    }

    public static void showToastCenter(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static boolean userIsRegistered(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(App.Prefs.NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(App.Prefs.USER_IS_REGISTERED, false);
    }

    public static boolean checkPlayServices(Activity activity) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("" + activity.getClass().getName(), "This device is not supported.");
                activity.finish();
            }
            return false;
        }
        return true;
    }
}
