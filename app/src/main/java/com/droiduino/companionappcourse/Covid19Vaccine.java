package com.droiduino.companionappcourse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Covid19Vaccine extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.covid_19_vaccine);

        // final TextView heading = findViewById(R.id.heading);
        // heading.setText("Did you know");

        //APP BAR PROPERTIES
        // getSupportActionBar().hide(); // hides appbar
        getSupportActionBar().setTitle("Covid 19 Vaccine");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //displays back button on app bar

        Button healthGuidanceButton = findViewById(R.id.healthGuidanceButton);
        healthGuidanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This is the code to move to another screen
                Intent intent = new Intent(Covid19Vaccine.this, HealthGuidance.class);
                startActivity(intent);
                finish();
            }
        });
    }
}