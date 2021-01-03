package com.droiduino.companionappcourse;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.google.android.flexbox.FlexboxLayout;

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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import androidx.appcompat.app.AppCompatActivity;

public class HealthGuidance extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_guidance);

        // final TextView heading = findViewById(R.id.heading);
        // heading.setText("Did you know");

        //APP BAR PROPERTIES
        // getSupportActionBar().hide(); // hides appbar
        getSupportActionBar().setTitle("HEALTH GUIDANCE");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //displays back button on app bar

        //getting current user id
        Session session;//global variable
        session = new Session(getApplicationContext());
        int userId = Integer.parseInt(session.getid());

        String payload = "{\"userId\": \""+userId+"\"}";
        new HealthGuidance.PostData().execute(payload);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goToBottomNav = new Intent(getApplicationContext(), BottomNav.class);
        goToBottomNav.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Will clear out your activity history stack till now
        startActivity(goToBottomNav);
    }

    private class PostData extends AsyncTask< String, Void, Void > {

        String resultvalue = "";
        JSONArray timeline;
        String birthdate = "";

        protected Void doInBackground(String...params) {

            URL url = null;
            try {
                url = new URL("https://bapfoundation.org/app-get-health-guidance");
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
                System.out.println(jsonObj);
                if(!jsonObj.getString("result").equals("not_existing")){
                    timeline = jsonObj.getJSONArray("result");
                    System.out.println(jsonObj.getJSONArray("age").getJSONObject(0).getString("birthday"));
                    birthdate = jsonObj.getJSONArray("age").getJSONObject(0).getString("birthday");
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

            if(!resultvalue.equals("not_existing")){
//                System.out.println("HERRRRROOOO");
//                System.out.println(timeline);
                Session session;//global variable
                session = new Session(getApplicationContext());
                session.destroyFlags();

                final FlexboxLayout flexboxLayout = findViewById(R.id.flexboxlayout);

                DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                try {
                    Date bday = format.parse(birthdate);
                    Date today = format.parse(format.format(new Date()));
                    long difference_In_Time = today.getTime() - bday.getTime();

                    long difference_In_Years
                            = (difference_In_Time
                            / (1000l * 60 * 60 * 24 * 365));

                    System.out.println("AGE= "+difference_In_Years);

//                    final TextView agetag = findViewById(R.id.agetag);
//                    agetag.setText(difference_In_Years+" years old");

                    TextView agetv = new TextView(getApplicationContext());
                    agetv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    agetv.setTextColor(Color.parseColor("#ffffff"));
                    agetv.setGravity(Gravity.CENTER_VERTICAL);
                    agetv.setTextSize(12);
                    agetv.setText(difference_In_Years+" years old");

                    flexboxLayout.addView(agetv);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //symptoms ani pre existing conditions ghe ani print kar
                int counter=1;
                for(int i=timeline.length()-1;i>=0;i--){
                    try {
//                        System.out.println("i="+i);
                        JSONObject j1 = new JSONObject(timeline.getString(i));
//                        System.out.println(j1);

                        String date_time = j1.getString("date_time");
                        String date = date_time.substring(0,9);
                        String time = date_time.substring(10);
//                        System.out.println(time);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else{

            }
        }
    }
}