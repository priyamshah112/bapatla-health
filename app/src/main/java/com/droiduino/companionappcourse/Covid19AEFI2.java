package com.droiduino.companionappcourse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Covid19AEFI2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.covid_19_aefi2);

        // final TextView heading = findViewById(R.id.heading);
        // heading.setText("Did you know");

        //APP BAR PROPERTIES
        // getSupportActionBar().hide(); // hides appbar
        getSupportActionBar().setTitle("COVID-19 AEFI");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false); //displays back button on app bar

        Button covid19aefi2nextbutton = findViewById(R.id.covid19aefi2nextbutton);
        covid19aefi2nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This is the code to move to another screen
                Intent intent = new Intent(Covid19AEFI2.this, Covid19AEFI3.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
