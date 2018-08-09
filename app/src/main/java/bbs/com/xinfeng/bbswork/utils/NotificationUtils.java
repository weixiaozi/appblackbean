package bbs.com.xinfeng.bbswork.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import bbs.com.xinfeng.bbswork.R;

/**
 * Created by LaoZhao on 2017/11/19.
 */

public class NotificationUtils extends ContextWrapper {

    private NotificationManager manager;
    public static final String id = "blackbean_1";
    public static final String name = "blackbeandown";
    private Notification.Builder builder;
    private NotificationCompat.Builder builder25;

    public NotificationUtils(Context context) {
        super(context);
    }

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_NONE);
        channel.enableVibration(false);
        channel.enableLights(false);
        channel.enableVibration(false);
        getManager().createNotificationChannel(channel);
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    private Notification.Builder getChannelNotification(String title, int progress) {
        if (builder == null) {
            createNotificationChannel();
            builder = new Notification.Builder(getApplicationContext(), id)
                    .setShowWhen(false)
                    .setOngoing(true)
                    .setAutoCancel(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//悬挂式Notification，5.0后显示
                builder.setSmallIcon(R.drawable.icon_notification_logo);
            } else {
                builder.setSmallIcon(R.mipmap.ic_launcher);
            }
        }
        builder.setContentTitle(title);
//        builder.setProgress(100, progress, false);
        return builder;
    }

    private NotificationCompat.Builder getNotification_25(String title, int progress) {
        if (builder25 == null) {
            builder25 = new NotificationCompat.Builder(getApplicationContext())
                    .setShowWhen(false)
                    .setOngoing(true)
                    .setVibrate(new long[]{0l})
                    .setAutoCancel(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//悬挂式Notification，5.0后显示
                builder25.setSmallIcon(R.drawable.icon_notification_logo);
            } else {
                builder25.setSmallIcon(R.mipmap.ic_launcher);
            }
        }
        builder25.setContentTitle(title).setProgress(100, progress, false);
        return builder25;
    }

    public void sendNotification(String title, int progress) {
        if (Build.VERSION.SDK_INT >= 26) {
//            createNotificationChannel();
            if (builder == null) {
                Notification notification = getChannelNotification
                        ("下载中...", progress).build();
                getManager().notify(103, notification);
            }
        } else {
            Notification notification = getNotification_25(title, progress).build();
            getManager().notify(103, notification);
        }
    }

    public void cancel() {
        getManager().cancel(103);
    }
}
