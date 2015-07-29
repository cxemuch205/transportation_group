package com.maker.contenttools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.maker.contenttools.Constants.App;
import com.maker.contenttools.Interfaces.GCMHelperCallback;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Daniil on 25-Mar-15.
 */
public class GCMHelper {

    public static final String TAG = "GCMHelper";

    private static GCMHelper instance;
    private Activity activity;
    private PreferencesManager preferencesManager;
    private AtomicInteger msgId = new AtomicInteger();
    private GoogleCloudMessaging gcm;
    private String regid;
    private GCMHelperCallback callback;

    public static GCMHelper getInstance(Activity activity) {
        if (instance == null) {
            instance = new GCMHelper();
        }
        instance.activity = activity;
        instance.preferencesManager = new PreferencesManager(activity);
        return instance;
    }

    public void initialUserDevice(GCMHelperCallback callback) {
        this.callback = callback;
        if (Tools.checkPlayServices(activity)) {
            gcm = GoogleCloudMessaging.getInstance(activity);
            regid = getRegistrationId(activity);

            if (regid.isEmpty()) {
                registerInBackGround();
            } else {
                if (callback != null)
                    callback.onInitSuccess();
            }
        } else {
            if (callback != null)
                callback.onInitError();
            Log.e(TAG, "NO Google Play Services");
        }
    }

    public static String getRegistrationId(Activity activity) {
        String registrationId = getInstance(activity)
                .preferencesManager.getGCMRegID();
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration id not found.");
            return "";
        }

        int registeredVersion = getInstance(activity)
                .preferencesManager.getGCMRegVersion();
        int currentVersion = getAppVersion(activity);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    public static int getAppVersion(Activity activity) {
        try {
            PackageInfo packageInfo = activity.getPackageManager()
                    .getPackageInfo(activity.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void registerInBackGround() {
        new AsyncTask<Void, Void, String>() {
            private ProgressDialog pd;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = ProgressDialog.show(activity, null, activity.getString(R.string.register_user_device));
                //pd.setMessage(activity.getString(R.string.register_user_device));
                //pd.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(activity);
                    }
                    regid = gcm.register(App.APIKeys.SENDER_ID);
                    msg = "Device registered, registration ID= " + regid;

                    sendRegistrationIdToBackend(regid);

                    storeRegistrationId(activity, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String o) {
                super.onPostExecute(o);
                if (o != null && pd != null) {
                    final String msg = o;
                    Log.i(TAG, msg);
                    pd.setMessage(activity.getString(R.string.register_successful));
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (activity != null
                                    && !activity.isFinishing()
                                    && pd != null
                                    && pd.isShowing()) {
                                pd.dismiss();
                                pd = null;
                            }
                            handler.removeCallbacksAndMessages(null);
                            if (callback != null) {
                                if (msg.contains("Error")) {
                                    callback.onInitError();
                                } else {
                                    callback.onInitSuccess();
                                }
                            }
                        }
                    }, 500);
                }
            }
        }.execute();
    }

    private void storeRegistrationId(Activity activity, String regId) {
        int appVersion = getAppVersion(activity);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        preferencesManager.setGCMRegId(regId);
        preferencesManager.setGCMRegVersion(appVersion);
    }

    private void sendRegistrationIdToBackend(String regId) {
        if (regId != null && !regId.isEmpty()) {
            //No need send, in Oggi need add in every request
        }
    }
}
