package com.example.trafficroadworks.sharedpreferencefornearme;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedNearMePreference {

    //this is universal shared preferences class

    //declaring context of the activity
    private Context context;

    //declaring shared preference variable
    private SharedPreferences shareNearMePreference;

    //declaring shared preferences editor
    private SharedPreferences.Editor edit;

    //initializing all variable
    public SharedNearMePreference(Context context){
        this.context = context;
        shareNearMePreference = context.getSharedPreferences("com", Context.MODE_PRIVATE);
    }

    //setting near me value
    public void setNearMeDialogValue(int val){
        edit = shareNearMePreference.edit();
        edit.putInt("NEAR_ME", val);
        edit.apply();
    }

    //getting near me value
    public int getNearMeDialogValue(){
        return shareNearMePreference.getInt("NEAR_ME", 0);
    }
}
