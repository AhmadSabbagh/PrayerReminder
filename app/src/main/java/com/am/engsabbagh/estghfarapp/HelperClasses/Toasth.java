package com.am.engsabbagh.estghfarapp.HelperClasses;

import android.content.Context;
import android.widget.Toast;

public class Toasth {
    public Toasth(Context context,String text)
    {
       Toast.makeText(context,text,Toast.LENGTH_LONG).show();
    }
}
