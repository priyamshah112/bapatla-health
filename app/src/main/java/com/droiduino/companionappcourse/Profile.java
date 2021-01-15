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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Profile extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();
    EditText edittext;

    final String[] birthday = new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        Session session;//global variable
        session = new Session(getApplicationContext());
        final String id = session.getid();

        // final TextView heading = findViewById(R.id.heading);
        // heading.setText("Did you know");

        //APP BAR PROPERTIES
        // getSupportActionBar().hide(); // hides appbar
        getSupportActionBar().setTitle("PROFILE");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //displays back button on app bar

        edittext = (EditText) findViewById(R.id.birthday);

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
                new DatePickerDialog(Profile.this, R.style.DialogTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final Button updateprofilebutton = findViewById(R.id.updateprofilebutton);
        updateprofilebutton.setEnabled(false);
        updateprofilebutton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disabled_rounded_corner_button));

        String payload = "{\"userId\": \""+id+"\"}";
        new Profile.PostData1().execute(payload);

        // update profile part
        updateprofilebutton.setOnClickListener(new View.OnClickListener() {
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

                final EditText surnameField = (EditText) findViewById(R.id.surname);
                String surname = surnameField.getText().toString();

                final EditText aadhar_numberField = (EditText) findViewById(R.id.aadhar_number);
                String aadhar_number = aadhar_numberField.getText().toString();

                final EditText mobileField = (EditText) findViewById(R.id.mobile);
                String mobile = mobileField.getText().toString();

                final EditText addressField = (EditText) findViewById(R.id.address);
                String address = addressField.getText().toString();

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
                if(TextUtils.isEmpty(address)) {
                    addressField.setError("Please fill this field.");
                    return;
                }

                updateprofilebutton.setEnabled(false);
                updateprofilebutton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.disabled_rounded_corner_button));

                System.out.println(birthday[0]);
                String payload2 = "{\"userId\": \""+id+"\", \"surname\": \""+surname+"\", \"aadhar_number\": \""+aadhar_number+"\", \"mobile\": \""+mobile+"\", \"address\": \""+address+"\", \"gender\": \""+gender+"\", \"birthday\": \""+ birthday[0] +"\"}";
                new Profile.PostData2().execute(payload2);
            }
        });

    }

    private class PostData1 extends AsyncTask< String, Void, Void > {

        String resultvalue = "";
        JSONArray userinfo;

        protected Void doInBackground(String...params) {

            URL url = null;
            try {
                url = new URL("https://bapfoundation.org/app-get-user-data");
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
                if(jsonObj.getString("result").equals("error") || jsonObj.getString("result").equals("not_existing")){
                    resultvalue = jsonObj.getString("result");
                }
                else{
                    userinfo = jsonObj.getJSONArray("result");
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

            if(!resultvalue.equals("error") && !resultvalue.equals("not_existing")){
//                System.out.println("RRRRRRRRRRRRRRRRRRRRRRRRRRRRR"+userinfo);
                JSONObject jobj = null;
                try {
                    jobj = new JSONObject(userinfo.getString(0));
                    System.out.println(jobj.getString("aadhar_number"));

                    TextView name = findViewById(R.id.name);
                    name.setText(jobj.getString("name"));

                    TextView surname = findViewById(R.id.surname);
                    surname.setText(jobj.getString("surname"));

                    TextView aadhar_number = findViewById(R.id.aadhar_number);
                    aadhar_number.setText(jobj.getString("aadhar_number"));

                    TextView birthdayfield = findViewById(R.id.birthday);
                    birthdayfield.setText(jobj.getString("birthday"));
                    birthday[0] = jobj.getString("birthday");

                    TextView mobile = findViewById(R.id.mobile);
                    mobile.setText(jobj.getString("mobile"));

                    TextView email = findViewById(R.id.email);
                    email.setText(jobj.getString("email"));

                    TextView address = findViewById(R.id.address);
                    address.setText(jobj.getString("address"));

                    if(jobj.getString("gender").equals("Male")){
                        RadioButton male = (RadioButton) findViewById(R.id.male);
                        male.setChecked(true);
                    }
                    else if(jobj.getString("gender").equals("Female")){
                        RadioButton female = (RadioButton) findViewById(R.id.female);
                        female.setChecked(true);
                    }
                    else{
                        RadioButton others = (RadioButton) findViewById(R.id.others);
                        others.setChecked(true);
                    }

                    final Button updateprofilebutton = findViewById(R.id.updateprofilebutton);
                    updateprofilebutton.setEnabled(true);
                    updateprofilebutton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_corner_button));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else{
                Toast.makeText(Profile.this, "There was some error while fetching your data.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class PostData2 extends AsyncTask< String, Void, Void > {

        String resultvalue = "";

        protected Void doInBackground(String...params) {

            URL url = null;
            try {
                url = new URL("https://bapfoundation.org/app-update-profile");
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
//                System.out.println("RRRRRRRRRRRRRRRRRRRRRRRRRRRRR"+userinfo);
                Toast.makeText(Profile.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(Profile.this, "There was some error.", Toast.LENGTH_SHORT).show();
            }
            final Button updateprofilebutton = findViewById(R.id.updateprofilebutton);
            updateprofilebutton.setEnabled(true);
            updateprofilebutton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_corner_button));
        }
    }
}