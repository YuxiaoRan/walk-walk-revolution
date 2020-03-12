package com.example.cse110_wwr_team2.User;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String device_ID;
    private String gmail;
    private String name;
    private int height;
    private String teamID;
    private String lastWalkID;

    public User(){}

    public User(String id, String gmail, String name, int height, String deviceID) {
        this.id = id;
        this.gmail = gmail;
        this.name = name;
        this.height = height;
        this.device_ID = deviceID;
        this.teamID = teamID;
    }

    public User(String gmail, String name, int height, String teamID, String deviceID){
        this.gmail = gmail;
        this.name = name;
        this.height = height;
        this.teamID = teamID;
        this.device_ID = deviceID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getTeamID() {
        return teamID;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public static String getInitial(String name){
        return ""+name.charAt(0);
    }

    public String getDeviceID() {
        return this.device_ID;
    }
}
