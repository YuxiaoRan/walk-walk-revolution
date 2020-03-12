package com.example.cse110_wwr_team2.firebasefirestore;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public interface MapCallBack {
    void onCallback(Map<String, Integer> members, FirebaseFirestore db);
}
