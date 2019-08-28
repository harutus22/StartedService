package com.example.startedservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotificationService extends Service {
    private NotificationManager notificationManager;
    private final String CHANNEL_ID = "com.myChannel";
    private final String TAG5 = "TAG";
    public static final String STOP_NOTIFICATION_SERVICE = "stopNotificationService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        openNotification();
        Log.d(TAG5, "Foreground service started");
        return START_NOT_STICKY;
    }

    private void openNotification() {
        createNotificationChannel();
        Notification notification = createNotification();
        startForeground(1, notification);
        Log.d(TAG5, "Foreground notification created");
    }

    private Notification createNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(STOP_NOTIFICATION_SERVICE, 123);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, intent, 0);
        Notification.Builder notification = new Notification.Builder(this).
                setContentTitle("Your service is running").
                setContentText("Your service have been started and you may open activity").
                setSmallIcon(R.drawable.ic_launcher_foreground).
                setContentIntent(pendingIntent).
                setAutoCancel(true).
                setTicker("Your Ticker sir");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notification.setChannelId(CHANNEL_ID);
        }
        return  notification.build();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "TestIntentServiceChannel";
            String description = "TestIntentServiceChannelDescription";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getManager();
            notificationManager.createNotificationChannel(channel);
        }
    }

    private NotificationManager getManager(){
        if(notificationManager == null){
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG5, "Foreground service destroyed");
    }
}
