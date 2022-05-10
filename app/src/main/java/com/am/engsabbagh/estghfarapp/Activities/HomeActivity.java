package com.am.engsabbagh.estghfarapp.Activities;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.am.engsabbagh.estghfarapp.HelperClasses.AttolSharedPreference;
import com.am.engsabbagh.estghfarapp.HelperClasses.RandomText;
import com.am.engsabbagh.estghfarapp.R;
import com.am.engsabbagh.estghfarapp.Services.MyJobSchedulerService;
import com.am.engsabbagh.estghfarapp.Services.TimerService;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.am.engsabbagh.estghfarapp.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ImageView MyAnimationImage;
    Animation animation;
    public static int TimeRepeat;
    public static String AlarmStatus;
    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2085;
    private static final int JOB_ID_UPDATE = 0x1000;
    public static Intent intent;
    public static JobScheduler jobScheduler;
    public  static JobInfo jobInfo;
    int Current_onlineInt,AllUsersInt;
        ArrayList<String> list = new ArrayList<String>();


    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference,databaseReference1,databaseReference2,databaseReference3;
    private AdView mAdView;
    TimerTask myTask = new TimerTask() {
        public void run() {
            mAdView.post(new Runnable() {
                @Override
                public void run() {
                  //  mAdView.setVisibility(View.INVISIBLE);
                }
            });
        }
    };
    Timer myTimer = new Timer();
    private final long INTERVAL = 10000; // I choose   10 seconds
    AdRequest adRequest;
    Handler handler;
    String Current_online,All_users;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAdView = findViewById(R.id.adView);
        myTimer.schedule(myTask, INTERVAL);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        MobileAds.initialize(this, "ca-app-pub-3224782505746796~4820657311");
        AttolSharedPreference attolSharedPreference = new AttolSharedPreference(this);
        PackageManager manager = this.getPackageManager ( );
        PackageInfo info = null;
        attolSharedPreference = new AttolSharedPreference (this);

        String versionName = BuildConfig.VERSION_NAME;
         adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        Get_Coins();
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
              //  Toast.makeText(HomeActivity.this,"AdsLoaded",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Videos");
        databaseReference1=firebaseDatabase.getReference("Version");
        databaseReference2=firebaseDatabase.getReference("CurentOn");
        databaseReference3=firebaseDatabase.getReference("AllUser");
        databaseReference3.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                AttolSharedPreference attolSharedPreference1 =new AttolSharedPreference(HomeActivity.this);
                String value=dataSnapshot.getValue(String.class);
                All_users=value;

                String AttoCheck = attolSharedPreference1.getKey("s");
                if(AttoCheck==null) {
                    AllUsersInt=Integer.parseInt(All_users)+1;
                    databaseReference3.child("1").setValue(String.valueOf(AllUsersInt));
                    attolSharedPreference1.setKey("s", "1");

                }



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
              //  list.clear();
                String value=dataSnapshot.getValue(String.class);

               // list.add(value);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        databaseReference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String value=dataSnapshot.getValue(String.class);
                Current_online=value;
                 Current_onlineInt=Integer.parseInt(Current_online)+1;
                databaseReference2.child("1").setValue(String.valueOf(Current_onlineInt));

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
              //  list.clear();
                String value=dataSnapshot.getValue(String.class);

              //  list.add(value);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String value=dataSnapshot.getValue(String.class);

                list.add(value);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                 list.clear();
                String value=dataSnapshot.getValue(String.class);

                list.add(value);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        RandomText randomText = new RandomText(this);
        fab.setOnClickListener(new View.OnClickListener() { //this part will be used soon
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Soon", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !android.provider.Settings.canDrawOverlays(this)) {
            askPermission();
            finish();
            Toast.makeText(HomeActivity.this, "أعد فتح التطبيق بعد اعطاء الاذن ", Toast.LENGTH_LONG).show();
        }
        else
        {
          //  attolSharedPreference.setKey("F","1");
        }

        ifHuaweiAlert();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        MyAnimationImage = (ImageView) findViewById(R.id.aniImg);
        Log.e("Test", "Home Page on create");

        /*
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);
        Glide.with(this).load("http://islamicbook.ws/6/2.png").apply(options).into(MyAnimationImage);
        */
        animation = AnimationUtils.loadAnimation(HomeActivity.this, R.animator.alpha_rotate_animation);
        MyAnimationImage.startAnimation(animation);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Snackbar.make(findViewById(android.R.id.content), "سيقوم التطبيق بارسال تنبيهات بشكل دوري على مدار اليوم , يمكنك تعديل الاوقات من الاعدادات", Snackbar.LENGTH_LONG)
                .show();

        String AlarmRepeatCount = attolSharedPreference.getKey("Alarm_repeat_count");
        if (AlarmRepeatCount != null) {
            TimeRepeat = Integer.parseInt(AlarmRepeatCount);

        } else {
            TimeRepeat = 15;
            attolSharedPreference.setKey("Alarm_repeat_count", "15");

        }
        AlarmStatus = attolSharedPreference.getKey("Alarm_status");
        if (AlarmStatus != null) {


        } else {

            attolSharedPreference.setKey("Alarm_status", "1");
            AlarmStatus = "1";
        }


            if (AlarmStatus.equals("1")) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        intent = new Intent(HomeActivity.this, TimerService.class);
                        startService(intent);
                    } else if (android.provider.Settings.canDrawOverlays(getApplicationContext())) {
                        intent = new Intent(HomeActivity.this, TimerService.class);
                        startService(intent);
                    } else {
                        askPermission();
                        Toast.makeText(getApplicationContext(), "You need System Alert Window Permission to do this", Toast.LENGTH_SHORT).show();
                    }
                } else {
                     if (android.provider.Settings.canDrawOverlays(getApplicationContext())) {
                         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                             Log.e("job","I am calling Job Scedualr :"+ new Date().toString());
                             jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
                             jobInfo = new JobInfo.Builder(11, new ComponentName(HomeActivity.this, MyJobSchedulerService.class))
                                     // only add if network access is required
                                     .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                                     .setPeriodic(1000 * 60 * TimeRepeat)
                                     .build();
                             jobScheduler.schedule(jobInfo);
                            // attolSharedPreference.setKey("F","1");
                         }
                     }
                }
            }


          handler = new Handler();

        handler.postDelayed(turnAdOn,10000);
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Test", "signInAnonymously:success");
                             user = mAuth.getCurrentUser();
                            Log.e("Test", "User Signed in "+user.getUid());

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Test", "signInAnonymously:failure", task.getException());

                        }

                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });



    }
    final Runnable turnAdOn = new Runnable(){

        @Override
        public void run() {
            Log.e("repeat","i am in");
            if(mAdView.getVisibility()==View.VISIBLE) {
                mAdView.setVisibility(View.INVISIBLE);
            }
            else
            {
                mAdView.setVisibility(View.VISIBLE);

            }
            HomeActivity.this.handler.postDelayed (turnAdOn, 10000);

        }};
    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Test", "Home Page on start");
        animation = AnimationUtils.loadAnimation(HomeActivity.this, R.animator.alpha_rotate_animation);
        MyAnimationImage.startAnimation(animation);
        Snackbar.make(findViewById(android.R.id.content), "سيقوم التطبيق بارسال تنبيهات بشكل دوري على مدار اليوم , يمكنك تعديل الاوقات من الاعدادات", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Test", "Home page on resume");
        mAdView.setVisibility(View.VISIBLE);
        databaseReference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String value=dataSnapshot.getValue(String.class);
                final int versionCode = BuildConfig.VERSION_CODE;
                if(Integer.parseInt(value)>versionCode)
                {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder (HomeActivity.this);
                    alertDialogBuilder.setMessage ("يتوفر اصدار احدث في المتجر هل تريد التحديث الآن؟ ")
                            .setCancelable (false)
                            .setPositiveButton ("نعم",
                                    new DialogInterface.OnClickListener ( ) {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent i = new Intent (Intent.ACTION_VIEW);
                                            i.setData (Uri.parse (getString(R.string.App_link)));
                                            startActivity (i);
                                        }
                                    });
                    alertDialogBuilder.setNegativeButton ("لاحقا",
                            new DialogInterface.OnClickListener ( ) {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel ( );

                                }
                            });
                    AlertDialog alert = alertDialogBuilder.create ( );
                    alert.show ( );

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("Test", "Home Page on Stop");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Test", "Home page on Pause");

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Test", "Home page on Destroyed");
        databaseReference2.child("1").setValue(String.valueOf(Current_onlineInt-1));
        mAuth.signOut();
        Log.e("Test", "user Signed out" + user.getUid());

    }
    /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.supplication_id) {
            startActivity(new Intent(HomeActivity.this, Supplication_Activity.class));

        } else if (id == R.id.nav_video_id) {
           Intent intent=new Intent(HomeActivity.this,VideoListAdapterActivity.class);
           intent.putStringArrayListExtra("arr",list);
           startActivity(intent);

        } else if (id == R.id.nav_donate_id) {
            startActivity(new Intent(HomeActivity.this, PaypalActivity.class)); //SOOOON


        } else if (id == R.id.nav_settings_id) {
            startActivity(new Intent(HomeActivity.this, Settings.class));

        } else if (id == R.id.nav_language_settings_id) {
            startActivity(new Intent(HomeActivity.this, SoonActivity.class)); //SOOOON


        }
     else if (id == R.id.Quran) {
        startActivity(new Intent(HomeActivity.this, Quraan.class)); //SOOOON


    }
        else if (id == R.id.morning_supp) {

            Intent intent= new Intent(HomeActivity.this, MorningOrEvning.class);
            intent.putExtra("activity","m");
            startActivity(intent); //SOOOON

        }
        else if (id == R.id.evning_supp) {
            Intent intent= new Intent(HomeActivity.this, MorningOrEvning.class);
            intent.putExtra("activity","e");
            startActivity(intent); //SOOOON

        }
        else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String Subject = getString(R.string.ShareAppSubject);
            String Link = getString(R.string.ShareAppLink);
            String Title = getString(R.string.ShareAppTitle);
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, Subject);
            sharingIntent.putExtra(Intent.EXTRA_TEXT, Link);
            startActivity(Intent.createChooser(sharingIntent, Title));

        } else if (id == R.id.nav_contact) {
            startActivity(new Intent(HomeActivity.this, ContactUsAvtivity.class)); //SOOOON

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //check if hwawei
    private void ifHuaweiAlert() {
        final SharedPreferences settings = getSharedPreferences("ProtectedApps", MODE_PRIVATE);
        final String saveIfSkip = "skipProtectedAppsMessage";
        boolean skipMessage = settings.getBoolean(saveIfSkip, false);
        if (!skipMessage) {
            final SharedPreferences.Editor editor = settings.edit();
            Intent intent = new Intent();
            intent.setClassName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");
            if (isCallable(intent)) {
                final AppCompatCheckBox dontShowAgain = new AppCompatCheckBox(this);
                dontShowAgain.setText("Do not show again");
                dontShowAgain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        editor.putBoolean(saveIfSkip, isChecked);
                        editor.apply();
                    }
                });

                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Huawei Protected Apps")
                        .setMessage(String.format("%s requires to be enabled in 'Protected Apps' to function properly.%n", getString(R.string.app_name)))
                        .setView(dontShowAgain)
                        .setPositiveButton("Protected Apps", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                huaweiProtectedApps();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            } else {
                editor.putBoolean(saveIfSkip, true);
                editor.apply();
            }
        }
    }

    private boolean isCallable(Intent intent) {
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void huaweiProtectedApps() {
        try {
            String cmd = "am start -n com.huawei.systemmanager/.optimize.process.ProtectActivity";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                cmd += " --user " + getUserSerial();
            }
            Runtime.getRuntime().exec(cmd);
        } catch (IOException ignored) {
        }
    }

    private String getUserSerial() {
        //noinspection ResourceType
        Object userManager = getSystemService("user");
        if (null == userManager) return "";

        try {
            Method myUserHandleMethod = android.os.Process.class.getMethod("myUserHandle", (Class<?>[]) null);
            Object myUserHandle = myUserHandleMethod.invoke(android.os.Process.class,
                    (Object[]) null);
            Method getSerialNumberForUser = userManager.getClass().getMethod("getSerialNumberForUser", myUserHandle.getClass());
            Long userSerial = (Long) getSerialNumberForUser.invoke(userManager, myUserHandle);
            if (userSerial != null) {
                return String.valueOf(userSerial);
            } else {
                return "";
            }
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException ignored) {
        }
        return "";
    }

    //end of check

    private void askPermission() {
        Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
    }
    public void Get_Coins() {
        String requestUrl = getString(R.string.Server_ip) + "Get_Coins_To_Buy";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    if (response.contains("coins_price")) {
                        try {
                            JSONArray JA = new JSONArray(response);
                            int [] coins_number= new int [JA.length()];
                            int []  coins_price =new int [JA.length()];
                            int []  coins_id =new int [JA.length()];

                            ArrayList<Integer> coins_numberArray = new ArrayList<Integer>();
                            Toast.makeText(HomeActivity.this, "succefully", Toast.LENGTH_LONG).show();

                            for (int i = 0; i < JA.length(); i++) {

                                JSONObject JO = (JSONObject) JA.get(i);

                                coins_number[i] = (int ) JO.get("coins_number");
                                coins_price[i] = (int) JO.get("coins_price");
                                coins_id[i] = (int) JO.get("coins_id");


                                coins_numberArray.add(coins_number[i]);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    else if (response.contains("data")) {
                        Toast.makeText(HomeActivity.this, "خطا في الاتصال حاول لاحقا", Toast.LENGTH_LONG).show();


                    }
                }
                else {
                    Toast.makeText(HomeActivity.this, "تعذر جلب ال معرف الخاص بك", Toast.LENGTH_LONG).show();


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace(); //log the error resulting from the request for diagnosis/debugging
                Log.e("Volley Result",  "Error"+error.getLocalizedMessage()); //the response contains the result from the server, a json string or any other object returned by your server

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                //..... Add as many key value pairs in the map as necessary for your request
                return params;
            }
        };
        Volley.newRequestQueue(HomeActivity.this).add(stringRequest);
    }




}
