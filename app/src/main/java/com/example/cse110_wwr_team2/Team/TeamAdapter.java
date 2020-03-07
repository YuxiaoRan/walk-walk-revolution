package com.example.cse110_wwr_team2.Team;


import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cse110_wwr_team2.User.User;
import com.example.cse110_wwr_team2.firebasefirestore.MapCallBack;
import com.example.cse110_wwr_team2.firebasefirestore.RouteCallback;
import com.example.cse110_wwr_team2.firebasefirestore.TeammateCallBack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
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

    private FirebaseFirestore db;

    public TeamAdapter(){
        db = FirebaseFirestore.getInstance();
    }

    public void getTeammatesNames(TeammateCallBack callback){

        String teamId = "HCteamID"; //TODO: change to current user's teamID

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
    }

    public void getAllMap(MapCallBack callback, String teamID){
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
                                acceptMembers.put(user.getId(), 0);
                            }
                            callback.onCallback(acceptMembers);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
