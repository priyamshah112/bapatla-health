package com.droiduino.companionappcourse;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Health extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health);

        // final TextView heading = findViewById(R.id.heading);
        // heading.setText("Did you know");

        //APP BAR PROPERTIES
        // getSupportActionBar().hide(); // hides appbar
        getSupportActionBar().setTitle("HEALTH DATA");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //displays back button on app bar

    }
}
