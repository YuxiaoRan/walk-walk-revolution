package com.example.cse110_wwr_team2.firebasefirestore;

import com.example.cse110_wwr_team2.ProposedRoute.ProposedRoute;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public interface ProposedRouteCallback {
    void onCallback(List<ProposedRoute> routes);
}
