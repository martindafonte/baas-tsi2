package com.trueque;

import android.app.NotificationManager;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import baas.sdk.ISDKPush.SDKIntentService;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class PushReciber extends SDKIntentService {

	public static final int NOTIFICATION_ID = 1;
	public static final String TAG = "com.trueque.Notificacion";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    
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
	                sendNotification("Send error: " + extras.toString());
	            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
	                sendNotification("Deleted messages on server: " + extras.toString());
	            // If it's a regular GCM message, do some work.
	            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
	                // This loop represents the service doing some work.
	                
	                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
	                // Post notification of received message.
	                sendNotification("Received: " + extras.getString("Mensaje"));
	                Log.i(TAG, "Received: " + extras.toString());
	            }
	        }
	        // Release the wake lock provided by the WakefulBroadcastReceiver.
	        baas.sdk.utils.GCMBroadcastReceiver.completeWakefulIntent(intent);
		
	}
	private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setTicker("Negocio P2P")
        .setSmallIcon(com.trueque.R.drawable.ic_launcher)
        .setContentTitle("Negocio P2P")
        .setContentText(msg)
        .setAutoCancel(true)
        .setContentIntent(TaskStackBuilder.create(this)
	                        .addParentStack(MainActivity.class)
	                        .addNextIntent(new Intent(this, MainActivity.class)
	                                .putExtra("verOferta", "From Notification"))
	                        .getPendingIntent(0, 0));
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
