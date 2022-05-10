package com.am.engsabbagh.estghfarapp.Adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.am.engsabbagh.estghfarapp.HelperClasses.Toasth;
import com.am.engsabbagh.estghfarapp.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by ahmad on 10/4/2018.
 */

public class MyCustomerAdapterSupplication extends BaseAdapter implements ListAdapter {
    private ArrayList<String> supplication_text_array_list = new ArrayList<String>();//offer pic
    Context context;
    View view;
    ClipboardManager clipBoard;
    private AdView mAdView;
    private AdRequest adRequest;
    public MyCustomerAdapterSupplication(ArrayList<String> Supplication_text, Context context) {
        this.supplication_text_array_list = Supplication_text;
        this.context = context;
    }

    @Override
    public int getCount() {
        return supplication_text_array_list.size();
    }

    @Override
    public Object getItem(int pos) {
        return supplication_text_array_list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.supplication_list, null);
            TextView supplication_text_view = (TextView) view.findViewById(R.id.supplication_text);
            //CardView supplication_cardview = (CardView) view.findViewById(R.id.supplication_card_id);
            FloatingActionButton supplication_button_share = (FloatingActionButton) view.findViewById(R.id.supplication_share_button);
            clipBoard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            mAdView = view.findViewById(R.id.adView);
            adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            supplication_text_view.setText(supplication_text_array_list.get(position));
            supplication_text_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("", supplication_text_array_list.get(position));
                    clipboard.setPrimaryClip(clip);
                    Snackbar.make(view, "تم نسخ الدعاء", Snackbar.LENGTH_LONG)
                            .show();
                }
            });
            supplication_button_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String Subject = supplication_text_array_list.get(position);
                    String Link = context.getString(R.string.ShareAppLink);
                    String Title = context.getString(R.string.ShareAppTitle);
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, Subject);
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, Link);
                    context.startActivity(Intent.createChooser(sharingIntent, Title));
                }
            });
        }


        return view;
    }


}

