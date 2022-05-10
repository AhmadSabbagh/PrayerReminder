package com.am.engsabbagh.estghfarapp.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.am.engsabbagh.estghfarapp.Adapters.MyCustomerAdapterSupplication;
import com.am.engsabbagh.estghfarapp.HelperClasses.Toasth;
import com.am.engsabbagh.estghfarapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Supplication_Activity extends AppCompatActivity {
    ListView listView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<String> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplication_);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Supplications");
        listView = (ListView) findViewById(R.id.supplication_list);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String value=dataSnapshot.getValue(String.class);
                arrayList.add(value);
                MyCustomerAdapterSupplication supplication_adapter=new MyCustomerAdapterSupplication(arrayList,Supplication_Activity.this);
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
