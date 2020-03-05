package com.example.cse110_wwr_team2.User;

import android.util.Log;
import android.widget.Toast;

import com.example.cse110_wwr_team2.InvitationActivity;
import com.example.cse110_wwr_team2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;

import androidx.annotation.NonNull;

public class User extends FirebaseMessagingService {
    private String id;
    private String gmail;
    private String name;
    private int height;
    private String teamID;
    private String lastWalkID;
    private String token;

    public User(){}

    public User(String id, String gmail, String name, int height) {
        this.id = id;
        this.gmail = gmail;
        this.name = name;
        this.height = height;
        this.teamID = teamID;
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

    public String getToken() {
        return token;
    }

    public void setToken(String regToken) {
        this.token = regToken;
    }

    public static String getInitial(String name){
        return ""+name.charAt(0);
    }
}
