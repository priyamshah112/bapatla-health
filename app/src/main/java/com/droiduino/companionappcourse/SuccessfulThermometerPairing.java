package com.droiduino.companionappcourse;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SuccessfulThermometerPairing extends AppCompatActivity {

    LocationCallback locationCallback;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.successful_thermometer_pairing);

        // final TextView heading = findViewById(R.id.heading);
        // heading.setText("Did you know");

        //APP BAR PROPERTIES
        // getSupportActionBar().hide(); // hides appbar
//        getSupportActionBar().setTitle("DEVICE SETUP");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false); //displays back button on app bar

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.custom_actionbar, null);
        ActionBar.LayoutParams p = new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        ((TextView) v.findViewById(R.id.title)).setText("DEVICE SETUP");
        getSupportActionBar().setCustomView(v, p);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        final Button redirectToHomeButton = findViewById(R.id.redirectToHomeButton);
        redirectToHomeButton.setEnabled(false);
        redirectToHomeButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        final float[] currentlatitude = new float[1];
        final float[] currentlongitude = new float[1];

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(30 * 1000)
                .setFastestInterval(5 * 1000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                //Location received
                System.out.println("LOCATIONNN");
                System.out.println(locationResult.getLocations().get(0).getLatitude());
                System.out.println(locationResult.getLocations().get(0).getLongitude());

                currentlatitude[0]= (float) locationResult.getLocations().get(0).getLatitude();
                currentlongitude[0] = (float) locationResult.getLocations().get(0).getLongitude();

                fusedLocationProviderClient.removeLocationUpdates(locationCallback);

                redirectToHomeButton.setEnabled(true);
                redirectToHomeButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_corner_button));
            }
        };

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);


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

                session.settemperature(96); //setting it to default 96F
                Fever f = new Fever();
                String fever = f.findfever(96);
                session.setfever(fever);

                // This is the code to move to another screen
                Intent intent = new Intent(SuccessfulThermometerPairing.this, BottomNav.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

//                String payload = "{\"userId\": \""+userId+"\", \"temperature\": \""+body_temperature+"\", \"date_time\": \""+datetime+"\", \"latitude\": \""+currentlatitude[0]+"\", \"longitude\": \""+currentlongitude[0]+"\"}";
//                new SuccessfulThermometerPairing.PostData().execute(payload);
            }
        });
    }

    private class PostData extends AsyncTask< String, Void, Void > {

        String resultvalue = "";

        protected Void doInBackground(String...params) {

            URL url = null;
            try {
                url = new URL("https://bapfoundation.org/app-store-temperature");
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

            if(!resultvalue.equals("error") && !resultvalue.equals("")){
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
                redirectToHomeButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_corner_button));
            }
        }
    }

}
