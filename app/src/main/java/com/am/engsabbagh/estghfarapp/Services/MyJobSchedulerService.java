package com.am.engsabbagh.estghfarapp.Services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyJobSchedulerService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e("job","Job Created at Time  is :"+ new Date().toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, FloatingViewService.class));
        }
        jobFinished(params,false);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
