package com.droiduino.companionappcourse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.nio.file.FileVisitOption;

import androidx.appcompat.app.AppCompatActivity;

public class SuccessfulThermometerPairing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.successful_thermometer_pairing);

        // final TextView heading = findViewById(R.id.heading);
        // heading.setText("Did you know");

        //APP BAR PROPERTIES
        // getSupportActionBar().hide(); // hides appbar
        getSupportActionBar().setTitle("DEVICE SETUP");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //displays back button on app bar

        Button redirectToHomeButton = findViewById(R.id.redirectToHomeButton);
        redirectToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            //setting temperature and fever
            Session session;//global variable
            session = new Session(getApplicationContext());
            session.settemperature(96); //setting it to default 96F
            Fever f = new Fever();
            String fever = f.findfever(96);
            session.setfever(fever);

            // This is the code to move to another screen
            Intent intent = new Intent(SuccessfulThermometerPairing.this, BottomNav.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            }
        });
    }

}
