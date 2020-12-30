package com.droiduino.companionappcourse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DeviceSetup1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_setup_1);

        // final TextView heading = findViewById(R.id.heading);
        // heading.setText("Did you know");

        //APP BAR PROPERTIES
        // getSupportActionBar().hide(); // hides appbar
        getSupportActionBar().setTitle("DEVICE SETUP");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false); //displays back button on app bar

        Button deviceSetup2Button = findViewById(R.id.deviceSetup2Button);
        deviceSetup2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This is the code to move to another screen
                Intent intent = new Intent(DeviceSetup1.this, DeviceSetup2.class);
                startActivity(intent);
            }
        });
    }
}
