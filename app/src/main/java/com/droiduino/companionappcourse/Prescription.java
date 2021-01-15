package com.droiduino.companionappcourse;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class Prescription extends AppCompatActivity {

//    private Button button;
    Dialog dialog;
    final Calendar myCalendar = Calendar.getInstance();
    EditText edittext;
    EditText time;

    EditText dedittext;
    EditText dtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prescription);

        dialog=new Dialog(Prescription.this);
        dialog.setContentView(R.layout.reminder_dialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        Button clear = dialog.findViewById(R.id.reminder_clear);
        Button save = dialog.findViewById(R.id.reminder_save);


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Prescription.this, "clear", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Prescription.this, "save", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

//        button = findViewById(R.id.dialog_btn);
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.show();
//            }
//        });

        // final TextView heading = findViewById(R.id.heading);
        // heading.setText("Did you know");

        //APP BAR PROPERTIES
        // getSupportActionBar().hide(); // hides appbar
        getSupportActionBar().setTitle("PRESCRIPTION");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //displays back button on app bar

        edittext = (EditText) findViewById(R.id.date);
        final String[] dates = new String[1];

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

                dates[0] = sdf.format(myCalendar.getTime());
                edittext.setText(sdf.format(myCalendar.getTime()));
                System.out.println(dates[0]);
            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                System.out.println("here");
                new DatePickerDialog(Prescription.this, R.style.DialogTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final String[] times = new String[1];
        time = (EditText) findViewById(R.id.time);
        time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Prescription.this, R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
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

                        times[0] = selHour+":"+selMin;
                        time.setText( selHour + ":" + selMin);
                        System.out.println(times[0]);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        //for dialog
        dedittext = (EditText) dialog.findViewById(R.id.ddate);
        final String[] ddates = new String[1];

        final DatePickerDialog.OnDateSetListener ddate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                ddates[0] = sdf.format(myCalendar.getTime());
                dedittext.setText(sdf.format(myCalendar.getTime()));
                System.out.println(ddates[0]);
            }

        };

        dedittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                System.out.println("here");
                new DatePickerDialog(Prescription.this, R.style.DialogTheme, ddate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final String[] dtimes = new String[1];
        dtime = (EditText) dialog.findViewById(R.id.dtime);
        dtime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Prescription.this, R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
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

                        dtimes[0] = selHour+":"+selMin;
                        dtime.setText( selHour + ":" + selMin);
                        System.out.println(dtimes[0]);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.prescription_topnav, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case R.id.menu_item:   //this item has your app icon
                dialog.show();

                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
}