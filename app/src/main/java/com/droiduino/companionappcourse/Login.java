package com.droiduino.companionappcourse;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // final TextView heading = findViewById(R.id.heading);
        // heading.setText("Did you know");

        //APP BAR PROPERTIES
        // getSupportActionBar().hide(); // hides appbar
        getSupportActionBar().setTitle("LOGIN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        TextView forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This is the code to move to another screen
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        TextView registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This is the code to move to another screen
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
            }
        });

        final Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This is the code to move to another screen
                //Intent intent = new Intent(Login.this, DeviceSetup1.class);
                //startActivity(intent);
                //finish();

                loginButton.setEnabled(false);
                loginButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                // Toast.makeText(Registration.this,gender,Toast.LENGTH_SHORT).show();

                final EditText emailField = (EditText) findViewById(R.id.loginEmail);
                String email = emailField.getText().toString();

                final EditText passwordField = (EditText) findViewById(R.id.loginPassword);
                String password = passwordField.getText().toString();

//                Toast.makeText(Login.this,password,Toast.LENGTH_SHORT).show();

                String payload = "{\"email\": \""+email+"\", \"password\": \""+password+"\"}";
                new Login.PostData().execute(payload);
            }
        });

    }

    private class PostData extends AsyncTask< String, Void, Void > {

        String resultvalue;

        protected Void doInBackground(String...params) {

            URL url = null;
            try {
                url = new URL("https://bapfoundation.org/app-login");
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

            try {
                JSONObject jsonObj = new JSONObject(jsonString.toString());
                if(!jsonObj.getString("result").equals("not_existing") && !jsonObj.getString("result").equals("error")) {
                    JSONArray rval = jsonObj.getJSONArray("result");
                    JSONObject j2 = new JSONObject(rval.getString(0));
                    resultvalue = j2.get("id").toString();
                }
                else{
                    resultvalue = jsonObj.getString("result");
                }
                System.out.println("JSOOOOOOOOOOOOOO"+resultvalue);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //do stuff
            System.out.println("inpostexecute------"+resultvalue);

            if(!resultvalue.equals("error") && !resultvalue.equals("not_existing")){
                Intent intent = new Intent(Login.this, DeviceSetup1.class);
                startActivity(intent);
                finish();
            }
            else if (resultvalue.equals("not_existing")){
                final TextView heading = findViewById(R.id.errorText);
                heading.setVisibility(View.VISIBLE);
                heading.setText("There is an error in the email or password.");

                Button loginButton = findViewById(R.id.loginButton);
                loginButton.setEnabled(true);
                loginButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            else{
                final TextView heading = findViewById(R.id.errorText);
                heading.setVisibility(View.VISIBLE);
                heading.setText("There was some error. Please try again in a few minutes.");

                Button loginButton = findViewById(R.id.loginButton);
                loginButton.setEnabled(true);
                loginButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        }
    }

}