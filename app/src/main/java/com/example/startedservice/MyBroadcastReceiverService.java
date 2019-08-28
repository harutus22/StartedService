package com.example.startedservice;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class MyBroadcastReceiverService extends IntentService {

    public static final String START_BROADCAST = "startBroadcast";
    private final String TAG5 = "TAG";


    public MyBroadcastReceiverService() {
        super("MyBroadcastReceiverService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            int time = intent.getIntExtra(START_BROADCAST, 0);
            Intent intent1 = new Intent(MainActivity.BROADCAST_ACTION);
            intent1.putExtra(MainActivity.BROADCAST_KEY, MainActivity.SERVICE_STARTED);
            Log.d(TAG5, "BroadCast service send Broadcast to start");
            sendBroadcast(intent1);
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            intent1.putExtra(MainActivity.BROADCAST_KEY, MainActivity.SERVICE_FINISHED);
            sendBroadcast(intent1);
            Log.d(TAG5, "BroadCast service send Broadcast to finish");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG5, "BroadCast service started");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG5, "BroadCast service destroyed");
    }
}
