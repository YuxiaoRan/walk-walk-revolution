package com.example.cse110_wwr_team2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;


/**
 * activity triggered when user decline the invitation
 */
public class InvitationDeclineActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();
        launchTeamPage();
    }

    // launch invitation activity
    public void launchTeamPage() {
        Intent intent = new Intent(this, TeamActivity.class);
        Log.d("launch", "Team from Invitation");
        startActivity(intent);
    }
}
