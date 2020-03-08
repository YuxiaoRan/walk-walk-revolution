package com.example.cse110_wwr_team2.ProposedRoute;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class ProposedRoute implements Serializable {
    String id;
    String teamID;
    String startPoint;
    String name;
    String dataTime;
    String proposerID;
    Map<String, Integer> acceptMembers;
    int scheduled;

    public ProposedRoute(String id, String teamID, String startPoint, String name, String dataTime, String proposerID, Map<String, Integer> map, int scheduled){
        this.id = id;
        this.teamID = teamID;
        this.startPoint = startPoint;
        this.name = name;
        this.dataTime = dataTime;
        this.proposerID = proposerID;
        this.acceptMembers = map;
        this.scheduled = scheduled;
    }

    public ProposedRoute(){}

    public int getScheduled() {
        return scheduled;
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

    @Override
    public String toString() {
        return  name + " " + dataTime;
    }

    public void setAcceptMembers(Map<String, Integer> acceptMembers) {
        this.acceptMembers = acceptMembers;
    }

    public void setScheduled(int scheduled) {
        this.scheduled = scheduled;
    }
}
