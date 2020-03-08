package com.example.cse110_wwr_team2.User;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import static android.content.Context.MODE_PRIVATE;

public class UserOnlineSaver {
    private User user;
    private FirebaseFirestore db;

    public UserOnlineSaver(User user){
        this.user = user;
        this.db = FirebaseFirestore.getInstance();
    }

    public void write() {
        db.collection("Users").document(user.getId()).set(user);
    }


    public static void saveLocalUserInfo(FirebaseUser user, String deviceID, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Log.d(TAG, "saveUserInfo: "+user.getUid());
        editor.putString("id", user.getUid());
        editor.putString("gmail", user.getEmail());
        editor.putString("name", user.getDisplayName());
        editor.putString("device_ID", deviceID);
        editor.apply();
    }


}
