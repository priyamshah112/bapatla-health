package com.droiduino.companionappcourse;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
        ArrayList<String> allusers = tinydb.getListString("allusers");
        System.out.println("ALLUSERSS"+allusers);

        final TextView homeFragmentUsername = (TextView)view.findViewById(R.id.homeFragmentUsername);
        homeFragmentUsername.setText(name);

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

        return view;
    }
}