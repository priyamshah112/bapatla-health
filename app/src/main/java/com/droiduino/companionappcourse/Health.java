package com.droiduino.companionappcourse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

        Button travelButton = findViewById(R.id.travelButton);
        travelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This is the code to move to another screen
                Intent intent = new Intent(Health.this, Travel.class);
                startActivity(intent);
            }
        });
    }
}
