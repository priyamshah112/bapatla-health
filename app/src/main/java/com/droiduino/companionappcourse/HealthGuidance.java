package com.droiduino.companionappcourse;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class HealthGuidance extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_guidance);

        // final TextView heading = findViewById(R.id.heading);
        // heading.setText("Did you know");

        //APP BAR PROPERTIES
        // getSupportActionBar().hide(); // hides appbar
        getSupportActionBar().setTitle("HEALTH GUIDANCE");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //displays back button on app bar


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goToBottomNav = new Intent(getApplicationContext(), BottomNav.class);
        goToBottomNav.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Will clear out your activity history stack till now
        startActivity(goToBottomNav);
    }
}