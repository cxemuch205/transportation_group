package com.maker.transportationgroup;

import android.app.Application;

import com.maker.contenttools.BuddyManager;
import com.maker.contenttools.PreferencesManager;

/**
 * Created by den4ik on 5/22/15.
 */
public class TGApplication extends Application {

    private static TGApplication instance;

    public static TGApplication getInstance() {
        if (instance == null) {
            instance = new TGApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        BuddyManager.initBuddy(instance);
        PreferencesManager preferencesManager = new PreferencesManager(instance);
        if (preferencesManager.isDeviceRegister()) {
            BuddyManager.registerDevice(preferencesManager);
        }
    }
}
