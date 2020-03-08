package com.example.cse110_wwr_team2.ProposedRoute;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class ProposedRoute {
    String id;
    String teamID;
    String startPoint;
    String name;
    String dataTime;
    String proposerID;
    Map<String, Integer> acceptMembers;

    public ProposedRoute(String id, String teamID, String startPoint, String name, String dataTime, String proposerID, Map<String, Integer> map){
        this.id = id;
        this.teamID = teamID;
        this.startPoint = startPoint;
        this.name = name;
        this.dataTime = dataTime;
        this.proposerID = proposerID;
        this.acceptMembers = map;
    }

    public String getId() {
        return id;
    }

    public String getTeamID() {
        return teamID;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public String getName() {
        return name;
    }

    public String getDataTime() {
        return dataTime;
    }

    public String getProposerID() {
        return proposerID;
    }

    public Map<String, Integer> getAcceptMembers() {
        return acceptMembers;
    }


}
