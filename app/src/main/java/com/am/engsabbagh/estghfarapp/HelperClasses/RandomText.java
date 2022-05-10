package com.am.engsabbagh.estghfarapp.HelperClasses;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class RandomText {
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    public static  ArrayList<String> arrayList = new ArrayList<String >(
            Arrays.asList("لا اله الا انت سبحانك اني كنت من الظالمين ","استغفر الله العلي العظيم ","لا حول ولا قوة الا بالله العلي العظيم","" +
                    "حسبنا الله سيؤتينا الله من فضله إنا الى الله راغبون"));

    public  static String random_text;
    public RandomText(Context context)
    {
        this.context=context;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("RandonText");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String value=dataSnapshot.getValue(String.class);
                arrayList.add(value);
                Log.e("job","i am the random txt:"+ new Date().toString());


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
    public String Get_Random_text()
    {
        return random_text;
    }

}
