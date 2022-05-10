package com.am.engsabbagh.estghfarapp.Activities;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.am.engsabbagh.estghfarapp.HelperClasses.MessagesClass;
import com.am.engsabbagh.estghfarapp.HelperClasses.Toasth;
import com.am.engsabbagh.estghfarapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ContactUsAvtivity extends AppCompatActivity {
EditText Email ,Message ;
FloatingActionButton send ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us_avtivity);
        Email= (EditText)findViewById(R.id.EmailEditTextId);
        Message=(EditText)findViewById(R.id.MessageEditTextId);
        send=(FloatingActionButton)findViewById(R.id.floatingSendingMessageActionButton);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Email.getText().toString().equals("") && !Message.getText().toString().equals(""))
                {
                    if(Email.getText().toString().contains("@"))
                    {
                        String Messages1=Message.getText().toString();
                        String EmailT=Email.getText().toString();
                        if(Email.getText().toString().contains(".")) {
                             EmailT = Email.getText().toString().replace(".","");

                        }
                        MessagesClass messagesClass = new MessagesClass();
                        messagesClass.setMail(EmailT);
                        messagesClass.setMsg(Messages1);
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference ref = firebaseDatabase.getReference("Messages");
                        DatabaseReference newRef = ref.child(EmailT).push();
                        newRef.setValue(Messages1);
                        new Toasth(ContactUsAvtivity.this,"شكرا لتواصلك معنا");
                        finish();

                    }
                    else
                    {
                        Snackbar.make(findViewById(android.R.id.content), getString(R.string.Invalid_Email), Snackbar.LENGTH_LONG)

                                .show();
                    }
                }
                else
                {
                    Snackbar.make(findViewById(android.R.id.content), getString(R.string.Fill_the_gabs), Snackbar.LENGTH_LONG)

                            .show();                }
            }
        });
    }
}
