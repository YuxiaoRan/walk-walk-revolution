package com.example.cse110_wwr_team2.firebasefirestore;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class WalkedRouteAdapter {
    private FirebaseFirestore db;
    private final String COLLECTION_KEY = "Users";
    private final String COLLECTION_PATH = "Walked";

    public WalkedRouteAdapter(){
        db = FirebaseFirestore.getInstance();
    }

    public void addToWalked(String userID, String routeID){
        Map<String, Object> map = new HashMap<>();
        map.put(routeID,null);
        db.collection(COLLECTION_KEY).document(userID).collection(COLLECTION_PATH).document(routeID).set(map);
    }


}

