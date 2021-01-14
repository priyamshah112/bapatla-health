package com.droiduino.companionappcourse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ManageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageFragment newInstance(String param1, String param2) {
        ManageFragment fragment = new ManageFragment();
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
        View view = inflater.inflate(R.layout.fragment_manage, container, false);

        Session session;//global variable
        session = new Session(getActivity().getApplicationContext());
        String name = session.getname();
        String id = session.getid();

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

        LinearLayout profileButton = (LinearLayout)view.findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), Profile.class);
                getActivity().startActivity(myIntent);
            }
        });

        LinearLayout doctorButton = (LinearLayout)view.findViewById(R.id.doctorButton);
        doctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), Doctor.class);
                getActivity().startActivity(myIntent);
            }
        });

        LinearLayout familyButton = (LinearLayout)view.findViewById(R.id.familyButton);
        familyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), Family.class);
                getActivity().startActivity(myIntent);
            }
        });

        LinearLayout prescriptionButton = (LinearLayout)view.findViewById(R.id.prescriptionButton);
        prescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), Prescription.class);
                getActivity().startActivity(myIntent);
            }
        });

        return view;
    }
}