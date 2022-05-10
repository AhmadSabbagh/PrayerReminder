package com.am.engsabbagh.estghfarapp.Activities;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.am.engsabbagh.estghfarapp.BroadCastRecieveres.AlarmBroadCastReciever;
import com.am.engsabbagh.estghfarapp.HelperClasses.AttolSharedPreference;
import com.am.engsabbagh.estghfarapp.R;
import com.am.engsabbagh.estghfarapp.Services.MyJobSchedulerService;
import com.am.engsabbagh.estghfarapp.Services.TimerService;

public class Settings extends AppCompatActivity {
    TextView text_count_alarm;
    SeekBar alarm_counter;
    Switch alarm_activate, notification_activate;
    CardView myCounterCardView;
    int repeat_count;
    ImageView MyAnimationImage;
    Animation animation;
    String OldAlarmStatus, OldNotificationStatus, Old_Repeat_Count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        alarm_counter = (SeekBar) findViewById(R.id.counter_id);
        text_count_alarm = (TextView) findViewById(R.id.text_alaram_count_id);
        alarm_activate = (Switch) findViewById(R.id.Active_alarm);
        notification_activate = (Switch) findViewById(R.id.active_notification);
        myCounterCardView = (CardView) findViewById(R.id.counter_cardview);
        MyAnimationImage = (ImageView) findViewById(R.id.aniImg);
        animation = AnimationUtils.loadAnimation(Settings.this, R.animator.alpha_rotate_animation);
        MyAnimationImage.startAnimation(animation);
        final AttolSharedPreference GetSavedStatusIbnAttoSharedPreference = new AttolSharedPreference(Settings.this);
        OldAlarmStatus = GetSavedStatusIbnAttoSharedPreference.getKey("Alarm_status");
        OldNotificationStatus = GetSavedStatusIbnAttoSharedPreference.getKey("Notification_status");
        Old_Repeat_Count = GetSavedStatusIbnAttoSharedPreference.getKey("Alarm_repeat_count");
        if (Old_Repeat_Count != null) {
            alarm_counter.setProgress(Integer.parseInt(Old_Repeat_Count));
            repeat_count = Integer.parseInt(Old_Repeat_Count);

        } else {
            alarm_counter.setProgress(15);

        }

        if (OldAlarmStatus != null) {
            if (OldAlarmStatus.equals("1"))//the alarmBroadCastReciever is ON
            {
                alarm_activate.setChecked(true);
            } else if (OldAlarmStatus.equals("0"))//the alarmBroadCastReciever is ON
            {
                alarm_activate.setChecked(false);
            }
        } else {
            alarm_activate.setChecked(true);
        }

        if (OldNotificationStatus != null) {
            if (OldNotificationStatus.equals("1"))//the NotificationStatus is ON
            {
                notification_activate.setChecked(true);
            } else if (OldNotificationStatus.equals("0"))//the NotificationStatus is ON
            {
                notification_activate.setChecked(false);
            }
        } else {
            notification_activate.setChecked(true);

        }


        if (!alarm_activate.isChecked()) {
            alarm_counter.setEnabled(false);
            text_count_alarm.setText(R.string.Please_Activate_the_Alarm);
            myCounterCardView.setCardBackgroundColor(getResources().getColor(R.color.CardViewBackGround_OFF_color));
            repeat_count = Integer.parseInt(Old_Repeat_Count);
        } else {
            repeat_count = alarm_counter.getProgress();
            text_count_alarm.setText("" + repeat_count);
            myCounterCardView.setCardBackgroundColor(getResources().getColor(R.color.CardViewBackGround_ON_color));

        }

        alarm_activate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("Test", "I changed");
                if (isChecked) {
                    alarm_counter.setEnabled(true);
                    myCounterCardView.setCardBackgroundColor(getResources().getColor(R.color.CardViewBackGround_ON_color));
                    repeat_count = alarm_counter.getProgress();
                    text_count_alarm.setText("" + repeat_count);
                    MyAnimationImage.startAnimation(animation);
                    GetSavedStatusIbnAttoSharedPreference.setKey("Alarm_status", "1");
                    GetSavedStatusIbnAttoSharedPreference.setKey("Alarm_repeat_count", String.valueOf(repeat_count));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        HomeActivity.jobScheduler.schedule(HomeActivity.jobInfo);
                    } else {
                        startService(HomeActivity.intent);
                    }
                } else {
                    MyAnimationImage.startAnimation(animation);
                    alarm_counter.setEnabled(false);
                    text_count_alarm.setText(R.string.Please_Activate_the_Alarm);
                    myCounterCardView.setCardBackgroundColor(getResources().getColor(R.color.CardViewBackGround_OFF_color));
                    repeat_count = alarm_counter.getProgress();
                    GetSavedStatusIbnAttoSharedPreference.setKey("Alarm_repeat_count", String.valueOf(repeat_count));
                    GetSavedStatusIbnAttoSharedPreference.setKey("Alarm_status", "0");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        HomeActivity.jobScheduler.cancel(11);
                    } else {
                        TimerService.alarmBroadCastReciever.cancelAlarm(Settings.this);
                    }


                }
            }
        });
        notification_activate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    GetSavedStatusIbnAttoSharedPreference.setKey("Notification_status", "1");
                    MyAnimationImage.startAnimation(animation);


                } else {

                    GetSavedStatusIbnAttoSharedPreference.setKey("Notification_status", "0");
                    MyAnimationImage.startAnimation(animation);


                }
            }
        });

        alarm_counter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                repeat_count = progress+15;
                text_count_alarm.setText("" + repeat_count);
                GetSavedStatusIbnAttoSharedPreference.setKey("Alarm_repeat_count", String.valueOf(repeat_count));
                MyAnimationImage.startAnimation(animation);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    HomeActivity.jobScheduler.cancel(11);
                    HomeActivity.jobInfo = new JobInfo.Builder(11, new ComponentName(Settings.this, MyJobSchedulerService.class))
                            // only add if network access is required
                            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                            .setPeriodic(1000 * 60 * repeat_count)
                            .build();
                    HomeActivity.jobScheduler.schedule(HomeActivity.jobInfo);
                } else {
                    stopService(new Intent(Settings.this,TimerService.class));
                    startService(HomeActivity.intent);

                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }
}
