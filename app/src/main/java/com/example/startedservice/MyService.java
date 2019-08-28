package com.example.startedservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    private final String TAG = "TAG";
    private int number = 0;
    private Handler handler;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service Created" + number);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final String message = intent.getStringExtra(MainActivity.KEY);
        final int id = startId;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Your service is started" + id);
                Log.d(TAG, message + id);
                number = id;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Your service is destroyed" + number);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
