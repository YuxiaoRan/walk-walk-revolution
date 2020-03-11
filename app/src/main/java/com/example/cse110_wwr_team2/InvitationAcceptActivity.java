package com.example.cse110_wwr_team2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class InvitationAcceptActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();
        launchTeamPage();
    }

    // launch invitation activity
    public void launchTeamPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(getApplicationContext(), TeamActivity.class);
                Log.d("launch", "Team from Invitation");
                startActivity(intent);
            }
        }, 2000);
    }
}
