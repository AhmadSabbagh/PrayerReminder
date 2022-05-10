package com.am.engsabbagh.estghfarapp.Activities;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.am.engsabbagh.estghfarapp.Adapters.MyPager;
import com.am.engsabbagh.estghfarapp.HelperClasses.AttolSharedPreference;
import com.am.engsabbagh.estghfarapp.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import me.relex.circleindicator.CircleIndicator;

public class Quraan extends AppCompatActivity {
    private ViewPager quran;
    private MyPager myPager;
    private SearchView searchView;
    AttolSharedPreference attolSharedPreference;
    MenuItem G_start_item;
    String old_page,browse_page;
    private AdView mAdView;
    private AdRequest adRequest;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quraan);
         attolSharedPreference=new AttolSharedPreference(Quraan.this);
         old_page=attolSharedPreference.getKey("star");
        browse_page=attolSharedPreference.getKey("browse_page");
        mAdView = findViewById(R.id.adView);
        quran=findViewById(R.id.quran_id_view);
        myPager = new MyPager(this);
        quran.setAdapter(myPager);
        if(browse_page!=null) {
            quran.setCurrentItem(604-Integer.parseInt(browse_page));
        }
        else
        {
            quran.setCurrentItem(604);

        }
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

  quran.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int i, float v, int i1) {

      }

      @Override
      public void onPageSelected(int i) {
          AttolSharedPreference attolSharedPreference = new AttolSharedPreference(Quraan.this);
          attolSharedPreference.setKey("browse_page", String.valueOf(604 - i));
          String old_page = attolSharedPreference.getKey("star");
          if (old_page != null)
          {
              if (604 - Integer.parseInt(old_page) == i) {
                  Drawable drawable = G_start_item.getIcon();
                  if (drawable != null) {
                      drawable.mutate();
                      drawable.setColorFilter(getResources().getColor(R.color.saved_icon), PorterDuff.Mode.SRC_ATOP);
                  }

              } else {
                  Drawable drawable = G_start_item.getIcon();
                  if (drawable != null) {
                      drawable.mutate();
                      drawable.setColorFilter(getResources().getColor(R.color.unsaved_icon), PorterDuff.Mode.SRC_ATOP);
                  }
              }
      }
          else
          {
              Drawable drawable = G_start_item.getIcon();
              if (drawable != null) {
                  drawable.mutate();
                  drawable.setColorFilter(getResources().getColor(R.color.unsaved_icon), PorterDuff.Mode.SRC_ATOP);
              }
          }


      }

      @Override
      public void onPageScrollStateChanged(int i) {

      }
  });

        handler = new Handler();

        handler.postDelayed(turnAdOn,10000);


    }
    final Runnable turnAdOn = new Runnable(){

        @Override
        public void run() {
            Log.e("repeat","i am in");
            if(mAdView.getVisibility()== View.VISIBLE) {
                mAdView.setVisibility(View.INVISIBLE);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        ActionBar.LayoutParams.MATCH_PARENT,
                        ActionBar.LayoutParams.MATCH_PARENT,
                        100f
                );
                mAdView.setLayoutParams(param);
            }
            else
            {
                mAdView.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        ActionBar.LayoutParams.MATCH_PARENT,
                        ActionBar.LayoutParams.MATCH_PARENT,
                        2f
                );
                mAdView.setLayoutParams(param);

            }
            Quraan.this.handler.postDelayed (turnAdOn, 10000);

        }};
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem search_item = menu.findItem(R.id.mi_search);
        MenuItem start_item = menu.findItem(R.id.mi_star);
        G_start_item= start_item;
        int current_page= 604-quran.getCurrentItem();
        String old_saved_str=attolSharedPreference.getKey("star");
        if(old_saved_str!=null) {
            int old_saved = Integer.parseInt(old_saved_str);
            if(old_saved==current_page)
            {
                Drawable drawable = start_item.getIcon();
                if (drawable != null) {
                    drawable.mutate();
                    drawable.setColorFilter(getResources().getColor(R.color.saved_icon), PorterDuff.Mode.SRC_ATOP);
                }
            }
            else
            {
                Drawable drawable = start_item.getIcon();
                if (drawable != null) {
                    drawable.mutate();
                    drawable.setColorFilter(getResources().getColor(R.color.unsaved_icon), PorterDuff.Mode.SRC_ATOP);
                }

            }
        }
        SearchView searchView = (SearchView) search_item.getActionView();
        searchView.setFocusable(false);
        searchView.setQueryHint("اذهب على صفحة");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String s) {

                quran.setCurrentItem(604-Integer.parseInt(s));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        AttolSharedPreference attolSharedPreference=new AttolSharedPreference(Quraan.this);
        String old_value=attolSharedPreference.getKey("star");

        int id = item.getItemId();
        if (id == R.id.mi_star) {
            if(old_value==null ||Integer.parseInt(old_value)!=604-quran.getCurrentItem()) {
                Drawable drawable = item.getIcon();
                if (drawable != null) {
                    drawable.mutate();
                    drawable.setColorFilter(getResources().getColor(R.color.saved_icon), PorterDuff.Mode.SRC_ATOP);
                }
                int saved = 604 - quran.getCurrentItem();
                attolSharedPreference.setKey("star", String.valueOf(saved));
                Snackbar.make(findViewById(android.R.id.content), "تم حفظ العلامة", Snackbar.LENGTH_LONG)
                        .show();
            }
            else
            {
                Drawable drawable = item.getIcon();
                if (drawable != null) {
                    drawable.mutate();
                    drawable.setColorFilter(getResources().getColor(R.color.unsaved_icon), PorterDuff.Mode.SRC_ATOP);
                }
                Snackbar.make(findViewById(android.R.id.content), "تم حذف العلامة", Snackbar.LENGTH_LONG)
                        .show();
                attolSharedPreference.setKey("star","0");

            }
            return true;
        }
        else if (id==R.id.mi_Re_star)
        {
            quran.setCurrentItem(604-Integer.parseInt(old_value));

        }

        return super.onOptionsItemSelected(item);
    }
}
