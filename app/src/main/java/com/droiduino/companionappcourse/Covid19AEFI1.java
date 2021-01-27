package com.droiduino.companionappcourse;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class Covid19AEFI1 extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();
    EditText edittext;
    EditText timeofvaccination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.covid_19_aefi1);

        // final TextView heading = findViewById(R.id.heading);
        // heading.setText("Did you know");

        //APP BAR PROPERTIES
        // getSupportActionBar().hide(); // hides appbar
        getSupportActionBar().setTitle("COVID-19 AEFI");
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
                new DatePickerDialog(Covid19AEFI1.this, R.style.DialogTheme, date, myCalendar
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
                mTimePicker = new TimePickerDialog(Covid19AEFI1.this, R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
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

        final Spinner vaccinemanufacturerdropdown = findViewById(R.id.vaccinemanufacturerdropdown);
        //create a list of items for the spinner.
        String[] itemsv = new String[]{"Serum Institute of India(Covishield)", "Bharath Biotech(Covaxin)", "Zydus Candulla(ZyCov-D)", "Pfizer inc(Pfizer-BioNTech covid -19 Vaccine)", "Dr Reddy labs (Sputnik V covid-19 vaccine)"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapterv = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsv);
        //set the spinners adapter to the previously created one.
        vaccinemanufacturerdropdown.setAdapter(adapterv);

        Button covid19aefi1nextbutton = findViewById(R.id.covid19aefi1nextbutton);
        covid19aefi1nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This is the code to move to another screen
                Intent intent = new Intent(Covid19AEFI1.this, Covid19AEFI2.class);
                startActivity(intent);
                finish();
            }
        });
    }
}