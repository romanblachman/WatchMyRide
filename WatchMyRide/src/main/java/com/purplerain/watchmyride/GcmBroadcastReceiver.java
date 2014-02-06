package com.purplerain.watchmyride;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.purplerain.watchmyride.utils.AppPreferences;

/**
 * Created by roman on 2/6/14.
 */
public class GCMBroadcastReceiver extends WakefulBroadcastReceiver {

    private static final String TAG = "GCM";
    AppPreferences appPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        appPreferences = new AppPreferences(context);
        Log.d(TAG, "inside onReceive");

        String action = intent.getAction();
        if (action.equals("com.google.android.c2dm.intent.REGISTRATION")) {
            handleRegistration(context,intent);
        } else if (action.equals("com.google.android.c2dm.intent.RECEIVE")) {
            handleMessage(context,intent);
        }
    }

    private void handleRegistration(Context context,Intent intent) {
        String registrationId = intent.getStringExtra("registration_id");
        String error = intent.getStringExtra("error");
        String unregistered = intent.getStringExtra("unregistered");

        // registration succeeded
        if (registrationId != null) {
            Log.d(TAG, "DEVICE REGISTRATION TOKEN : "+registrationId);

            // store registration ID on shared preferences
            appPreferences.putString("DEVICE_REG_ID", registrationId);

            // notify 3rd-party server about the registered ID
            generateNotification(context, "Registration Successful", "Device registered successfully!");
        }

        // unregistration succeeded
        if (unregistered != null) {
            // get old registration ID from shared preferences
            // notify 3rd-party server about the unregistered ID
        }

        // last operation (registration or unregistration) returned an error;
        if (error != null) {
            if ("SERVICE_NOT_AVAILABLE".equals(error)) {
                // optionally retry using exponential back-off
                // (see Advanced Topics)
            } else {
                // Unrecoverable error, log it
                Log.i(TAG, "Received error: " + error);
            }
        }

    }
    private void handleMessage(Context context, Intent intent) {

        String data = intent.getExtras().getString("data");
        generateNotification(context, "New Message is received", data);
    }

    private void generateNotification(Context context,String title,String text) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(text);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, MainActivity.class);

        PendingIntent resultPendingIntent = PendingIntent.getBroadcast(context,0,resultIntent, 0);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());

    }

}