package com.am.engsabbagh.estghfarapp.BroadCastRecieveres;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.am.engsabbagh.estghfarapp.HelperClasses.AttolSharedPreference;
import com.am.engsabbagh.estghfarapp.HelperClasses.RandomText;
import com.am.engsabbagh.estghfarapp.Services.MyJobSchedulerService;
import com.am.engsabbagh.estghfarapp.Services.TimerService;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class AutoStartBroadCastReciever extends BroadcastReceiver {
    AlarmBroadCastReciever alarmBroadCastReciever = new AlarmBroadCastReciever();
    int TimeRepeat;
    String AlarmStatus;
    Timer myTimer = new Timer();
    //the amount of time after which you want to stop the service
    private final long INTERVAL = 10000; // I choose 5 seconds
    TimerTask myTask;
    @Override
    public void onReceive(final Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
        Log.e("job","i am in the boot:"+ new Date().toString());

        RandomText randomText= new RandomText(context);
//        try {
//            Thread.sleep(10000);
//
//        }
//        catch (InterruptedException e){}
        myTask = new TimerTask() {
            public void run() {
               resume(context);
                Log.e("job","i am calling job scedualar:"+ new Date().toString());

            }
        };
        myTimer.schedule(myTask, INTERVAL);


        }
    }
    public  void resume (Context context)
    {
        AttolSharedPreference attolSharedPreference= new AttolSharedPreference( context);
        AlarmStatus= attolSharedPreference.getKey3("Alarm_status");
        String AlarmRepeatCount = attolSharedPreference.getKey3("Alarm_repeat_count");
        if (AlarmRepeatCount != null) {
            TimeRepeat = Integer.parseInt(AlarmRepeatCount);

        } else {
            TimeRepeat = 1;

        }
        if(AlarmStatus.equals("1"))
        {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    Intent intent1 = new Intent(context, TimerService.class);
                    context.startService(intent1);
                } else if (android.provider.Settings.canDrawOverlays(context)) {
                    Intent intent1 = new Intent(context, TimerService.class);
                    context.startService(intent1);
                } else {
                    // askPermission();
                    Toast.makeText(context, "You need System Alert Window Permission to do this Go to the settings and give permission " +
                            "to the app", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
                JobInfo jobInfo = new JobInfo.Builder(11, new ComponentName(context, MyJobSchedulerService.class))
                        // only add if network access is required
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .setPeriodic(1000*60*TimeRepeat)
                        .build();
                jobScheduler.schedule(jobInfo);
            }
        }
    }
}
