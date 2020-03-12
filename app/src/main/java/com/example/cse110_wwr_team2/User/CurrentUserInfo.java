package com.example.cse110_wwr_team2.User;

import android.content.Context;
import android.content.SharedPreferences;

public class CurrentUserInfo {

    public static String getId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        return sharedPreferences.getString("id", null);
//        return "8B7qKDUGQubzG0lajb16F5gML233";
    }

    public static String getTeamId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        return sharedPreferences.getString("teamID", null);
//        return "1";
    }

}
