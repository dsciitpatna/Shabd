package com.dsciitp.shabd.Notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.dsciitp.shabd.MainActivity;
import com.dsciitp.shabd.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FcmService extends FirebaseMessagingService {

    private static final String LOG_TAG = FcmService.class.getName();
    String imageUri;
    String messageBody;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(LOG_TAG, "FROM: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0){
            /*message data will be used to create intents to particular activity on notification click*/
            Log.d(LOG_TAG, "Message Data: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null){
            Log.d(LOG_TAG, "Message Body: " + remoteMessage.getNotification().getBody());
        }

        imageUri = remoteMessage.getData().get("image_url");
        messageBody = remoteMessage.getNotification().getBody();

        sendNotification(messageBody);

    }

    private void sendNotification(String body){

        NotificationUtils.createNotificationChannel(this);

        int notificationId = NotificationUtils.getID();

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(Notification.VISIBILITY_PUBLIC);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, notificationBuilder.build());

    }
}
