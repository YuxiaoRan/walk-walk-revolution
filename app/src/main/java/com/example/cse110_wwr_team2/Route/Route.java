package com.example.cse110_wwr_team2.Route;

import com.example.cse110_wwr_team2.RandomIDGenerator.UUIDGenerator;

import java.io.Serializable;
import java.util.UUID;

/*
 * This is the simplest class for a route
 *      String start_point: the string that stores the start point of the route
 *      String name: the string that saves the name of the route
 *      int step_cnt: the step count of this route
 */
public class Route implements Serializable {
    private float distance;
    private String features;
    private String id;
    private String name;
    private String note;
    private String startPoint;
    private int stepCnt;
    private String userTeamID;
    private String userId;
    private String userInitial;
    private String startTime;
    private Boolean isFavourite;

    public Route(){}

    public Route(String start_point, String name, int step_cnt, String note, String features, float distance, String userId){
        if(this.id == null){
            // Ramdonly generating a route id
            this.id = UUIDGenerator.uuidHexToUuid64(UUID.randomUUID().toString());
        }
        this.startPoint = start_point;
        this.name = name;
        this.stepCnt = step_cnt;
        this.note = note;
        this.features = features;
        this.distance = distance;
        this.userId = userId;
    }

    public Route(String start_point, String name, String note, int step_cnt, float distance, String features, String userTeamID) {
        if(this.id == null){
            // Ramdonly generating a route id
            this.id = UUIDGenerator.uuidHexToUuid64(UUID.randomUUID().toString());
        }
        this.startPoint = start_point;
        this.name = name;
        this.note = note;
        this.stepCnt = step_cnt;
        this.distance = distance;
        this.features = features;
        this.userTeamID = userTeamID;
    }

    public String getId() {
        return id;
    }

    public String getUserTeamID() {
        return userTeamID;
    }

    public void setUserTeamID(String userTeamID) {
        this.userTeamID = userTeamID;
    }

    public String getUserID() {
        return userId;
    }

    public void setUserID(String userID) {
        this.userId = userID;
    }

    public double getDistance(){
        return distance;
    }

    public void setDistance(float distance){
        this.distance = distance;
    }

    public Route(String start_point, String name, int step_cnt, String note, String features){
        this.startPoint = start_point;
        this.name = name;
        this.stepCnt = step_cnt;
        this.note = note;
        this.features = features;
    }

    public String getFeatures(){return this.features;}

    public String getNote(){return this.note;}

    public void updateNote(String newNote) {this.note = newNote;}

    public String getStartPoint(){
        return this.startPoint;
    }

    public String getName(){
        return this.name;
    }

    public int getStepCnt(){
        return this.stepCnt;
    }

    public void updateStep(int new_step_cnt){
        this.stepCnt = new_step_cnt;
    }

    public void updateDistance(float distance){
        this.distance = distance;
    }

    public String toString(){
        return "Route Name: " + this.name + "\nStart point: " + this.startPoint + "\nStep Count: " + this.stepCnt;
    }

    public void setUserInitial(String userInitial) {
        this.userInitial = userInitial;
    }

    public String getUserInitial() {
        return userInitial;
    }

    public String[] toList(){
        return new String[]{this.name, this.startPoint, Integer.toString(this.stepCnt), Double.toString(distance)};
    }

    public void setStepCnt(int stepCnt) {
        this.stepCnt = stepCnt;
    }
}
