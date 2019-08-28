package com.example.startedservice;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyPendingIntentService extends Service {
    public static final String TEXT_MESSAGE ="message";
    public final String TAG4 = "TAG";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG4, "Pending Service started");
        PendingIntent pendingIntent = intent.getParcelableExtra(MainActivity.PENDING_KEY);
        Thread thread = new Thread(new MyRunnable(pendingIntent, this));
        thread.start();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG4, "Pending Service Destroyed");
    }
}
