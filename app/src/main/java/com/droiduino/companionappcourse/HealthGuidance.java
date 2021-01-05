package com.droiduino.companionappcourse;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
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
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

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
        String fever = session.getfever();
        float temperature = session.gettemperature();

        final TextView health_guidance_fever_text = (TextView)findViewById(R.id.health_guidance_fever_text);
        health_guidance_fever_text.setText(fever);

        ImageView health_guidance_temperature_bar = (ImageView)findViewById(R.id.health_guidance_temperature_bar);

        if(temperature<99){
//            return("No Fever");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                health_guidance_temperature_bar.setImageDrawable(getResources().getDrawable(R.drawable.no_fever_bar, getApplicationContext().getTheme()));
            }
            else{
                health_guidance_temperature_bar.setImageDrawable(getResources().getDrawable(R.drawable.no_fever_bar));
            }
            health_guidance_fever_text.setTextColor(0xAA74BD4E);
        }
        else if(temperature>=99 && temperature<101){
//            return("Mild Fever");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                health_guidance_temperature_bar.setImageDrawable(getResources().getDrawable(R.drawable.mild_fever_bar, getApplicationContext().getTheme()));
            }
            else{
                health_guidance_temperature_bar.setImageDrawable(getResources().getDrawable(R.drawable.mild_fever_bar));
            }
            health_guidance_fever_text.setTextColor(0xAAF1D316);
        }
        else if(temperature>=101 && temperature<104){
//            return("Moderate Fever");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                health_guidance_temperature_bar.setImageDrawable(getResources().getDrawable(R.drawable.moderate_fever_bar, getApplicationContext().getTheme()));
            }
            else{
                health_guidance_temperature_bar.setImageDrawable(getResources().getDrawable(R.drawable.moderate_fever_bar));
            }
            health_guidance_fever_text.setTextColor(0xAAF39700);
        }
        else if(temperature>=104){
//            return("High Fever");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                health_guidance_temperature_bar.setImageDrawable(getResources().getDrawable(R.drawable.high_fever_bar, getApplicationContext().getTheme()));
            }
            else{
                health_guidance_temperature_bar.setImageDrawable(getResources().getDrawable(R.drawable.high_fever_bar));
            }
            health_guidance_fever_text.setTextColor(0xAAD44E57);
        }
        else {
//            return "No Fever";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                health_guidance_temperature_bar.setImageDrawable(getResources().getDrawable(R.drawable.no_fever_bar, getApplicationContext().getTheme()));
            }
            else{
                health_guidance_temperature_bar.setImageDrawable(getResources().getDrawable(R.drawable.no_fever_bar));
            }
            health_guidance_fever_text.setTextColor(0xAA74BD4E);
        }

        final TextView health_guidance_temperature = (TextView)findViewById(R.id.health_guidance_temperature);
        health_guidance_temperature.setText(Float.toString(temperature));

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
        JSONArray covid_vaccination;

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
                    covid_vaccination = jsonObj.getJSONArray("covid_vaccination");
                }
                else{
                    resultvalue = jsonObj.getString("result");
                    birthdate = jsonObj.getJSONArray("age").getJSONObject(0).getString("birthday");
                    covid_vaccination = jsonObj.getJSONArray("covid_vaccination");
                }

                System.out.println("JSOOOOOOOOOOOOOO"+covid_vaccination);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //do stuff
            System.out.println("inpostexecute------"+resultvalue);

//          System.out.println("HERRRRROOOO");
//          System.out.println(timeline);
            Session session;//global variable
            session = new Session(getApplicationContext());

            Typeface attenroundnewregular = ResourcesCompat.getFont(getApplicationContext(), R.font.attenroundnewregular);

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
                FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(8,8,0,0);
                agetv.setLayoutParams(params);
                agetv.setTextColor(Color.parseColor("#000000"));
                agetv.setPadding(16, 5, 16, 5);
                agetv.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_corners_health_guidance));
                agetv.setGravity(Gravity.CENTER_VERTICAL);
                agetv.setTypeface(attenroundnewregular);
                agetv.setTextSize(20);
                agetv.setText(difference_In_Years+" years old");

                flexboxLayout.addView(agetv);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(!resultvalue.equals("not_existing")){
                //symptoms ani pre existing conditions ghe ani print kar

                String symptoms=session.getSymptomsFlag();
                String preexistingconditions=session.getConditionsFlag();
                String covid = session.getVitalsCovid19Flag();
                System.out.println(covid);
                if(symptoms!="") {
                    for (int i = 0; i <timeline.length(); i++) {
                        try {
                            JSONObject j1 = new JSONObject(timeline.getString(i));
                            // System.out.println(j1);
                            if(!j1.getString("symptoms_reported").equals("")){
                                String allsymptoms = j1.getString("symptoms_reported");
                                System.out.println(allsymptoms);

                                String[] onesymptom = allsymptoms.split(",");
                                for(int j=0;j<onesymptom.length;j++){
                                    System.out.println(onesymptom[j]);
                                    TextView agetv = new TextView(getApplicationContext());
                                    FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
                                    params.setMargins(8,8,0,0);
                                    agetv.setLayoutParams(params);
                                    agetv.setTextColor(Color.parseColor("#000000"));
                                    agetv.setPadding(16, 5, 16, 5);
                                    agetv.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_corners_health_guidance));
                                    agetv.setGravity(Gravity.CENTER_VERTICAL);
                                    agetv.setTypeface(attenroundnewregular);
                                    agetv.setTextSize(20);
                                    agetv.setText(onesymptom[j].trim());

                                    flexboxLayout.addView(agetv);

                                }

                                break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if(preexistingconditions!="") {
                    for (int i = 0; i <timeline.length(); i++) {
                        try {
                            JSONObject j1 = new JSONObject(timeline.getString(i));
                            // System.out.println(j1);
                            if(!j1.getString("preexisting_conditions").equals("")){
                                String allconditions = j1.getString("preexisting_conditions");
                                System.out.println(allconditions);

                                String[] onecondition = allconditions.split(",");
                                for(int j=0;j<onecondition.length;j++){
                                    System.out.println(onecondition[j]);
                                    TextView agetv = new TextView(getApplicationContext());
                                    FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
                                    params.setMargins(8,8,0,0);
                                    agetv.setLayoutParams(params);
                                    agetv.setTextColor(Color.parseColor("#000000"));
                                    agetv.setPadding(16, 5, 16, 5);
                                    agetv.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_corners_health_guidance));
                                    agetv.setGravity(Gravity.CENTER_VERTICAL);
                                    agetv.setTypeface(attenroundnewregular);
                                    agetv.setTextSize(20);
                                    agetv.setText(onecondition[j].trim());

                                    flexboxLayout.addView(agetv);

                                }

                                break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if(covid!="") {
                    for (int i = covid_vaccination.length()-1; i>=0; i--) {
                        try {
                            JSONObject j1 = new JSONObject(covid_vaccination.getString(i));
                            System.out.println(j1);
                            String date_of_vaccination = j1.getString("date_of_vaccination");
                            String time_of_vaccination = j1.getString("time_of_vaccination");
                            int dose = j1.getInt("dose");
                            String facility_name = j1.getString("facility_name");
                            String facility_location = j1.getString("facility_location");

                            LinearLayout vaccination_report = findViewById(R.id.vaccination_report);
                            vaccination_report.setVisibility(View.VISIBLE);

                            TextView temptv = new TextView(getApplicationContext());
                            LinearLayout.LayoutParams temptvparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            temptvparams.setMargins(0,10,0,0);
                            temptv.setLayoutParams(temptvparams);
                            temptv.setTextColor(Color.parseColor("#00AAAA"));
                            temptv.setGravity(Gravity.CENTER_HORIZONTAL);
                            temptv.setTypeface(attenroundnewregular);
                            temptv.setTextSize(15);
                            temptv.setText("Covid 19 Vaccination Report");

                            TextView datetimetv = new TextView(getApplicationContext());
                            LinearLayout.LayoutParams datetimetvparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            datetimetvparams.setMargins(10,13,0,0);
                            datetimetv.setLayoutParams(datetimetvparams);
                            datetimetv.setTextColor(Color.parseColor("#000000"));
//                            datetimetv.setGravity(Gravity.CENTER_HORIZONTAL);
                            datetimetv.setTypeface(attenroundnewregular);
                            datetimetv.setTextSize(15);
                            datetimetv.setText(date_of_vaccination+", "+time_of_vaccination);

                            TextView reporttv = new TextView(getApplicationContext());
                            LinearLayout.LayoutParams reporttvparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            reporttvparams.setMargins(10,13,0,20);
                            reporttv.setLayoutParams(reporttvparams);
                            reporttv.setTextColor(Color.parseColor("#000000"));
                            reporttv.setTypeface(attenroundnewregular);
                            reporttv.setTextSize(15);
                            reporttv.setText("Vaccination done at "+facility_name+", "+facility_location+" and dosage given "+dose);

                            vaccination_report.addView(temptv);
                            vaccination_report.addView(datetimetv);
                            vaccination_report.addView(reporttv);

                            break;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            session.destroyFlags();
        }
    }
}