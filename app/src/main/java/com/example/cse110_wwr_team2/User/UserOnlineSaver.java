package com.example.cse110_wwr_team2.User;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.cse110_wwr_team2.firebasefirestore.LastWalkIDCallback;
import com.example.cse110_wwr_team2.firebasefirestore.UserCallBack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;



import javax.security.auth.callback.Callback;

import androidx.annotation.NonNull;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;



import javax.security.auth.callback.Callback;

import androidx.annotation.NonNull;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class UserOnlineSaver {
    private User user;
    private FirebaseFirestore db;

    public UserOnlineSaver() {
        this.db = FirebaseFirestore.getInstance();
    }

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

    public void getLastestWalkID(String userID, LastWalkIDCallback callback){
        db.collection("Users").document(userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            String lastWalkID = (String)task.getResult().get("lastWalkID");
                            callback.onCallback(lastWalkID);
                        }else{
                            Log.d(TAG, "Failure at attaining the latest walk id");
                        }
                    }
                });
    }


    public void updateLatestWalk(String userID, String routeID, UserCallBack callBack){
        db.collection("Users").document(userID)
                .update("lastWalkID", routeID)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    callBack.onCallBack();
                }
                else{
                    Log.d(TAG, "Updating latest route failed");
                }
            }
        });
    }



}
