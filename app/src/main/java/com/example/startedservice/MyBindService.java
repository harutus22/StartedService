package com.example.startedservice;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class MyBindService extends IntentService {

    private final String TAG = "TAG3";
    public static final int COUNTER_CODE = 111;
    public static final int STOP_SERVICE = 211;
    private MyHandler myHandler;
    private Messenger messenger;

    public MyBindService() {
        super("MyBindService");
        Log.d(TAG, "Create Bind service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        HandlerThread thread = new HandlerThread("MyBind");
        thread.start();
        myHandler = new MyHandler(thread.getLooper());
        messenger = new Messenger(myHandler);
        Log.d(TAG, "Bind service bounded");
        return messenger.getBinder();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "Bind service unbounded");
        return super.onUnbind(intent);
    }

    public class MyHandler extends Handler{

        MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if(msg.what == COUNTER_CODE){
                int i = 0;
                while(i < 10){
                    Log.d(TAG, "Counting bind service " + i);
                    ++i;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else if(msg.what == STOP_SERVICE){
                stopSelf();
            }
        }
    }
}
