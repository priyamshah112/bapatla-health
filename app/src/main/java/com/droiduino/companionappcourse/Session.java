package com.droiduino.companionappcourse;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setid(String id) {
        prefs.edit().putString("id", id).commit();
    }

    public String getid() {
        String id = prefs.getString("id","");
        return id;
    }

    public void setname(String name) {
        prefs.edit().putString("name", name).commit();
    }

    public String getname() {
        String name = prefs.getString("name","");
        return name;
    }

}