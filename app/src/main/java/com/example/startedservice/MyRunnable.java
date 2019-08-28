package com.example.startedservice;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyRunnable implements Runnable {

    private PendingIntent pendingIntent;
    private Context context;

    public MyRunnable(PendingIntent pendingIntent, Context context){
        this.pendingIntent = pendingIntent;
        this.context = context;
    }

    @Override
    public void run() {
        Log.d(MyPendingIntentService.TEXT_MESSAGE, "Pending Service Started");
        Intent intent = new Intent().putExtra(MyPendingIntentService.TEXT_MESSAGE, "Pending service started");
        Intent intent2 = new Intent().putExtra(MyPendingIntentService.TEXT_MESSAGE, "Pending service have finished");
        try {
            pendingIntent.send(context, MainActivity.SERVICE_STARTED, intent);
            Thread.sleep(3000);
            pendingIntent.send(context, MainActivity.SERVICE_FINISHED, intent2);
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
