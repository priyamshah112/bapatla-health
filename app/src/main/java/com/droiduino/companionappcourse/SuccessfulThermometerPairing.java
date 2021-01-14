package com.droiduino.companionappcourse;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.nio.file.FileVisitOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(false); //displays back button on app bar

        final Button redirectToHomeButton = findViewById(R.id.redirectToHomeButton);
        redirectToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                redirectToHomeButton.setEnabled(false);
                redirectToHomeButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yy hh:mm a");
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
                String datetime = sdf.format(calendar.getTime());
                System.out.println(datetime);

                //getting current user id
                Session session;//global variable
                session = new Session(getApplicationContext());
                int userId = Integer.parseInt(session.getid());

                float body_temperature = 96;

                String payload = "{\"userId\": \""+userId+"\", \"temperature\": \""+body_temperature+"\", \"date_time\": \""+datetime+"\"}";
                new SuccessfulThermometerPairing.PostData().execute(payload);
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

                float body_temperature = 96;
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
            else{
                final TextView heading = findViewById(R.id.errorText);
                heading.setVisibility(View.VISIBLE);
                heading.setText("There was some error. Please try again in a few minutes.");

                Button redirectToHomeButton = findViewById(R.id.redirectToHomeButton);
                redirectToHomeButton.setEnabled(true);
                redirectToHomeButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        }
    }

}
