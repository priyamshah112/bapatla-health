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

    // the current user (could be a family member)
    public void setid(String id) {
        prefs.edit().putString("id", id).commit();
    }

    public String getid() {
        String id = prefs.getString("id","");
        return id;
    }

    // the person who logs into the app (CANNOT be a family member)
    public void setPrimaryId(String id) {
        prefs.edit().putString("primaryUserId", id).commit();
    }

    public String getPrimaryId() {
        String id = prefs.getString("primaryUserId","");
        return id;
    }

    public void setname(String name) {
        prefs.edit().putString("name", name).commit();
    }

    public String getname() {
        String name = prefs.getString("name","");
        return name;
    }

    public void settemperature(float temperature) {
        prefs.edit().putFloat("temperature", temperature).commit();
    }

    public float gettemperature() {
        float temperature = prefs.getFloat("temperature", 0.0f);
        return temperature;
    }

    public void setfever(String fever){
        prefs.edit().putString("fever", fever).commit();
    }

    public String getfever() {
        String fever = prefs.getString("fever","");
        return fever;
    }

    public void setSymptomsFlag(){
        prefs.edit().putString("symptoms", "set").commit();
    }

    public String getSymptomsFlag() {
        String symptoms = prefs.getString("symptoms","");
        return symptoms;
    }

    public void setConditionsFlag(){
        prefs.edit().putString("conditions", "set").commit();
    }

    public String getConditionsFlag() {
        String conditions = prefs.getString("conditions","");
        return conditions;
    }

    public void setVitalsCovid19Flag(){
        prefs.edit().putString("vitals_covid", "set").commit();
    }

    public String getVitalsCovid19Flag() {
        String vitals_covid = prefs.getString("vitals_covid","");
        return vitals_covid;
    }

    public void destroyFlags(){
        prefs.edit().putString("symptoms", "").commit();
        prefs.edit().putString("conditions", "").commit();
        prefs.edit().putString("vitals_covid", "").commit();
    }

}