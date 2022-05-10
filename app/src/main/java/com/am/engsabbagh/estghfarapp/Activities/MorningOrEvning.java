package com.am.engsabbagh.estghfarapp.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.am.engsabbagh.estghfarapp.Adapters.MyCustomerAdapterSupplication;
import com.am.engsabbagh.estghfarapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MorningOrEvning extends AppCompatActivity {
    ListView listView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morning_or_evning);
        listView=findViewById(R.id.list);
        firebaseDatabase = FirebaseDatabase.getInstance();
        Intent intent = getIntent();
        String activity_type=intent.getStringExtra("activity");
        if(activity_type.equals("m")) {
            databaseReference = firebaseDatabase.getReference("morning");
        }
        else if (activity_type.equals("e"))
        {
            databaseReference = firebaseDatabase.getReference("evning");

        }
        else
        {

        }

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String value=dataSnapshot.getValue(String.class);
                arrayList.add(value);
                MyCustomerAdapterSupplication supplication_adapter=new MyCustomerAdapterSupplication(arrayList,MorningOrEvning.this);
                listView.setAdapter(supplication_adapter);
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
}
