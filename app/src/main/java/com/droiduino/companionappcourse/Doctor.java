package com.droiduino.companionappcourse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Doctor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor);

        // final TextView heading = findViewById(R.id.heading);
        // heading.setText("Did you know");

        //APP BAR PROPERTIES
         getSupportActionBar().hide(); // hides appbar
//        getSupportActionBar().setTitle("DIAGNOSIS");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false); //displays back button on app bar

        Session session;//global variable
        session = new Session(getApplicationContext());
        String name = session.getname();
        String id = session.getid();

        TinyDB tinydb = new TinyDB(getApplicationContext());
        final ArrayList<String> allusers = tinydb.getListString("allusers");
        System.out.println("ALLUSERSS"+allusers);

        final TextView homeFragmentUsername = (TextView)findViewById(R.id.homeFragmentUsername);
        homeFragmentUsername.setText(name);
        homeFragmentUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String allusernames[] = new String[allusers.size()/2];
                final String alluserids[] = new String[allusers.size()/2];
                int counter=0;
                int id_counter=0;
                for(int i=0;i<allusers.size();i++){
                    if(i%2!=0){
                        allusernames[counter]=allusers.get(i);
                        counter+=1;
                    }else{
                        alluserids[id_counter]=allusers.get(i);
                        id_counter+=1;
                    }
                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(Doctor.this);
                builder.setTitle("Choose user");
                builder.setItems(allusernames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on allusernames[which]
                        Session session;//global variable
                        session = new Session(getApplicationContext());
                        session.setname(allusernames[which]);
                        session.setid(alluserids[which]);

                        session.settemperature(96); //setting it to default 96F
                        Fever f = new Fever();
                        String fever = f.findfever(96);
                        session.setfever(fever);

                        dialog.dismiss();

                        // reloading activity
                        finish();
                        startActivity(getIntent());
                    }
                });
                builder.show();
            }
        });

        ImageView doctorbackbtn = findViewById(R.id.doctorbackbtn);
        doctorbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This is the code to move to another screen
                finish();
            }
        });

    }
}