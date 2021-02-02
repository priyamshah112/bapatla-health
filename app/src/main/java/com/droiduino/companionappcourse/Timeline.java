package com.droiduino.companionappcourse;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

public class Timeline extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);

        // final TextView heading = findViewById(R.id.heading);
        // heading.setText("Did you know");

        //APP BAR PROPERTIES
        // getSupportActionBar().hide(); // hides appbar
        getSupportActionBar().setTitle("TIME LINE");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //displays back button on app bar

        //getting current user id
        Session session;//global variable
        session = new Session(getApplicationContext());
        int userId = Integer.parseInt(session.getid());

        String payload = "{\"userId\": \""+userId+"\"}";
        new Timeline.PostData().execute(payload);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.timeline_top_nav, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case R.id.menu_item:   //this item has your app icon
                BottomSheetDialog bottomSheet = new BottomSheetDialog();
                bottomSheet.show(getSupportFragmentManager(),"ModalBottomSheet");
//                return true;
            default: return super.onOptionsItemSelected(item);
        }
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
                LinearLayout parent = (LinearLayout) findViewById(R.id.parent);
                for(int i=0;i<timeline.length();i++){
                    try {
                        System.out.println("i="+i);
                        JSONObject j1 = new JSONObject(timeline.getString(i));
//                        System.out.println(j1);

                        String date_time = j1.getString("date_time");
                        String date = date_time.substring(0,9);
                        date=date.substring(0,2)+"."+date.substring(3,6).toUpperCase()+"."+date.substring(7);
                        String time = date_time.substring(10);
//                        System.out.println(time);

                        // ~~~~~~~~~~~~~~~``UI  CONSTANTS DEFINING ~~~~~~~~~~~~~~~~~~~~~~~~~
                        Typeface attenroundnewbold = ResourcesCompat.getFont(getApplicationContext(), R.font.attenroundnewbold);
                        Typeface attenroundnewbook = ResourcesCompat.getFont(getApplicationContext(), R.font.attenroundnewbook);

                        ImageView light_circle = new ImageView(getApplicationContext());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getpx(12), getpx(12));
                        lp.setMargins(getpx(6), 0, getpx(6), 0);
                        lp.gravity = Gravity.CENTER_VERTICAL;
                        light_circle.setLayoutParams(lp);
                        light_circle.setBackgroundResource(R.drawable.blue_circle);

                        ImageView first_line_light_circle = new ImageView(getApplicationContext());
                        LinearLayout.LayoutParams fllp = new LinearLayout.LayoutParams(getpx(12), getpx(12));
                        fllp.setMargins(getpx(9), getpx(3), getpx(6), 0);
                        first_line_light_circle.setLayoutParams(fllp);
                        first_line_light_circle.setBackgroundResource(R.drawable.blue_circle);

                        // ~~~~~~~~~~~~~~~``UI  CONSTANTS DEFINED ~~~~~~~~~~~~~~~~~~~~~~~~~

                        // ~~~~~~~~~~~~~~~~~~~~~~~`` start timeline box ~~~~~~~~~~~~~~~~~~~~~~~~~~~~

                        LinearLayout emptybox = new LinearLayout(getApplicationContext());
                        LinearLayout.LayoutParams ep = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                getpx(14)
                        );
                        emptybox.setLayoutParams(ep);
                        parent.addView(emptybox);

                        LinearLayout timeline_box = new LinearLayout(getApplicationContext());
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(0, getpx(14), 0, 0);
                        timeline_box.setLayoutParams(params);

//                        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) timeline_box.getLayoutParams();
//                        p.setMargins(0, 14, 0, 0);
//                        timeline_box.requestLayout();
//                        timeline_box.setLayoutParams(p);

                        timeline_box.setOrientation(LinearLayout.VERTICAL);
                        timeline_box.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_corners_teal_color));
                        timeline_box.setPadding(getpx(13), getpx(14), getpx(13), getpx(14));

                        LinearLayout inner_parent = new LinearLayout(getApplicationContext());
                        LinearLayout.LayoutParams inner_parent_params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        timeline_box.setLayoutParams(inner_parent_params);
                        timeline_box.addView(inner_parent);

                        // write first line of timeline
                        RelativeLayout first_child = new RelativeLayout(getApplicationContext());
                        RelativeLayout.LayoutParams fc_params = new RelativeLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        first_child.setLayoutParams(fc_params);

                        //first_child left part
                        LinearLayout fc_l = new LinearLayout(getApplicationContext());
                        LinearLayout.LayoutParams fc_l_params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        fc_l.setLayoutParams(fc_l_params);
                        fc_l.setOrientation(LinearLayout.HORIZONTAL);

                        TextView datetv = new TextView(getApplicationContext());
                        datetv.setLayoutParams(new LinearLayout.LayoutParams(143, LinearLayout.LayoutParams.WRAP_CONTENT));
                        datetv.setTextColor(Color.parseColor("#ffffff"));
                        datetv.setGravity(Gravity.CENTER_VERTICAL);
                        datetv.setTypeface(attenroundnewbold);
                        datetv.setTextSize(12);
                        datetv.setText(date);

                        fc_l.addView(datetv);
                        fc_l.addView(first_line_light_circle);

                        if(j1.getDouble("temperature")!=0){
                            // displaying the temperature line
                            System.out.println(j1.get("temperature").toString());

                            float temp = Float.parseFloat(j1.getString("temperature"));
                            Fever f = new Fever();
                            String fever = f.findfever(temp);
                            String temptext = temp+"°F "+fever;

                            TextView temptv = new TextView(getApplicationContext());
                            temptv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            temptv.setTextColor(Color.parseColor("#ffffff"));
                            temptv.setGravity(Gravity.CENTER_VERTICAL);
                            temptv.setTypeface(attenroundnewbook);
                            temptv.setTextSize(14);
                            temptv.setTypeface(temptv.getTypeface(), Typeface.BOLD);
                            temptv.setText(temptext);

                            fc_l.addView(temptv);
                        }
                        else {
                            String upperstring = "";
                            String lowerstring = "";
                            if (!j1.getString("symptoms_reported").equals("")) {
                                // displaying the symptoms line
                                System.out.println(j1.get("symptoms_reported"));
                                upperstring = j1.getString("symptoms_reported");
                                lowerstring = "Symptoms Reported";
                            } else {
                                // displaying the preexisting conditions line
                                System.out.println(j1.get("preexisting_conditions"));
                                upperstring = j1.getString("preexisting_conditions");
                                lowerstring = "Pre-existing Conditions";
                            }

                            LinearLayout nc_l_v = new LinearLayout(getApplicationContext());
                            LinearLayout.LayoutParams nc_l_v_params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            nc_l_v.setLayoutParams(nc_l_v_params);
                            nc_l_v.setOrientation(LinearLayout.VERTICAL);

                            TextView uppertv = new TextView(getApplicationContext());
                            uppertv.setLayoutParams(new LinearLayout.LayoutParams(365, LinearLayout.LayoutParams.WRAP_CONTENT));
                            uppertv.setTextColor(Color.parseColor("#ffffff"));
                            uppertv.setGravity(Gravity.CENTER_VERTICAL);
                            uppertv.setTypeface(attenroundnewbook);
                            uppertv.setTextSize(14);
                            uppertv.setText(upperstring);
                            uppertv.setTypeface(uppertv.getTypeface(), Typeface.BOLD);
                            nc_l_v.addView(uppertv);

                            TextView lowertv = new TextView(getApplicationContext());
                            lowertv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            lowertv.setTextColor(Color.parseColor("#ffffff"));
                            lowertv.setGravity(Gravity.CENTER_VERTICAL);
                            lowertv.setTypeface(attenroundnewbook);
                            lowertv.setTextSize(8);
                            lowertv.setText(lowerstring);
                            nc_l_v.addView(lowertv);

                            fc_l.addView(nc_l_v);
                        }

                        first_child.addView(fc_l);

                        TextView timetv = new TextView(getApplicationContext());
                        timetv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                        timetv.setTextColor(Color.parseColor("#ffffff"));
                        timetv.setGravity(Gravity.CENTER_VERTICAL);
                        timetv.setTypeface(attenroundnewbook);
                        timetv.setTextSize(14);
                        timetv.setTypeface(timetv.getTypeface(), Typeface.BOLD);
                        timetv.setText(time);

                        RelativeLayout.LayoutParams timetvparams = (RelativeLayout.LayoutParams)timetv.getLayoutParams();
                        timetvparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        timetv.setLayoutParams(timetvparams);

                        first_child.addView(timetv);

                        timeline_box.addView(first_child);

                        if(i!=timeline.length()-1){
                            //means if i is not at the last position
                            int color_counter=0;
                            for(int j=i+1;j<timeline.length();j++){
                                JSONObject j2 = new JSONObject(timeline.getString(j));
                                String date_time2 = j2.getString("date_time");
                                String date2 = date_time2.substring(0,9);
                                date2=date2.substring(0,2)+"."+date2.substring(3,6).toUpperCase()+"."+date2.substring(7);
                                String time2 = date_time2.substring(10);
                                i=j; //getting all readings of a day together
                                if(date2.equals(date)){
                                    //they're of the same date
                                    //include the data in the same timeline box

                                    RelativeLayout next_child = new RelativeLayout(getApplicationContext());
                                    RelativeLayout.LayoutParams nc_params = new RelativeLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    );
                                    next_child.setLayoutParams(nc_params);

                                    //next_child left part
                                    LinearLayout nc_l = new LinearLayout(getApplicationContext());
                                    LinearLayout.LayoutParams nc_l_params = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    );
//                                    nc_l_params.setMargins(getpx(47),getpx(9),0,0);
                                    nc_l_params.setMargins(getpx(54),getpx(9),0,0);
                                    nc_l.setLayoutParams(nc_l_params);
                                    nc_l.setOrientation(LinearLayout.HORIZONTAL);

                                    if(color_counter%2==0){

                                        ImageView white_circle = new ImageView(getApplicationContext());
                                        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(getpx(12), getpx(12));
                                        lp1.setMargins(getpx(6), 0, getpx(6), 0);
                                        lp1.gravity = Gravity.CENTER_VERTICAL;
                                        white_circle.setLayoutParams(lp1);
                                        white_circle.setBackgroundResource(R.drawable.white_circle);

                                        nc_l.addView(white_circle);
                                    }
                                    else{
                                        ImageView white_circle = new ImageView(getApplicationContext());
                                        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(getpx(12), getpx(12));
                                        lp1.setMargins(getpx(6), 0, getpx(6), 0);
                                        lp1.gravity = Gravity.CENTER_VERTICAL;
                                        white_circle.setLayoutParams(lp1);
                                        white_circle.setBackgroundResource(R.drawable.blue_circle);

                                        nc_l.addView(white_circle);
                                    }
                                    color_counter+=1;

                                    if(j2.getDouble("temperature")!=0){
                                        // displaying the temperature line
                                        System.out.println(j2.get("temperature").toString());

                                        float temp = Float.parseFloat(j2.getString("temperature"));
                                        Fever f = new Fever();
                                        String fever = f.findfever(temp);
                                        String temptext = temp+"°F "+fever;

                                        TextView temptv = new TextView(getApplicationContext());
                                        temptv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                        temptv.setTextColor(Color.parseColor("#ffffff"));
                                        temptv.setGravity(Gravity.CENTER_VERTICAL);
                                        temptv.setTypeface(attenroundnewbook);
                                        temptv.setTextSize(14);
                                        temptv.setTypeface(temptv.getTypeface(), Typeface.BOLD);
                                        temptv.setText(temptext);

                                        nc_l.addView(temptv);
                                    }
                                    else {
                                        String upperstring = "";
                                        String lowerstring = "";
                                        if(!j2.getString("symptoms_reported").equals("")){
                                            // displaying the symptoms line
                                            System.out.println(j2.get("symptoms_reported"));
                                            upperstring = j2.getString("symptoms_reported");
                                            lowerstring = "Symptoms Reported";
                                        }
                                        else{
                                            // displaying the preexisting conditions line
                                            System.out.println(j2.get("preexisting_conditions"));
                                            upperstring = j2.getString("preexisting_conditions");
                                            lowerstring = "Pre-existing Conditions";
                                        }

                                        LinearLayout nc_l_v = new LinearLayout(getApplicationContext());
                                        LinearLayout.LayoutParams nc_l_v_params = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT
                                        );
                                        nc_l_v.setLayoutParams(nc_l_v_params);
                                        nc_l_v.setOrientation(LinearLayout.VERTICAL);

                                        TextView uppertv = new TextView(getApplicationContext());
                                        uppertv.setLayoutParams(new LinearLayout.LayoutParams(365, LinearLayout.LayoutParams.WRAP_CONTENT));
                                        uppertv.setTextColor(Color.parseColor("#ffffff"));
                                        uppertv.setGravity(Gravity.CENTER_VERTICAL);
                                        uppertv.setTypeface(attenroundnewbook);
                                        uppertv.setTextSize(14);
                                        uppertv.setText(upperstring);
                                        uppertv.setTypeface(uppertv.getTypeface(), Typeface.BOLD);
                                        nc_l_v.addView(uppertv);

                                        TextView lowertv = new TextView(getApplicationContext());
                                        lowertv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                        lowertv.setTextColor(Color.parseColor("#ffffff"));
                                        lowertv.setGravity(Gravity.CENTER_VERTICAL);
                                        lowertv.setTypeface(attenroundnewbook);
                                        lowertv.setTextSize(8);
                                        lowertv.setText(lowerstring);
                                        nc_l_v.addView(lowertv);

                                        nc_l.addView(nc_l_v);
                                    }

                                    next_child.addView(nc_l);

                                    TextView nctimetv = new TextView(getApplicationContext());
                                    nctimetv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                                    nctimetv.setTextColor(Color.parseColor("#ffffff"));
                                    nctimetv.setGravity(Gravity.CENTER_VERTICAL);
                                    nctimetv.setTypeface(attenroundnewbook);
                                    nctimetv.setTextSize(14);
                                    nctimetv.setTypeface(nctimetv.getTypeface(), Typeface.BOLD);
                                    nctimetv.setText(time2);

                                    RelativeLayout.LayoutParams nctimetvparams = (RelativeLayout.LayoutParams)nctimetv.getLayoutParams();
                                    nctimetvparams.setMargins(0,getpx(9),0,0);
                                    nctimetvparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                    nctimetv.setLayoutParams(nctimetvparams);

                                    next_child.addView(nctimetv);

                                    timeline_box.addView(next_child);
                                }
                                else{
                                    break;
                                }
                            }
                        }

                        //end timeline box
                        parent.addView(timeline_box);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
            else{
                LinearLayout parent = (LinearLayout) findViewById(R.id.parent);

                TextView errortexttv = new TextView(getApplicationContext());
                errortexttv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                errortexttv.setTextColor(getResources().getColor(R.color.colorPrimary));
                errortexttv.setGravity(Gravity.CENTER_VERTICAL);
                errortexttv.setTextSize(12);
                errortexttv.setText("No data");

                parent.addView(errortexttv);
            }
        }

        public int getpx(int dp){
            Resources r = getApplicationContext().getResources();
            int px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dp,
                    r.getDisplayMetrics()
            );
            return px;
        }
    }
}
