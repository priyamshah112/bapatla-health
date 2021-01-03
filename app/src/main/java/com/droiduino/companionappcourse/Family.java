package com.droiduino.companionappcourse;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class Family extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();
    EditText edittext;
    String registname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.family);

        // final TextView heading = findViewById(R.id.heading);
        // heading.setText("Did you know");

        //APP BAR PROPERTIES
        // getSupportActionBar().hide(); // hides appbar
        getSupportActionBar().setTitle("NEW PROFILE");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //displays back button on app bar

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ DATE SELECTION ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~```
        edittext = (EditText) findViewById(R.id.birthday);
        final String[] birthday = new String[1];

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

                birthday[0] = sdf.format(myCalendar.getTime());
                edittext.setText(sdf.format(myCalendar.getTime()));
                System.out.println(birthday[0]);
            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                System.out.println("here");
                new DatePickerDialog(Family.this, R.style.DialogTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ DATE SELECTION END ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        final Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This is the code to move to another screen
                // Intent intent = new Intent(Registration.this, DeviceSetup1.class);
                // startActivity(intent);
                // finish();

                RadioGroup gender_radio=(RadioGroup)findViewById(R.id.gender_radio);

                int selectedId=gender_radio.getCheckedRadioButtonId();
                RadioButton radioGenderButton=(RadioButton)findViewById(selectedId);
                String gender= (String) radioGenderButton.getText();
                // Toast.makeText(Registration.this,gender,Toast.LENGTH_SHORT).show();

                final EditText nameField = (EditText) findViewById(R.id.name);
                String name = nameField.getText().toString();
                registname = name;

                final EditText surnameField = (EditText) findViewById(R.id.surname);
                String surname = surnameField.getText().toString();

                final EditText aadhar_numberField = (EditText) findViewById(R.id.aadhar_number);
                String aadhar_number = aadhar_numberField.getText().toString();

                final EditText mobileField = (EditText) findViewById(R.id.mobile);
                String mobile = mobileField.getText().toString();

                final EditText emailField = (EditText) findViewById(R.id.email);
                String email = emailField.getText().toString();

                final EditText addressField = (EditText) findViewById(R.id.address);
                String address = addressField.getText().toString();

                if(TextUtils.isEmpty(name)) {
                    nameField.setError("Please fill this field.");
                    return;
                }
                if(TextUtils.isEmpty(surname)) {
                    surnameField.setError("Please fill this field.");
                    return;
                }
                if(TextUtils.isEmpty(aadhar_number)) {
                    aadhar_numberField.setError("Please fill this field.");
                    return;
                }
                if(TextUtils.isEmpty(mobile)) {
                    mobileField.setError("Please fill this field.");
                    return;
                }
                if(TextUtils.isEmpty(email)) {
                    emailField.setError("Please fill this field.");
                    return;
                }
                if(TextUtils.isEmpty(address)) {
                    addressField.setError("Please fill this field.");
                    return;
                }

                registerButton.setEnabled(false);
                registerButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                System.out.println(birthday[0]);

                Session session;//global variable
                session = new Session(getApplicationContext());
                String currentUserId = session.getPrimaryId();

                String payload = "{\"name\": \""+name+"\", \"surname\": \""+surname+"\", \"aadhar_number\": \""+aadhar_number+"\", \"mobile\": \""+mobile+"\", \"email\": \""+email+"\", \"address\": \""+address+"\", \"gender\": \""+gender+"\", \"birthday\": \""+ birthday[0] +"\", \"currentUserId\": \""+ currentUserId +"\"}";
                new Family.PostData().execute(payload);
            }
        });
    }

    private class PostData extends AsyncTask< String, Void, Void > {

        String resultvalue = "";

        protected Void doInBackground(String...params) {

            URL url = null;
            try {
                url = new URL("https://bapfoundation.org/app-register-family");
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

            if(!resultvalue.equals("already_registered") && !resultvalue.equals("error")){
                System.out.println("RRRRRRRRRRRRRRRRRRRRRRRRRRRRR"+registname);

                // adding to allusers list in tinydb
                TinyDB tinydb = new TinyDB(getApplicationContext());
                ArrayList<String> allusers = tinydb.getListString("allusers");
                allusers.add(resultvalue);
                allusers.add(registname);
                tinydb.putListString("allusers", allusers);

                Toast.makeText(Family.this, "Family member successfully registered.", Toast.LENGTH_SHORT).show();

                // reloading activity
                finish();
                startActivity(getIntent());
            }
            else if (resultvalue.equals("already_registered")){
                final TextView heading = findViewById(R.id.errorText);
                heading.setVisibility(View.VISIBLE);
                heading.setText("A user with this email already exists.");

                Button registerButton = findViewById(R.id.registerButton);
                registerButton.setEnabled(true);
                registerButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            else{
                final TextView heading = findViewById(R.id.errorText);
                heading.setVisibility(View.VISIBLE);
                heading.setText("There was some error. Please try again in a few minutes.");

                Button registerButton = findViewById(R.id.registerButton);
                registerButton.setEnabled(true);
                registerButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        }
    }
}