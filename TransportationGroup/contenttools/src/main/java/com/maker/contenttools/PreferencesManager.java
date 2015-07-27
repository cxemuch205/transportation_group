package com.maker.contenttools;

import android.content.Context;
import android.content.SharedPreferences;

import com.maker.contenttools.Constants.App;

/**
 * Created by den4ik on 5/22/15.
 */
public class PreferencesManager {

    private Context context;
    private SharedPreferences preferences;

    public PreferencesManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(App.Prefs.NAME, Context.MODE_PRIVATE);
    }

    public String getGCMRegID() {
        return preferences.getString(App.Prefs.PROPERTY_REG_ID, "");
    }

    public int getGCMRegVersion() {
        return preferences.getInt(App.Prefs.PROPERTY_APP_VERSION, Integer.MIN_VALUE);
    }

    public void setGCMRegId(String regId) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(App.Prefs.PROPERTY_REG_ID, regId);
        editor.apply();
    }

    public void setGCMRegVersion(int appVersion) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(App.Prefs.PROPERTY_APP_VERSION, appVersion);
        editor.apply();
    }
}
