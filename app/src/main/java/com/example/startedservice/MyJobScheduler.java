package com.example.startedservice;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;

public class MyJobScheduler extends JobService {

    private final String TAG6 = "TAG";

    public MyJobScheduler() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG6, "Job service created");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG6, "Job service started");
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG6, "Job service stopped");
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG6, "Job service destroyed");
    }
}
