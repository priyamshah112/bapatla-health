package com.droiduino.companionappcourse;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Record extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        // final TextView heading = findViewById(R.id.heading);
        // heading.setText("Did you know");

        //APP BAR PROPERTIES
        // getSupportActionBar().hide(); // hides appbar
        getSupportActionBar().setTitle("RECORD");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //displays back button on app bar

    }
}