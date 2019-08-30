package com.example.startedservice;

import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String KEY = "Your Message";
    public static final int PENDING_INTENT_CODE = 1;
    public static final int SERVICE_STARTED = 1001;
    public static final int SERVICE_FINISHED = 2001;
    public static final String PENDING_KEY = "paddingKey";
    public static final String BROADCAST_KEY = "paddingKey";
    public static final String BROADCAST_ACTION = "broadcastAction";
    private static int counter = 0;

    private IncomingHandler incomingHandler = new IncomingHandler();
    private Messenger incomingMessenger = new Messenger(incomingHandler);
    private TextView textView;
    private Messenger messenger;
    private boolean isBound = false;
    private boolean isBoundStarted = false;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(BROADCAST_KEY, 0);
            if(status == SERVICE_STARTED){
                textView.setText("Broadcast service have started");
            }
            if (status == SERVICE_FINISHED){
                textView.setText("Broadcast service have finished");
            }
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messenger = new Messenger(service);
            isBound = true;

            IncomingHandler incomingHandler = new IncomingHandler();
            incomingMessenger = new Messenger(incomingHandler);

            Message message = Message.obtain(null, MyBindService.COUNTER_CODE);
            message.replyTo = incomingMessenger;
            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            messenger = null;
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);

            if(getIntent() != null ){
                if(getIntent().getIntExtra(
                    NotificationService.STOP_NOTIFICATION_SERVICE, 0) ==
                            123){
                stopNotification();
                }
            }

        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);

    }

    public void startStartedService(View view){
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra(KEY, "Hello from the underworld" + counter);
        startService(intent);
    }

    public void stopStartedService(View view){
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
    }

    public void startIntentService(View view){
        Intent intent = new Intent(this, MyIntentService.class);
        intent.putExtra(KEY, "Hello from the underworld");
        startService(intent);
    }

    public void startServicePaddingIntent(View view){
        PendingIntent pendingIntent = createPendingResult(PENDING_INTENT_CODE, new Intent(), 0);
        Intent intent = new Intent(this, MyPendingIntentService.class);
        intent.putExtra(PENDING_KEY, pendingIntent);
        startService(intent);
    }

    public void stopServicePendingIntent(View view){
        Intent intent = new Intent(this, MyPendingIntentService.class);
        stopService(intent);
    }

    public void startServiceFromBroadcast(View view){
        Intent intent = new Intent(this, MyBroadcastReceiverService.class);
        intent.putExtra(MyBroadcastReceiverService.START_BROADCAST, 5000);
        startService(intent);
    }

    public void startNotificationService(View view){
        Intent intent = new Intent(this, NotificationService.class);
        startService(intent);
    }

    public void stopNotificationService(View view){
        stopNotification();
    }

    private void stopNotification(){
        Intent intent = new Intent(this, NotificationService.class);
        stopService(intent);
    }

    public void bindService(View view){
        if(!isBound) {
            Intent intent = new Intent(this, MyBindService.class);
            bindService(intent, serviceConnection, BIND_AUTO_CREATE);
            ((Button)view).setText("UnBindService");
            isBound = true;
        } else{
            unBindServiceConnection();
            ((Button)view).setText("BindService");
            isBound = false;
        }
    }

    public void startBoundService(View view){
        Message message = new Message();
        if(isBound && !isBoundStarted) {
            isBoundStarted = true;
            message.what = MyBindService.COUNTER_CODE;
            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if (isBound && isBoundStarted){
            isBoundStarted = false;
            message.what = MyBindService.STOP_SERVICE;
            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void startJobService(View view){
        scheduleJob();
    }

    private void scheduleJob() {
        JobScheduler jobScheduler = (JobScheduler)getApplicationContext().
                getSystemService(JOB_SCHEDULER_SERVICE);
        ComponentName componentName = new ComponentName(this, MyJobScheduler.class);
        JobInfo jobInfo = new JobInfo.Builder(1, componentName).
                setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY).
                setPeriodic(10000).setPersisted(true).build();
        jobScheduler.schedule(jobInfo);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        if (isBound) {
            unBindServiceConnection();
        }
    }

    private void unBindServiceConnection(){
        unbindService(serviceConnection);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == SERVICE_STARTED || resultCode == SERVICE_FINISHED){
            textView.setText(data.getStringExtra(MyPendingIntentService.TEXT_MESSAGE));
        }

    }

    class IncomingHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 10:
                    textView.setText(String.valueOf(msg.what));
                    break;

                    default:
                        super.handleMessage(msg);
            }
        }
    }
}
