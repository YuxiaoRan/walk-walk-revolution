package com.example.cse110_wwr_team2;

import java.io.Serializable;

/*
 * This is the simplest class for a route
 *      String start_point: the string that stores the start point of the route
 *      String name: the string that saves the name of the route
 *      int step_cnt: the step count of this route
 */
public class Route implements Serializable {
    private String start_point;
    private String name;
    private String note;
    private int step_cnt;
    private float distance;
    private String features;

    public Route(String start_point, String name, int step_cnt, String note, float distance){
        this.start_point = start_point;
        this.name = name;
        this.step_cnt = step_cnt;
        this.note = note;
        this.features = "00000";
        this.distance = distance;
    }

    public double getDistance(){
        return distance;
    }

    public void setDistance(float distance){
        this.distance = distance;
    }

    public Route(String start_point, String name, int step_cnt, String note, String features){
        this.start_point = start_point;
        this.name = name;
        this.step_cnt = step_cnt;
        this.note = note;
        this.features = features;
    }

    public String getFeatures(){return this.features;}

    public String getNote(){return this.note;}

    public void updateNote(String newNote) {this.note = newNote;}

    public String getStartPoint(){
        return this.start_point;
    }

    public String getName(){
        return this.name;
    }

    public int getStepCnt(){
        return this.step_cnt;
    }

    public void updateStep(int new_step_cnt){
        this.step_cnt = new_step_cnt;
    }

    public void updateDistance(float distance){
        this.distance = distance;
    }

    public String toString(){
        return "Route Name: " + this.name + "\nStart point: " + this.start_point + "\nStep Count: " + this.step_cnt;
    }

    public String[] toList(){
        return new String[]{this.name, this.start_point, Integer.toString(this.step_cnt), Double.toString(distance)};
    }
}
