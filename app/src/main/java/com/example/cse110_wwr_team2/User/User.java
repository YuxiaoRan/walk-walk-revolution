package com.example.cse110_wwr_team2.User;

public class User {
    private String id;
    private String gmail;
    private String name;
    private int height;
    private String teamID;
    private String lastWalkID;

    public User(){}

    public User(String id, String gmail, String name, int height) {
        this.id = id;
        this.gmail = gmail;
        this.name = name;
        this.height = height;
    }

    public User(String gmail, String name, int height, String teamID){
        this.gmail = gmail;
        this.name = name;
        this.height = height;
        this.teamID = teamID;
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
}
