package com.droiduino.companionappcourse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Session session;//global variable
        session = new Session(getActivity().getApplicationContext());
        String name = session.getname();
        String id = session.getid();
        String fever = session.getfever();
        float temperature = session.gettemperature();

        TinyDB tinydb = new TinyDB(getActivity().getApplicationContext());
        final ArrayList<String> allusers = tinydb.getListString("allusers");
        System.out.println("ALLUSERSS"+allusers);

        final TextView homeFragmentUsername = (TextView)view.findViewById(R.id.homeFragmentUsername);
        homeFragmentUsername.setText(name);
        homeFragmentUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String allusernames[] = new String[allusers.size()/2];
                final String alluserids[] = new String[allusers.size()/2];
                int counter=0;
                int id_counter=0;
                for(int i=0;i<allusers.size();i++){
                    if(i%2!=0){
                        allusernames[counter]=allusers.get(i);
                        counter+=1;
                    }else{
                        alluserids[id_counter]=allusers.get(i);
                        id_counter+=1;
                    }
                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose user");
                builder.setItems(allusernames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on allusernames[which]
                        Session session;//global variable
                        session = new Session(getActivity());
                        session.setname(allusernames[which]);
                        session.setid(alluserids[which]);

                        session.settemperature(96); //setting it to default 96F
                        Fever f = new Fever();
                        String fever = f.findfever(96);
                        session.setfever(fever);

                        dialog.dismiss();

                        // reloading activity
                        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.container);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.detach(currentFragment);
                        fragmentTransaction.attach(currentFragment);
                        fragmentTransaction.commit();
                    }
                });
                builder.show();
            }
        });

        final TextView home_fever_text = (TextView)view.findViewById(R.id.home_fever_text);
        home_fever_text.setText(fever);

        final TextView home_temperature = (TextView)view.findViewById(R.id.home_temperature);
        home_temperature.setText(Float.toString(temperature));

        LinearLayout timelineButton = (LinearLayout)view.findViewById(R.id.timelineButton);
        timelineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), Timeline.class);
                getActivity().startActivity(myIntent);
            }
        });

        LinearLayout healthGuidanceButton = (LinearLayout)view.findViewById(R.id.healthGuidanceButton);
        healthGuidanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), HealthGuidance.class);
                getActivity().startActivity(myIntent);
            }
        });

        LinearLayout dailyTrendButton = (LinearLayout)view.findViewById(R.id.dailyTrendButton);
        dailyTrendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), DailyTrend.class);
                getActivity().startActivity(myIntent);
            }
        });



        String payload = "{\"userId\": \""+id+"\"}";
        new HomeFragment.PostData().execute(payload);

        return view;
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
                LinearLayout parent = (LinearLayout) getActivity().findViewById(R.id.parent);
                for(int i=0;i<timeline.length();i++){
                    try {
                        System.out.println("i="+i);
                        JSONObject j1 = new JSONObject(timeline.getString(i));
//                        System.out.println(j1);

                        String date_time = j1.getString("date_time");
                        String date = date_time.substring(0,9);
                        String time = date_time.substring(10);
//                        System.out.println(time);

                        // ~~~~~~~~~~~~~~~``UI  CONSTANTS DEFINING ~~~~~~~~~~~~~~~~~~~~~~~~~
                        Typeface attenroundnewbold = ResourcesCompat.getFont(getActivity().getApplicationContext(), R.font.attenroundnewbold);
                        Typeface attenroundnewbook = ResourcesCompat.getFont(getActivity().getApplicationContext(), R.font.attenroundnewbook);

                        ImageView light_circle = new ImageView(getActivity().getApplicationContext());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getpx(12), getpx(12));
                        lp.setMargins(getpx(6), 0, getpx(6), 0);
                        lp.gravity = Gravity.CENTER_VERTICAL;
                        light_circle.setLayoutParams(lp);
                        light_circle.setBackgroundResource(R.drawable.blue_circle);

                        // ~~~~~~~~~~~~~~~``UI  CONSTANTS DEFINED ~~~~~~~~~~~~~~~~~~~~~~~~~

                        // ~~~~~~~~~~~~~~~~~~~~~~~`` start timeline box ~~~~~~~~~~~~~~~~~~~~~~~~~~~~

                        LinearLayout emptybox = new LinearLayout(getActivity().getApplicationContext());
                        LinearLayout.LayoutParams ep = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                getpx(14)
                        );
                        emptybox.setLayoutParams(ep);
                        parent.addView(emptybox);

                        LinearLayout timeline_box = new LinearLayout(getActivity().getApplicationContext());
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(0, 0, 0, 0);
                        timeline_box.setLayoutParams(params);

//                        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) timeline_box.getLayoutParams();
//                        p.setMargins(0, 14, 0, 0);
//                        timeline_box.requestLayout();
//                        timeline_box.setLayoutParams(p);

                        timeline_box.setOrientation(LinearLayout.VERTICAL);
                        timeline_box.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.rounded_corners_teal_color));
//                        timeline_box.setPadding(getpx(13), getpx(14), getpx(13), getpx(14));

                        LinearLayout inner_parent = new LinearLayout(getActivity().getApplicationContext());
                        LinearLayout.LayoutParams inner_parent_params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        timeline_box.setLayoutParams(inner_parent_params);
                        timeline_box.addView(inner_parent);

                        // write first line of timeline
                        RelativeLayout first_child = new RelativeLayout(getActivity().getApplicationContext());
                        RelativeLayout.LayoutParams fc_params = new RelativeLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        first_child.setLayoutParams(fc_params);

                        //first_child left part
                        LinearLayout fc_l = new LinearLayout(getActivity().getApplicationContext());
                        LinearLayout.LayoutParams fc_l_params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        fc_l.setLayoutParams(fc_l_params);
                        fc_l.setOrientation(LinearLayout.HORIZONTAL);

                        TextView datetv = new TextView(getActivity().getApplicationContext());
                        datetv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        datetv.setTextColor(Color.parseColor("#ffffff"));
                        datetv.setGravity(Gravity.CENTER_VERTICAL);
                        datetv.setTypeface(attenroundnewbold);
                        datetv.setTextSize(12);
                        datetv.setText(date);

                        fc_l.addView(datetv);
                        fc_l.addView(light_circle);

                        if(j1.getDouble("temperature")!=0){
                            // displaying the temperature line
                            System.out.println(j1.get("temperature").toString());

                            float temp = Float.parseFloat(j1.getString("temperature"));
                            Fever f = new Fever();
                            String fever = f.findfever(temp);
                            String temptext = temp+"°F "+fever;

                            TextView temptv = new TextView(getActivity().getApplicationContext());
                            temptv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            temptv.setTextColor(Color.parseColor("#ffffff"));
                            temptv.setGravity(Gravity.CENTER_VERTICAL);
                            temptv.setTypeface(attenroundnewbook);
                            temptv.setTextSize(14);
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

                            LinearLayout nc_l_v = new LinearLayout(getActivity().getApplicationContext());
                            LinearLayout.LayoutParams nc_l_v_params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            nc_l_v.setLayoutParams(nc_l_v_params);
                            nc_l_v.setOrientation(LinearLayout.VERTICAL);

                            TextView uppertv = new TextView(getActivity().getApplicationContext());
                            uppertv.setLayoutParams(new LinearLayout.LayoutParams(200, LinearLayout.LayoutParams.WRAP_CONTENT));
                            uppertv.setTextColor(Color.parseColor("#ffffff"));
                            uppertv.setGravity(Gravity.CENTER_VERTICAL);
                            uppertv.setTypeface(attenroundnewbook);
                            uppertv.setTextSize(14);
                            uppertv.setText(upperstring);
                            nc_l_v.addView(uppertv);

                            TextView lowertv = new TextView(getActivity().getApplicationContext());
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

                        TextView timetv = new TextView(getActivity().getApplicationContext());
                        timetv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                        timetv.setTextColor(Color.parseColor("#ffffff"));
                        timetv.setGravity(Gravity.CENTER_VERTICAL);
                        timetv.setTypeface(attenroundnewbook);
                        timetv.setTextSize(14);
                        timetv.setText(time);

                        RelativeLayout.LayoutParams timetvparams = (RelativeLayout.LayoutParams)timetv.getLayoutParams();
                        timetvparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        timetv.setLayoutParams(timetvparams);

                        first_child.addView(timetv);

                        timeline_box.addView(first_child);

                        if(i!=timeline.length()-1){
                            //means if i is not at the last position
                            int color_counter=0;
                            int entries = timeline.length()>3?3:timeline.length();
                            for(int j=i+1;j<entries;j++){
                                JSONObject j2 = new JSONObject(timeline.getString(j));
                                String date_time2 = j2.getString("date_time");
                                String date2 = date_time2.substring(0,9);
                                String time2 = date_time2.substring(10);
                                i=j; //getting all readings of a day together
                                if(date2.equals(date)){
                                    //they're of the same date
                                    //include the data in the same timeline box

                                    RelativeLayout next_child = new RelativeLayout(getActivity().getApplicationContext());
                                    RelativeLayout.LayoutParams nc_params = new RelativeLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    );
                                    next_child.setLayoutParams(nc_params);

                                    //next_child left part
                                    LinearLayout nc_l = new LinearLayout(getActivity().getApplicationContext());
                                    LinearLayout.LayoutParams nc_l_params = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    );
                                    nc_l_params.setMargins(getpx(47),getpx(9),0,0);
                                    nc_l.setLayoutParams(nc_l_params);
                                    nc_l.setOrientation(LinearLayout.HORIZONTAL);

                                    if(color_counter%2==0){

                                        ImageView white_circle = new ImageView(getActivity().getApplicationContext());
                                        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(getpx(12), getpx(12));
                                        lp1.setMargins(getpx(6), 0, getpx(6), 0);
                                        lp1.gravity = Gravity.CENTER_VERTICAL;
                                        white_circle.setLayoutParams(lp1);
                                        white_circle.setBackgroundResource(R.drawable.white_circle);

                                        nc_l.addView(white_circle);
                                    }
                                    else{
                                        ImageView white_circle = new ImageView(getActivity().getApplicationContext());
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

                                        TextView temptv = new TextView(getActivity().getApplicationContext());
                                        temptv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                        temptv.setTextColor(Color.parseColor("#ffffff"));
                                        temptv.setGravity(Gravity.CENTER_VERTICAL);
                                        temptv.setTypeface(attenroundnewbook);
                                        temptv.setTextSize(14);
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

                                        LinearLayout nc_l_v = new LinearLayout(getActivity().getApplicationContext());
                                        LinearLayout.LayoutParams nc_l_v_params = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT
                                        );
                                        nc_l_v.setLayoutParams(nc_l_v_params);
                                        nc_l_v.setOrientation(LinearLayout.VERTICAL);

                                        TextView uppertv = new TextView(getActivity().getApplicationContext());
                                        uppertv.setLayoutParams(new LinearLayout.LayoutParams(200, LinearLayout.LayoutParams.WRAP_CONTENT));
                                        uppertv.setTextColor(Color.parseColor("#ffffff"));
                                        uppertv.setGravity(Gravity.CENTER_VERTICAL);
                                        uppertv.setTypeface(attenroundnewbook);
                                        uppertv.setTextSize(14);
                                        uppertv.setText(upperstring);
                                        nc_l_v.addView(uppertv);

                                        TextView lowertv = new TextView(getActivity().getApplicationContext());
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

                                    TextView nctimetv = new TextView(getActivity().getApplicationContext());
                                    nctimetv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                                    nctimetv.setTextColor(Color.parseColor("#ffffff"));
                                    nctimetv.setGravity(Gravity.CENTER_VERTICAL);
                                    nctimetv.setTypeface(attenroundnewbook);
                                    nctimetv.setTextSize(14);
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
                    break;
                }

            }
            else{
                LinearLayout parent = (LinearLayout) getActivity().findViewById(R.id.parent);

                TextView errortexttv = new TextView(getActivity().getApplicationContext());
                errortexttv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                errortexttv.setTextColor(Color.parseColor("#ffffff"));
                errortexttv.setGravity(Gravity.CENTER_VERTICAL);
                errortexttv.setTextSize(12);
                errortexttv.setText("No data");

                parent.addView(errortexttv);

            }
        }

        public int getpx(int dp){
            Resources r = getActivity().getApplicationContext().getResources();
            int px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dp,
                    r.getDisplayMetrics()
            );
            return px;
        }
    }
}