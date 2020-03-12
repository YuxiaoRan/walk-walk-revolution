package com.example.cse110_wwr_team2.firebasefirestore;

import com.example.cse110_wwr_team2.Route.Route;

import java.util.List;

public interface TeammateCallBack {
    void onCallback(List<String> userNames);
    void onCallbackPending(List<String> userNames);
}
