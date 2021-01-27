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

public class Covid19AEFI2 extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();
    EditText edittext;
    EditText edittext2;
    EditText timeofadverseevents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.covid_19_aefi2);

        // final TextView heading = findViewById(R.id.heading);
        // heading.setText("Did you know");

        //APP BAR PROPERTIES
        // getSupportActionBar().hide(); // hides appbar
        getSupportActionBar().setTitle("COVID-19 AEFI");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //displays back button on app bar

        final Spinner aefsdropdown = findViewById(R.id.aefsdropdown);
        //create a list of items for the spinner.
        String[] items = new String[]{"Vaccine-product related", "Vaccine quality related", "Immunisation-error related", "Immunisation stress-related", "Coincidental event"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        aefsdropdown.setAdapter(adapter);

        edittext = (EditText) findViewById(R.id.dateofadverseevents);
        final String[] dateofadverseevents = new String[1];

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

                dateofadverseevents[0] = sdf.format(myCalendar.getTime());

                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                edittext.setText(sdf1.format(myCalendar.getTime()));
                System.out.println(dateofadverseevents[0]);
            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                System.out.println("here");
                new DatePickerDialog(Covid19AEFI2.this, R.style.DialogTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final String[] time_of_ae = new String[1];
        timeofadverseevents = (EditText) findViewById(R.id.timeofadverseevents);
        timeofadverseevents.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Covid19AEFI2.this, R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
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

                        time_of_ae[0] = selHour+":"+selMin;
                        timeofadverseevents.setText( selHour + ":" + selMin);
                        System.out.println(time_of_ae[0]);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        edittext2 = (EditText) findViewById(R.id.dateofdeath);
        final String[] dateofdeath = new String[1];

        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                dateofdeath[0] = sdf.format(myCalendar.getTime());

                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                edittext2.setText(sdf1.format(myCalendar.getTime()));
                System.out.println(dateofdeath[0]);
            }

        };

        edittext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                System.out.println("here");
                new DatePickerDialog(Covid19AEFI2.this, R.style.DialogTheme, date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Button covid19aefi2nextbutton = findViewById(R.id.covid19aefi2nextbutton);
        covid19aefi2nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This is the code to move to another screen
                Intent intent = new Intent(Covid19AEFI2.this, Covid19AEFI3.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
