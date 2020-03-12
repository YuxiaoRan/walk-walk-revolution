package com.example.cse110_wwr_team2.Team;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cse110_wwr_team2.User.CurrentUserInfo;
import com.example.cse110_wwr_team2.User.User;
import com.example.cse110_wwr_team2.firebasefirestore.GetCallBack;
import com.example.cse110_wwr_team2.firebasefirestore.MapCallBack;
import com.example.cse110_wwr_team2.firebasefirestore.RouteCallback;
import com.example.cse110_wwr_team2.firebasefirestore.TeammateCallBack;
import com.example.cse110_wwr_team2.firebasefirestore.UserCallBack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * This class get the desired team info from firebase
 */
public class TeamAdapter {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Context context;

    public TeamAdapter(){}

    public TeamAdapter(Context context){
        this.context = context;
    }

    public void getTeammatesNames(TeammateCallBack callback, Context context){

//        String teamId = "HCteamID";
        //String teamId = "1";
        String teamId = CurrentUserInfo.getTeamId(context);
        Log.d(TAG, "getTeammatesNames: "+teamId);
        db.collection("Users")
                .whereEqualTo("teamID", teamId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> teammates = new ArrayList<String>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                User user = document.toObject(User.class);
                                if(!user.getId().equals(CurrentUserInfo.getId(context))) {
                                    teammates.add(user.getName());
                                }
                            }
                            callback.onCallback(teammates);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void getTeammatesNames(GetCallBack callback, String userID){
        db.collection("Users").whereEqualTo("id", userID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            String username = "";
                            for (QueryDocumentSnapshot document: task.getResult()){
                                username = document.toObject(User.class).getName();
                            }
                            callback.onCallBack(username);
                        }
                    }
                });
    }

    public void getTeammates(UserCallBack callback, Context context){
//        String teamId = "HCteamID"; //TODO: change to current user's teamID

        String teamId = CurrentUserInfo.getTeamId(context);
        db.collection("Users")
                .whereEqualTo("teamID", teamId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<User> teammates = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                User user = document.toObject(User.class);
                                teammates.add(user);
                            }
                            callback.onCallback(teammates);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void getAllMap(MapCallBack callback, String teamID, String proposerID){
        db.collection("Users")
                .whereEqualTo("teamID", teamID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Map<String, Integer> acceptMembers = new HashMap<>();
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                User user = document.toObject(User.class);
                                if(!user.getId().equals(proposerID)) {
                                    acceptMembers.put(user.getId(), 0);
                                }
                            }
                            callback.onCallback(acceptMembers, FirebaseFirestore.getInstance());
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public void getTeammatesNames(TeammateCallBack callback){

        String teamId = CurrentUserInfo.getTeamId(context);
        Log.d(TAG, "getTeammatesNames: "+teamId);

        db.collection("Users")
                .whereEqualTo("teamID", teamId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> teammates = new ArrayList<String>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                User user = document.toObject(User.class);
                                teammates.add(user.getName());
                            }
                            callback.onCallback(teammates);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        // Get pending invitations
        db.collection("Invitations")
                .whereEqualTo("status", 0)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> teammates = new ArrayList<String>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.get("NameTo").toString();
                                teammates.add(name);

                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                //User user = document.toObject(User.class);
                            }
                            callback.onCallbackPending(teammates);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
}
