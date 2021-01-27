package com.droiduino.companionappcourse;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.util.Calendar;
import java.util.TimeZone;

import androidx.appcompat.app.AppCompatActivity;

public class Health extends AppCompatActivity {

    boolean symptompsflag=false;
    boolean preexistingconditionsflag=false;

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

        final Button travelButton = findViewById(R.id.travelButton);
        travelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String symptoms_reported = "";

                CheckBox checkBox1 = findViewById(R.id.checkBox1);
                CheckBox checkBox2 = findViewById(R.id.checkBox2);
                CheckBox checkBox3 = findViewById(R.id.checkBox3);
                CheckBox checkBox4 = findViewById(R.id.checkBox4);
                CheckBox checkBox5 = findViewById(R.id.checkBox5);
                CheckBox checkBox6 = findViewById(R.id.checkBox6);
                CheckBox checkBox7 = findViewById(R.id.checkBox7);
                CheckBox checkBox8 = findViewById(R.id.checkBox8);
                CheckBox checkBox9 = findViewById(R.id.checkBox9);
                CheckBox checkBox10 = findViewById(R.id.checkBox10);
                CheckBox checkBox11 = findViewById(R.id.checkBox11);
                CheckBox checkBox12 = findViewById(R.id.checkBox12);
                CheckBox checkBox13 = findViewById(R.id.checkBox13);
                CheckBox checkBox18 = findViewById(R.id.checkBox18);
                CheckBox checkBox19 = findViewById(R.id.checkBox19);
                CheckBox checkBox21 = findViewById(R.id.checkBox21);
                if(checkBox1.isChecked()){
//                    System.out.println("YESSSSSSSSSSSS"+checkBox1.getText().toString());
                    symptoms_reported += checkBox1.getText().toString().trim();
                }
                if(checkBox2.isChecked()){
                    symptoms_reported += ", "+checkBox2.getText().toString().trim();
                }
                if(checkBox3.isChecked()){
                    symptoms_reported += ", "+checkBox3.getText().toString().trim();
                }
                if(checkBox4.isChecked()){
                    symptoms_reported += ", "+checkBox4.getText().toString().trim();
                }
                if(checkBox5.isChecked()){
                    symptoms_reported += ", "+checkBox5.getText().toString().trim();
                }
                if(checkBox6.isChecked()){
                    symptoms_reported += ", "+checkBox6.getText().toString().trim();
                }
                if(checkBox7.isChecked()){
                    symptoms_reported += ", "+checkBox7.getText().toString().trim();
                }
                if(checkBox8.isChecked()){
                    symptoms_reported += ", "+checkBox8.getText().toString().trim();
                }
                if(checkBox9.isChecked()){
                    symptoms_reported += ", "+checkBox9.getText().toString().trim();
                }
                if(checkBox10.isChecked()){
                    symptoms_reported += ", "+checkBox10.getText().toString().trim();
                }
                if(checkBox11.isChecked()){
                    symptoms_reported += ", "+checkBox11.getText().toString().trim();
                }
                if(checkBox12.isChecked()){
                    symptoms_reported += ", "+checkBox12.getText().toString().trim();
                }
                if(checkBox13.isChecked()){
                    symptoms_reported += ", "+checkBox13.getText().toString().trim();
                }
                if(checkBox18.isChecked()){
                    symptoms_reported += ", "+checkBox18.getText().toString().trim();
                }
                if(checkBox19.isChecked()){
                    symptoms_reported += ", "+checkBox19.getText().toString().trim();
                }
                if(checkBox21.isChecked()){
                    symptoms_reported += ", "+checkBox21.getText().toString().trim();
                }

                String preexisting_conditions = "";
                CheckBox checkBox14 = findViewById(R.id.checkBox14);
                CheckBox checkBox15 = findViewById(R.id.checkBox15);
                CheckBox checkBox16 = findViewById(R.id.checkBox16);
                CheckBox checkBox17 = findViewById(R.id.checkBox17);
                CheckBox checkBox20 = findViewById(R.id.checkBox20);
                if(checkBox14.isChecked()){
                    preexisting_conditions += checkBox14.getText().toString().trim();
                }
                if(checkBox15.isChecked()){
                    preexisting_conditions += ", "+checkBox15.getText().toString().trim();
                }
                if(checkBox16.isChecked()){
                    preexisting_conditions += ", "+checkBox16.getText().toString().trim();
                }
                if(checkBox17.isChecked()){
                    preexisting_conditions += ", "+checkBox17.getText().toString().trim();
                }
                if(checkBox20.isChecked()){
                    preexisting_conditions += ", "+checkBox20.getText().toString().trim();
                }

                System.out.println("SYMPPPPPPP");
                System.out.println(symptoms_reported);
                System.out.println(preexisting_conditions);

                if(symptoms_reported.equals("") && preexisting_conditions.equals("")){
                    // This is the code to move to another screen
                    Intent intent = new Intent(Health.this, Covid19Vaccine.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    travelButton.setEnabled(false);
                    travelButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yy hh:mm a");
                    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
                    String datetime = sdf.format(calendar.getTime());
                    System.out.println(datetime);

                    //getting current user id
                    Session session;//global variable
                    session = new Session(getApplicationContext());
                    int userId = Integer.parseInt(session.getid());

                    if(!symptoms_reported.equals("") && symptoms_reported.charAt(0)==',') {
                        symptoms_reported = symptoms_reported.substring(2);
                        symptompsflag=true;
                    }
                    else if(!symptoms_reported.equals("")){
                        symptompsflag=true;
                    }

                    if(!preexisting_conditions.equals("") && preexisting_conditions.charAt(0)==',') {
                        preexisting_conditions = preexisting_conditions.substring(2);
                        preexistingconditionsflag=true;
                    }
                    else if(!preexisting_conditions.equals("")){
                        preexistingconditionsflag=true;
                    }

                    String payload = "{\"userId\": \""+userId+"\", \"symptoms_reported\": \""+symptoms_reported+"\", \"preexisting_conditions\": \""+preexisting_conditions+"\", \"date_time\": \""+datetime+"\"}";
                    new Health.PostData().execute(payload);
                }
            }
        });
    }

    private class PostData extends AsyncTask< String, Void, Void > {

        String resultvalue = "";

        protected Void doInBackground(String...params) {

            URL url = null;
            try {
                url = new URL("https://bapfoundation.org/app-health-data");
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
                Session session;//global variable
                session = new Session(getApplicationContext());
                if(symptompsflag==true){
                    session.setSymptomsFlag();
                }

                if(preexistingconditionsflag==true){
                    session.setConditionsFlag();
                }

                System.out.println("symptoms:"+session.getSymptomsFlag());
                System.out.println("pre-existing conditions:"+session.getConditionsFlag());
                // session.destroyFlags();

                // This is the code to move to another screen
                Intent intent = new Intent(Health.this, Covid19Vaccine.class);
                startActivity(intent);
                finish();
            }
            else{
                final TextView heading = findViewById(R.id.errorText);
                heading.setVisibility(View.VISIBLE);
                heading.setText("There was some error. Please try again in a few minutes.");

                Button travelButton = findViewById(R.id.travelButton);
                travelButton.setEnabled(true);
                travelButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        }
    }
}
