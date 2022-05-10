package com.am.engsabbagh.estghfarapp.Services;

/**
 * Created by Luminance on 5/4/2018.
 */


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.am.engsabbagh.estghfarapp.Activities.HomeActivity;
import com.am.engsabbagh.estghfarapp.HelperClasses.AttolSharedPreference;
import com.am.engsabbagh.estghfarapp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Date;


/**
 * Created by Ahmad on 08/05/19.

 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    Date date ;
    private NotificationChannel mChannel;
    private NotificationManager notifManager;
    String TAG="TAG";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
         date=new Date();
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        String title=remoteMessage.getNotification().getTitle();
        String content=remoteMessage.getNotification().getBody();
        AttolSharedPreference attolSharedPreference=new AttolSharedPreference(this);
        String noti_status=attolSharedPreference.getKey1("Notification_status");
        if(noti_status!=null)
        {
            if(noti_status.equals("1"))
                sendNotification(title, content);
            else if(noti_status.equals("0"))
            {

            }
        }
        else
        {
            attolSharedPreference.setKey1("Notification_status","1");
            sendNotification(title, content);
        }

    }





    private void sendNotification(String title, String content) {

        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);//Notification Page
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;
        if (notifManager == null) {
            notifManager = (NotificationManager) getSystemService
                    (Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            if (mChannel == null) {
                NotificationChannel mChannel = new NotificationChannel
                        ("0", title, importance);
                mChannel.setDescription (content);
                mChannel.enableVibration (true);
                mChannel.setVibrationPattern (new long[]
                        {100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel (mChannel);
            }
            builder = new NotificationCompat.Builder (this, "0");

            intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity (this, 0, intent, 0);
            builder.setContentTitle (title)  // flare_icon_30
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText (content)  // required
                    .setDefaults (Notification.DEFAULT_ALL)
                    .setAutoCancel (true)
                    .setContentIntent (pendingIntent)
                    .setSound (RingtoneManager.getDefaultUri
                            (RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate (new long[]{100, 200, 300, 400,
                            500, 400, 300, 200, 400});
        }

        else
        {
            builder = new NotificationCompat.Builder (this);

            intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity (this, 0, intent, 0);
            builder.setContentTitle (title)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText (content)  // required
                    .setDefaults (Notification.DEFAULT_ALL)
                    .setAutoCancel (true)

                    .setContentIntent (pendingIntent)
                    .setSound (RingtoneManager.getDefaultUri
                            (RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate (new long[]{100, 200, 300, 400, 500,
                            400, 300, 200, 400})
                    .setPriority (Notification.PRIORITY_HIGH);

        }

        Notification notification = builder.build ();
        notifManager.notify((int)date.getTime(), notification);

    }










}