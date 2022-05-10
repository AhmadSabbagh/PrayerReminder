package com.am.engsabbagh.estghfarapp.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.am.engsabbagh.estghfarapp.BroadCastRecieveres.AlarmBroadCastReciever;
import com.am.engsabbagh.estghfarapp.HelperClasses.AttolSharedPreference;
import com.am.engsabbagh.estghfarapp.HelperClasses.RandomText;

public class TimerService extends Service {
    public TimerService() {
    }

    int repeat_count;
    public  static AlarmBroadCastReciever alarmBroadCastReciever = new AlarmBroadCastReciever();
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("job","I am in Timer Serveies");
        RandomText randomText=new RandomText(this);
        AttolSharedPreference attolSharedPreference= new AttolSharedPreference(this);
        String AlarmRepeatCount= attolSharedPreference.getKey2("Alarm_repeat_count");
        repeat_count = Integer.parseInt(AlarmRepeatCount);
        alarmBroadCastReciever=new AlarmBroadCastReciever();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.am.engsabbagh.estghfarapp.START_ALARM");
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(alarmBroadCastReciever, filter);
        alarmBroadCastReciever.setAlarm(this,1000 * 60  * repeat_count);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        //alarmBroadCastReciever.cancelAlarm(this);
        unregisterReceiver(alarmBroadCastReciever);
        super.onDestroy();
    }


}
