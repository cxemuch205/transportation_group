package com.maker.transportationgroup.Services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.maker.contenttools.Constants.App;
import com.maker.transportationgroup.R;
import com.maker.transportationgroup.Receivers.GcmBroadcastReceiver;

/**
 * Created by Daniil on 25-Mar-15.
 */
public class GcmIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentServiceTransportationGroup");
    }
    public static final String TAG = "GCM Oggi.nl";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification(extras);
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification(extras);
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
                /*for (int i = 0; i < 5; i++) {
                    Log.i(TAG, "Working... " + (i + 1)
                            + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }*/
                // Post notification of received message.
                sendNotification(extras);
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(Bundle data) {
        if (data != null) {
            //TODO: make data execute and show notification
            /*String msg = "";
            int id = -1;
            int type = -1;
            if(data.containsKey(App.Extras.MESSAGE))
                msg = data.getString(App.Extras.MESSAGE);

            if (data.containsKey(App.Extras.ID)) {
                id = Integer.parseInt(String.valueOf(data.get(App.Extras.ID)));
            }

            if(data.containsKey(App.Extras.TYPE)) {
                type = Integer.parseInt(String.valueOf(data.get(App.Extras.TYPE)));
            }

            mNotificationManager = (NotificationManager)
                    this.getSystemService(Context.NOTIFICATION_SERVICE);

            Intent intentData;

            if (type == GCMType.OUTING) {
                intentData = new Intent(this, OutingDetailActivity.class);
                Outing outing = new Outing(String.valueOf(id));
                intentData.putExtra(App.Extras.OBJECT, outing);
                intentData.putExtra(App.Extras.TRACKER, true);
            } else {
                intentData = new Intent(this, ActionDetailActivity.class);
                intentData.putExtra(App.Extras.ID, id);
                intentData.putExtra(App.Extras.TRACKER, true);
            }


            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    intentData, 0);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.icon_logo)
                            .setContentTitle(getString(R.string.app_name))
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(msg))
                            .setContentText(msg);

            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());*/
        }
    }
}