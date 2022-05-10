package com.am.engsabbagh.estghfarapp.BroadCastRecieveres;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import com.am.engsabbagh.estghfarapp.Services.FloatingViewService;
import com.am.engsabbagh.estghfarapp.Services.TimerService;

public class AlarmBroadCastReciever extends BroadcastReceiver
{


    @Override
    public void onReceive(Context context, Intent intent)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "koko:koko");
        wl.acquire();

        // Put here YOUR code.
        //Toast.makeText(context, "AlarmBroadCastReciever !!!!!!!!!!", Toast.LENGTH_LONG).show(); // For example
        Log.e("job","i am in AlarbBroadCast Resciver");
        context.startService(new Intent(context, FloatingViewService.class));

        wl.release();
    }

    public void setAlarm(Context context,int time_in_Mille)
    {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent("com.am.engsabbagh.estghfarapp.START_ALARM");
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), time_in_Mille, pi); // Millisec * Second * Minute
        context.stopService(new Intent(context,TimerService.class));
    }

    public void cancelAlarm(Context context)
    {
        Intent intent = new Intent("com.am.engsabbagh.estghfarapp.START_ALARM");
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}