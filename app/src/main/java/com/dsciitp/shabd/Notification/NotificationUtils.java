package com.dsciitp.shabd.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.dsciitp.shabd.R;

import java.util.concurrent.atomic.AtomicInteger;

public class NotificationUtils {

    private final static AtomicInteger c = new AtomicInteger(0);
    static int getID() {
        return c.incrementAndGet();
    }

    public static void createNotificationChannel(Context context){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.notification_channel_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(context.getString(R.string.default_notification_channel_id), name, importance);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
