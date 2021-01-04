package com.droiduino.companionappcourse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Covid19AEFI3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.covid_19_aefi3);

        // final TextView heading = findViewById(R.id.heading);
        // heading.setText("Did you know");

        //APP BAR PROPERTIES
        // getSupportActionBar().hide(); // hides appbar
        getSupportActionBar().setTitle("COVID-19 AEFI");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false); //displays back button on app bar

        Button covid19aefi3submitbutton = findViewById(R.id.covid19aefi3submitbutton);
        covid19aefi3submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This is the code to move to another screen
                Intent intent = new Intent(Covid19AEFI3.this, CovidAEFISubmitReferenceNo.class);
                startActivity(intent);
            }
        });
    }
}
