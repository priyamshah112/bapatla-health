package com.droiduino.companionappcourse;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        // final TextView heading = findViewById(R.id.heading);
        // heading.setText("Did you know");

        //APP BAR PROPERTIES
        // getSupportActionBar().hide(); // hides appbar
        getSupportActionBar().setTitle("RECOVER PASSWORD");

        final Button forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This is the code to move to another screen
                // Intent intent = new Intent(Registration.this, DeviceSetup1.class);
                // startActivity(intent);
                // finish();

                // Toast.makeText(Registration.this,gender,Toast.LENGTH_SHORT).show();

                final EditText emailField = (EditText) findViewById(R.id.forgotPasswordEmail);
                String email = emailField.getText().toString();

                if(TextUtils.isEmpty(email)) {
                    emailField.setError("Please fill this field.");
                    return;
                }

                forgotPasswordButton.setEnabled(false);
                forgotPasswordButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                String payload = "{\"email\": \""+email+"\"}";
                new ForgotPassword.PostData().execute(payload);
            }
        });
    }

    private class PostData extends AsyncTask< String, Void, Void > {

        String resultvalue = "";

        protected Void doInBackground(String...params) {

            URL url = null;
            try {
                url = new URL("https://bapfoundation.org/app-recoverPassword");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection uc = null;
            try {
                uc = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String line;
            StringBuffer jsonString = new StringBuffer();

            uc.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            try {
                uc.setRequestMethod("POST");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            uc.setDoInput(true);
            uc.setInstanceFollowRedirects(false);
            try {
                uc.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            OutputStreamWriter writer = null;
            try {
                writer = new OutputStreamWriter(uc.getOutputStream(), "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                writer.write(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
                while((line = br.readLine()) != null){
                    jsonString.append(line);
                }
                br.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            uc.disconnect();
            System.out.println(jsonString.toString().substring(11,jsonString.toString().lastIndexOf("\"")));
            resultvalue = jsonString.toString().substring(11,jsonString.toString().lastIndexOf("\""));
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //do stuff
            System.out.println("inpostexecute------"+resultvalue);

            if(resultvalue.equals("success")){
                final TextView heading = findViewById(R.id.errorText);
                heading.setVisibility(View.VISIBLE);
                heading.setTextColor(getResources().getColor(R.color.colorGreen));
                heading.setText("Recovery mail successfully sent to your email address.");
            }
            else if (resultvalue.equals("not_existing")){
                final TextView heading = findViewById(R.id.errorText);
                heading.setVisibility(View.VISIBLE);
                heading.setTextColor(getResources().getColor(R.color.colorRed));
                heading.setText("The entered email is not registered.");
            }
            else{
                final TextView heading = findViewById(R.id.errorText);
                heading.setVisibility(View.VISIBLE);
                heading.setTextColor(getResources().getColor(R.color.colorRed));
                heading.setText("There was some error. Please try again in a few minutes.");
            }

            Button forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
            forgotPasswordButton.setEnabled(true);
            forgotPasswordButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
    }

}
