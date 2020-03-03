package com.example.cse110_wwr_team2.firebasefirestore;

import com.example.cse110_wwr_team2.Route.Route;
import com.example.cse110_wwr_team2.User.User;

import java.util.List;

public interface RouteCallback {
    void onCallback(List<Route> routes);
}
