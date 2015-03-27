package com.maker.transportationgroup.Receivers;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.maker.transportationgroup.Services.GcmIntentService;

/**
 * Created by Daniil on 25-Mar-15.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    /**
     * TODO: Если вылетит и не получит registrationId
     * <action android:name="com.google.android.c2dm.intent.REGISTRATION" />
     * Вы получите возможность получить рабочий registration ID:
     * final String registrationId = intent.getStringExtra("registration_id");
     */

    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName comp = new ComponentName(context.getPackageName(),
                GcmIntentService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
