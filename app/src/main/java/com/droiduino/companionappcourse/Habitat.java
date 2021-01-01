package com.droiduino.companionappcourse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Habitat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habitat);

        // final TextView heading = findViewById(R.id.heading);
        // heading.setText("Did you know");

        //APP BAR PROPERTIES
        // getSupportActionBar().hide(); // hides appbar
        getSupportActionBar().setTitle("HABITAT");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //displays back button on app bar

        Button healthGuidanceButton = findViewById(R.id.healthGuidanceButton);
        healthGuidanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This is the code to move to another screen
                Intent intent = new Intent(Habitat.this, HealthGuidance.class);
                startActivity(intent);
                finish();
            }
        });
    }
}