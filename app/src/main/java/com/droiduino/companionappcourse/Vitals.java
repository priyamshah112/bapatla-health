package com.droiduino.companionappcourse;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Vitals extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vitals);

        // final TextView heading = findViewById(R.id.heading);
        // heading.setText("Did you know");

        //APP BAR PROPERTIES
        // getSupportActionBar().hide(); // hides appbar
        getSupportActionBar().setTitle("VITALS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //displays back button on app bar

        final TextView body_temperature_field = findViewById(R.id.body_temperature);

        body_temperature_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                System.out.println("text changed=="+s.toString());
                if(s.toString().length()!=0){
                    float tempr = Float.parseFloat(s.toString());
                    if(tempr>=96 && tempr<99){
                        body_temperature_field.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.vitals_border_green));
                    }
                    else if(tempr>=99 && tempr<104){
                        body_temperature_field.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.vitals_border_yellow));
                    }
                    else if(tempr>=104){
                        body_temperature_field.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.vitals_border_red));
                    }
                    else {
                        body_temperature_field.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_corners));
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        final TextView heartrate_field = findViewById(R.id.heartrate);

        heartrate_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                System.out.println("text changed=="+s.toString());
                if(s.toString().length()!=0){
                    float tempr = Float.parseFloat(s.toString());
                    if(tempr>=41 && tempr<50){
                        heartrate_field.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.vitals_border_yellow));
                    }
                    else if(tempr>=50 && tempr<71){
                        heartrate_field.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.vitals_border_green));
                    }
                    else if(tempr>=71){
                        heartrate_field.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.vitals_border_yellow));
                    }
                    else {
                        heartrate_field.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_corners));
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        final TextView rr_field = findViewById(R.id.rr);

        rr_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                System.out.println("text changed=="+s.toString());
                if(s.toString().length()!=0){
                    float tempr = Float.parseFloat(s.toString());
                    if(tempr>=11 && tempr<12){
                        rr_field.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.vitals_border_yellow));
                    }
                    else if(tempr>=12 && tempr<21){
                        rr_field.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.vitals_border_green));
                    }
                    else if(tempr>=20){
                        rr_field.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.vitals_border_yellow));
                    }
                    else {
                        rr_field.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_corners));
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        final TextView bpsys_field = findViewById(R.id.bpsys);

        bpsys_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                System.out.println("text changed=="+s.toString());
                if(s.toString().length()!=0){
                    float tempr = Float.parseFloat(s.toString());
                    if(tempr>=101 && tempr<110){
                        bpsys_field.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.vitals_border_yellow));
                    }
                    else if(tempr>=110 && tempr<151){
                        bpsys_field.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.vitals_border_green));
                    }
                    else if(tempr>=151){
                        bpsys_field.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.vitals_border_yellow));
                    }
                    else {
                        bpsys_field.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_corners));
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        final TextView bpdia_field = findViewById(R.id.bpdia);

        bpdia_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                System.out.println("text changed=="+s.toString());
                if(s.toString().length()!=0){
                    float tempr = Float.parseFloat(s.toString());
                    if(tempr>=61 && tempr<70){
                        bpdia_field.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.vitals_border_yellow));
                    }
                    else if(tempr>=70 && tempr<91){
                        bpdia_field.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.vitals_border_green));
                    }
                    else if(tempr>=91){
                        bpdia_field.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.vitals_border_yellow));
                    }
                    else {
                        bpdia_field.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_corners));
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        final TextView spo2_field = findViewById(R.id.spo2);

        spo2_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                System.out.println("text changed=="+s.toString());
                if(s.toString().length()!=0){
                    float tempr = Float.parseFloat(s.toString());
                    if(tempr>=81 && tempr<90){
                        spo2_field.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.vitals_border_red));
                    }
                    else if(tempr>=90 && tempr<95){
                        spo2_field.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.vitals_border_yellow));
                    }
                    else if(tempr>=95){
                        spo2_field.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.vitals_border_green));
                    }
                    else {
                        spo2_field.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_corners));
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        final Button healthButton = findViewById(R.id.healthButton);
        healthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(body_temperature_field.getText().toString().equals("") && heartrate_field.getText().toString().equals("") && rr_field.getText().toString().equals("") && bpsys_field.getText().toString().equals("") && bpdia_field.getText().toString().equals("") && spo2_field.getText().toString().equals("")) {
                    // body_temperature_field.setError("Please fill this field.");
                    // return;
                    Intent intent = new Intent(Vitals.this, Health.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    if(body_temperature_field.getText().toString().equals("")){
                        body_temperature_field.setError("Please fill this field.");
                        return;
                    }
                    float body_temperature = Float.parseFloat(body_temperature_field.getText().toString());

                    if(heartrate_field.getText().toString().equals("")){
                        heartrate_field.setError("Please fill this field.");
                        return;
                    }
                    float heartrate = Float.parseFloat(heartrate_field.getText().toString());

                    if(rr_field.getText().toString().equals("")){
                        rr_field.setError("Please fill this field.");
                        return;
                    }
                    float rr = Float.parseFloat(rr_field.getText().toString());

                    if(bpsys_field.getText().toString().equals("")){
                        bpsys_field.setError("Please fill this field.");
                        return;
                    }
                    float bpsys = Float.parseFloat(bpsys_field.getText().toString());

                    if(bpdia_field.getText().toString().equals("")){
                        bpdia_field.setError("Please fill this field.");
                        return;
                    }
                    float bpdia = Float.parseFloat(bpdia_field.getText().toString());

                    if(spo2_field.getText().toString().equals("")){
                        spo2_field.setError("Please fill this field.");
                        return;
                    }
                    float spo2 = Float.parseFloat(spo2_field.getText().toString());

                    healthButton.setEnabled(false);
                    healthButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yy hh:mm a");
                    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
                    String datetime = sdf.format(calendar.getTime());
                    System.out.println(datetime);

                    //getting current user id
                    Session session;//global variable
                    session = new Session(getApplicationContext());
                    int userId = Integer.parseInt(session.getid());

                    String payload = "{\"userId\": \""+userId+"\", \"temperature\": \""+body_temperature+"\", \"heartrate\": \""+heartrate+"\", \"rr\": \""+rr+"\", \"bpsys\": \""+bpsys+"\", \"bpdia\": \""+bpdia+"\", \"spo2\": \""+spo2+"\", \"date_time\": \""+datetime+"\"}";
                    new Vitals.PostData().execute(payload);
                }
            }
        });
    }

    private class PostData extends AsyncTask< String, Void, Void > {

        String resultvalue = "";

        protected Void doInBackground(String...params) {

            URL url = null;
            try {
                url = new URL("https://bapfoundation.org/app-vitals");
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
//            System.out.println(jsonString.toString().substring(11,jsonString.toString().lastIndexOf("\"")));

//            resultvalue = jsonString.toString().substring(11,jsonString.toString().lastIndexOf("\""));

            try {
                JSONObject jsonObj = new JSONObject(jsonString.toString());
                resultvalue = jsonObj.getString("result");

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

            if(!resultvalue.equals("error")){
//                System.out.println("RRRRRRRRRRRRRRRRRRRRRRRRRRRRR"+registname);

                float body_temperature = Float.parseFloat(resultvalue);
                //setting temperature and fever
                Session session;//global variable
                session = new Session(getApplicationContext());
                session.settemperature(body_temperature); //setting it to default 96F
                Fever f = new Fever();
                String fever = f.findfever(body_temperature);
                session.setfever(fever);

                // This is the code to move to another screen
                Intent intent = new Intent(Vitals.this, Health.class);
                startActivity(intent);
                finish();
            }
            else{
                final TextView heading = findViewById(R.id.errorText);
                heading.setVisibility(View.VISIBLE);
                heading.setText("There was some error. Please try again in a few minutes.");

                Button healthButton = findViewById(R.id.healthButton);
                healthButton.setEnabled(true);
                healthButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        }
    }
}