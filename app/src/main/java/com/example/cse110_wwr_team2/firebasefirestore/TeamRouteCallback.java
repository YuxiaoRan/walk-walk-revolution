package com.example.cse110_wwr_team2.firebasefirestore;

import com.example.cse110_wwr_team2.Route.Route;

import java.util.List;

public interface TeamRouteCallback {
    public void onCallback(List<Route> routes, List<String> routes_info);
}
