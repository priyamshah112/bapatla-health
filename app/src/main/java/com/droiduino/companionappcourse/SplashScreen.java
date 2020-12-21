package com.droiduino.companionappcourse;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        // final TextView heading = findViewById(R.id.heading);
        // heading.setText("Did you know");

        //APP BAR PROPERTIES
        getSupportActionBar().hide(); // hides appbar
    }

}
