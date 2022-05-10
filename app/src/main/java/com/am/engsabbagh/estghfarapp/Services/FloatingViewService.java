package com.am.engsabbagh.estghfarapp.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.am.engsabbagh.estghfarapp.BroadCastRecieveres.AlarmBroadCastReciever;
import com.am.engsabbagh.estghfarapp.HelperClasses.AttolSharedPreference;
import com.am.engsabbagh.estghfarapp.HelperClasses.RandomText;
import com.am.engsabbagh.estghfarapp.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Timer;
import java.util.TimerTask;

public class FloatingViewService extends Service implements View.OnClickListener {

    TimerTask myTask = new TimerTask() {
        public void run() {
            stopSelf();
        }
    };
    //Timer that will make the runnable run.
    Timer myTimer = new Timer();
    //the amount of time after which you want to stop the service
    private final long INTERVAL = 8000; // I choose 5 seconds
    private WindowManager mWindowManager;
    private View mFloatingView;
    //CardView MyCardEs;
    String text;
    Button MyTextView;
    private AdView mAdView;
    private static final String ANDROID_CHANNEL_ID = "com.am.engsabbagh.estghfarapp.Floating.Channel";
    public FloatingViewService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myTimer.schedule(myTask, INTERVAL);
        //getting the widget layout from xml using layout inflater
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);
        //MyCardEs = (CardView) mFloatingView.findViewById(R.id.MyCardViewID);
        MyTextView = (Button) mFloatingView.findViewById(R.id.EstghfarTextID);
        super.onCreate();
        MobileAds.initialize(this, "ca-app-pub-3224782505746796~4820657311");
        AttolSharedPreference attolSharedPreference = new AttolSharedPreference(this);
        mAdView = mFloatingView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());

            MyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
            }
        });

        //setting the layout parameters
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);


        //getting windows services and adding the floating view to it
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);


        //adding an touchlistener to make drag movement of the floating widget
        mFloatingView.findViewById(R.id.relativeLayoutParent).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_UP:
                        //when the drag is ended switching the state of the widget
                        // collapsedView.setVisibility(View.GONE);
                        MyTextView.setVisibility(View.VISIBLE);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        //this code is helping the widget to move around the screen with fingers
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });

    }

    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.am.engsabbagh.estghfarapp.floating";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_pray_icon)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.EstghfarTextID:
                stopSelf();
                break;


        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int random_index= (int) (Math.random()*RandomText.arrayList.size());
        for (int item=0 ; item<RandomText.arrayList.size();item++)
        {
            Log.e("job","Item "+item+ " : " +RandomText.arrayList.get(item));
        }
        text= RandomText.arrayList.get(random_index);
        MyTextView.setText(text);
        return super.onStartCommand(intent, flags, startId);
    }
}