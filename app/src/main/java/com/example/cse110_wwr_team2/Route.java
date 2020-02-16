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
    private long step_cnt;
    private String features;

    public Route(String start_point, String name, int step_cnt, String note){
        this.start_point = start_point;
        this.name = name;
        this.step_cnt = step_cnt;
        this.note = note;
        this.features = "00000";
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

    public long getStepCnt(){
        return this.step_cnt;
    }

    public void updateStep(long new_step_cnt){
        this.step_cnt = new_step_cnt;
    }

    public String toString(){
        return "Route Name: " + this.name + "\nStart point: " + this.start_point + "\nStep Count: " + this.step_cnt;
    }

    public String[] toList(){
        return new String[]{this.name, this.start_point, Long.toString(this.step_cnt)};
    }
}
