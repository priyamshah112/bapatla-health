package com.droiduino.companionappcourse;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

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
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class Covid19Vaccine extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();
    EditText edittext;
    EditText timeofvaccination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.covid_19_vaccine);

        // final TextView heading = findViewById(R.id.heading);
        // heading.setText("Did you know");

        //APP BAR PROPERTIES
        // getSupportActionBar().hide(); // hides appbar
        getSupportActionBar().setTitle("COVID-19 VACCINE");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //displays back button on app bar

        edittext = (EditText) findViewById(R.id.dateofvaccination);
        final String[] dateofvaccination = new String[1];

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                dateofvaccination[0] = sdf.format(myCalendar.getTime());

                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                edittext.setText(sdf1.format(myCalendar.getTime()));
                System.out.println(dateofvaccination[0]);
            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                System.out.println("here");
                new DatePickerDialog(Covid19Vaccine.this, R.style.DialogTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final String[] time_of_vaccination = new String[1];
        timeofvaccination = (EditText) findViewById(R.id.timeofvaccination);
        timeofvaccination.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Covid19Vaccine.this, R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String selHour=String.valueOf(selectedHour);
                        String selMin=String.valueOf(selectedMinute);
                        if(selectedHour/10==0)
                            selHour="0"+selectedHour;
                        if(selectedMinute==0)
                            selMin="00";
                        else if(selectedMinute/10==0)
                            selMin="0"+selectedMinute;

                        time_of_vaccination[0] = selHour+":"+selMin;
                        timeofvaccination.setText( selHour + ":" + selMin);
                        System.out.println(time_of_vaccination[0]);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        //get the spinner from the xml.
        final Spinner doseSpinner = findViewById(R.id.doseSpinner);
        //create a list of items for the spinner.
        String[] items = new String[]{"1", "2"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        doseSpinner.setAdapter(adapter);

        final Button healthGuidanceButton = findViewById(R.id.healthGuidanceButton);
        healthGuidanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckBox positive_before_vaccination_checkbox = findViewById(R.id.positive_before_vaccination);
                int positive_before_vaccination=0;
                if(positive_before_vaccination_checkbox.isChecked()){
                    positive_before_vaccination=1;
                }

                CheckBox pregnant_checkbox = findViewById(R.id.pregnant);
                int pregnant=0;
                if(pregnant_checkbox.isChecked()){
                    pregnant=1;
                }

                CheckBox preexisting_illness_checkbox = findViewById(R.id.preexisting_illness);
                int preexisting_illness=0;
                if(preexisting_illness_checkbox.isChecked()){
                    preexisting_illness=1;
                }

                int dose = Integer.parseInt(doseSpinner.getSelectedItem().toString());
                System.out.println(dose);

                EditText facility_name_field = findViewById(R.id.facility_name);
                EditText facility_location_field = findViewById(R.id.facility_location);

                CheckBox adverse_events_checkbox = findViewById(R.id.adverse_events);
                int adverse_events=0;
                if(adverse_events_checkbox.isChecked()){
                    adverse_events=1;
                }

                if(facility_name_field.getText().toString().equals("") && facility_location_field.getText().toString().equals("")){
                    // This is the code to move to another screen
                    Intent intent = new Intent(Covid19Vaccine.this, HealthGuidance.class);
                    startActivity(intent);
                    finish();
                }
                else{

                    String facility_name = facility_name_field.getText().toString();
                    if(TextUtils.isEmpty(facility_name)) {
                        facility_name_field.setError("Please fill this field.");
                        return;
                    }

                    String facility_location = facility_location_field.getText().toString();
                    if(TextUtils.isEmpty(facility_location)) {
                        facility_location_field.setError("Please fill this field.");
                        return;
                    }

                    healthGuidanceButton.setEnabled(false);
                    healthGuidanceButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                    //getting current user id
                    Session session;//global variable
                    session = new Session(getApplicationContext());
                    int userId = Integer.parseInt(session.getid());

                    String payload = "{\"userId\": \""+userId+"\", \"positive_before_vaccination\": \""+positive_before_vaccination+"\", \"pregnant\": \""+pregnant+"\", \"preexisting_illness\": \""+preexisting_illness+"\", \"date_of_vaccination\": \""+dateofvaccination[0]+"\", \"time_of_vaccination\": \""+time_of_vaccination[0]+"\", \"dose\": \""+dose+"\", \"facility_name\": \""+facility_name+"\", \"facility_location\": \""+ facility_location +"\", \"adverse_events\": \""+ adverse_events +"\"}";
                    new Covid19Vaccine.PostData().execute(payload);

                }
            }
        });
    }

    private class PostData extends AsyncTask< String, Void, Void > {

        String resultvalue = "";

        protected Void doInBackground(String...params) {

            URL url = null;
            try {
                url = new URL("https://bapfoundation.org/app-covid-vaccination-data");
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

            if(resultvalue.equals("success")){
//                System.out.println("RRRRRRRRRRRRRRRRRRRRRRRRRRRRR"+registname);
                Session session;//global variable
                session = new Session(getApplicationContext());
                session.setVitalsCovid19Flag();

                System.out.println("covid:"+session.getVitalsCovid19Flag());
                // session.destroyFlags();

                // This is the code to move to another screen
                Intent intent = new Intent(Covid19Vaccine.this, HealthGuidance.class);
                startActivity(intent);
                finish();
            }
            else{
                final TextView heading = findViewById(R.id.errorText);
                heading.setVisibility(View.VISIBLE);
                heading.setText("There was some error. Please try again in a few minutes.");

                Button healthGuidanceButton = findViewById(R.id.healthGuidanceButton);
                healthGuidanceButton.setEnabled(true);
                healthGuidanceButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        }
    }
}