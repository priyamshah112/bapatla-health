package com.droiduino.companionappcourse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Manage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage);

        // final TextView heading = findViewById(R.id.heading);
        // heading.setText("Did you know");

        //APP BAR PROPERTIES
        // getSupportActionBar().hide(); // hides appbar
        getSupportActionBar().setTitle("MANAGE");

//        TextView forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
//
//        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // This is the code to move to another screen
//                Intent intent = new Intent(Login.this, ForgotPassword.class);
//                startActivity(intent);
//            }
//        });

    }

}