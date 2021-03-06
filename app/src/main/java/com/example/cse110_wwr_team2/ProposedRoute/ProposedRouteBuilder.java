package com.example.cse110_wwr_team2.ProposedRoute;

import android.content.Context;

import com.example.cse110_wwr_team2.Team.TeamAdapter;
import com.example.cse110_wwr_team2.firebasefirestore.MapCallBack;

import java.util.Map;

public class ProposedRouteBuilder {
    String id;
    String teamID;
    String startPoint;
    String name;
    String dataTime;
    String proposerID;
    Map<String, Integer> acceptMembers;
    int scheduled;
    String user = "";
    String message = "";

    public ProposedRouteBuilder(){}

    public ProposedRouteBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public ProposedRouteBuilder setTeamId(String teamID) {
        this.teamID = teamID;
        return this;
    }

    public ProposedRouteBuilder setStartPoint(String startPoint) {
        this.startPoint = startPoint;
        return this;
    }

    public ProposedRouteBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ProposedRouteBuilder setDataTime(String dataTime) {
        this.dataTime = dataTime;
        return this;
    }

    public ProposedRouteBuilder setProposerID(String proposerID) {
        this.proposerID = proposerID;
        return this;
    }

    public ProposedRouteBuilder setAcceptMembers(Map<String, Integer> acceptMembers) {
        this.acceptMembers = acceptMembers;
        return this;
    }


    public ProposedRouteBuilder setScheduled(int scheduled){
        this.scheduled = scheduled;
        return this;
    }

    public ProposedRouteBuilder setMessage(String user, String message){
        this.user = user;
        this.message = message;
        return this;
    }


    public ProposedRoute getRoute(){
        ProposedRoute a = new ProposedRoute(id, teamID, startPoint, name, dataTime, proposerID, acceptMembers, scheduled);
        a.setUpdatedMessage(message);
        a.setUpdatedUser(user);
        return a;
    }
}


