package com.droiduino.companionappcourse;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.github.mikephil.charting.utils.ViewPortHandler;

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

public class DailyTrend extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_trend);

        // final TextView heading = findViewById(R.id.heading);
        // heading.setText("Did you know");

        //APP BAR PROPERTIES
        // getSupportActionBar().hide(); // hides appbar
        getSupportActionBar().setTitle("DAILY TREND");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //displays back button on app bar

        //getting current user id
        Session session;//global variable
        session = new Session(getApplicationContext());
        int userId = Integer.parseInt(session.getid());

        String payload = "{\"userId\": \""+userId+"\"}";
        new DailyTrend.PostData().execute(payload);
    }

    private class PostData extends AsyncTask< String, Void, Void > {

        String resultvalue = "";
        JSONArray timeline;

        protected Void doInBackground(String...params) {

            URL url = null;
            try {
                url = new URL("https://bapfoundation.org/app-get-timeline");
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
                if(!jsonObj.getString("result").equals("not_existing")){
                    timeline = jsonObj.getJSONArray("result");
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

                boolean reading_exists = false; // check if at least one reading exists

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yy");
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
                String todaysdate = sdf.format(calendar.getTime());
                System.out.println("todaysdate==="+todaysdate);

                ArrayList<Entry> lineEntries = new ArrayList<Entry>();
                final ArrayList<String> xLabel = new ArrayList<>();
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

                        //if(date.equals(todaysdate)) {
                            if (j1.getDouble("temperature") != 0) {
                                 System.out.println(j1.get("temperature").toString());

                                float temp = Float.parseFloat(j1.getString("temperature"));

//                                SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yy hh:mm a");
//                                Date mDate = sdf1.parse(date_time);
//                                long timeInMilliseconds = mDate.getTime();
//                                System.out.println(timeInMilliseconds);

                                lineEntries.add(new Entry(counter, temp));
                                xLabel.add(date+"\n"+time);
                                counter+=1;

                            }
                            reading_exists=true;
                        //}
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
//                System.out.println(xLabel);

                if(reading_exists){
                    LineChart chart = (LineChart) findViewById(R.id.chart);
//                    LineData data = new LineData();
                    chart.setTouchEnabled(true);
                    chart.setPinchZoom(true);
                    Collections.sort(lineEntries, new EntryXComparator());

                    LineDataSet lineDataSet = new LineDataSet(lineEntries, "Temperature");
                    lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                    lineDataSet.setHighlightEnabled(true);
                    lineDataSet.setLineWidth(2);
                    lineDataSet.setColor(R.color.colorPrimary);
                    lineDataSet.setCircleColor(R.color.colorGreen);
                    lineDataSet.setCircleRadius(6);
                    lineDataSet.setCircleHoleRadius(3);
                    lineDataSet.setDrawHighlightIndicators(true);
                    lineDataSet.setHighLightColor(Color.RED);
                    lineDataSet.setValueTextSize(12);
                    lineDataSet.setValueTextColor(R.color.colorRed);

                    LineData lineData = new LineData(lineDataSet);

                    chart.getDescription().setText("Temperature");
                    chart.getDescription().setTextSize(12);
                    chart.setDrawMarkers(true);
                    chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                    chart.getXAxis().setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getAxisLabel(float value, AxisBase axis) {
                            System.out.println("xxxx="+xLabel+" value="+value);
                            if(timeline.length()==1)
                                value=1.0f;
                            return xLabel.get((int)value-1);
//                            return "your text"+value;
                        }
                    });
//                     chart.getXAxis().setValueFormatter(new MyXAxisValueFormatter());
                    chart.animateY(1000);
                    chart.getXAxis().setGranularityEnabled(true);
                    chart.getXAxis().setGranularity(1.0f);
//                    chart.getXAxis().setLabelCount(lineDataSet.getEntryCount());
                    chart.getXAxis().setLabelCount(5);
                    chart.setData(lineData);
                    chart.invalidate();
                }


            }
            else{

            }
        }
    }
}