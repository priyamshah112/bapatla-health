package com.droiduino.companionappcourse;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class UnsuccessfulThermometerPairing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unsuccessful_thermometer_pairing);

        // final TextView heading = findViewById(R.id.heading);
        // heading.setText("Did you know");

        //APP BAR PROPERTIES
        // getSupportActionBar().hide(); // hides appbar
        getSupportActionBar().setTitle("DEVICE SETUP");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //displays back button on app bar

    }

}
