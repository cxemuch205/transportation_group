package com.maker.contenttools;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.buddy.sdk.Buddy;
import com.buddy.sdk.BuddyCallback;
import com.buddy.sdk.BuddyClient;
import com.buddy.sdk.BuddyResult;
import com.google.gson.JsonObject;
import com.maker.contenttools.Api.ApiParser;
import com.maker.contenttools.Constants.App;
import com.maker.contenttools.Models.ApiResponse;
import com.maker.contenttools.Models.RegDevice;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by den4ik on 5/22/15.
 */
public class BuddyManager {

    public static final String TAG = "BuddyManager";

    private static BuddyClient buddyClient;
    private static Context context;

    public static void initBuddy(Context context) {
        buddyClient = Buddy.init(context, App.Buddy.APP_ID, App.Buddy.APP_KEY);
        BuddyManager.context = context;
    }

    public static void registerDevice(final PreferencesManager preferencesManager) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("appID", App.Buddy.APP_ID);
        parameters.put("appKey", App.Buddy.APP_KEY);
        parameters.put("platform", "Android");
        parameters.put("appVersion", Tools.getVersionName(context));
        parameters.put("osVersion", Build.VERSION.CODENAME + " " + Build.VERSION.SDK_INT);
        parameters.put("uniqueId", Tools.getDeviceId(context));
        parameters.put("model", Build.MANUFACTURER + " " + Build.DEVICE + " " + Build.BRAND);

        Buddy.post("/devices", parameters, new BuddyCallback<JsonObject>(JsonObject.class) {
            @Override
            public void completed(BuddyResult<JsonObject> result) {
                Log.i(TAG, "RESPONSE: \n" + result.getResult());
                ApiResponse response = ApiParser.getGson()
                        .fromJson(
                                result.getResult().toString(),
                                ApiResponse.getTypeToken());
                if (response != null && response.is201()) {
                    RegDevice regDevice = ApiParser.getGson().fromJson(String.valueOf(response.result), RegDevice.getTypeToken());
                    if (regDevice != null) {
                        preferencesManager.setDeviceRegister(true);
                        preferencesManager.setRegDeviceData(String.valueOf(response.data));
                        return;
                    }
                }
                preferencesManager.setDeviceRegister(false);
            }
        });
    }
}
