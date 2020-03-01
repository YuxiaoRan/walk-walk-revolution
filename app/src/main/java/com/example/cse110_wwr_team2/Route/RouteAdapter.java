package com.example.cse110_wwr_team2.Route;

import com.example.cse110_wwr_team2.firebasefirestore.FireBaseFireStoreService;
import com.google.firebase.firestore.FirebaseFirestore;

public class RouteAdapter implements FireBaseFireStoreService {
    private Route route;
    private FirebaseFirestore db;

    public RouteAdapter(Route route){
        this.route = route;
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public void write() {
        db.collection("Routes").document(route.getId()).set(route);
    }

    @Override
    public void read(String id) {
        // Need different implementation because getting data from firebase is an async task
    }
}
