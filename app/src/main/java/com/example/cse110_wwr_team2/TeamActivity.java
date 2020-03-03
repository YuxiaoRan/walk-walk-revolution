package com.example.cse110_wwr_team2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.cse110_wwr_team2.Team.TeamAdapter;
import com.example.cse110_wwr_team2.firebasefirestore.TeammateCallBack;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TeamActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        listView = findViewById(R.id.teammate_list);

        FloatingActionButton fabHome = findViewById(R.id.team_back_home);
        fabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("button","back to home activity");
                launchHome();
            }
        });

        TeamAdapter teamAdapter = new TeamAdapter();
        teamAdapter.getTeammatesNames(new TeammateCallBack() {
            @Override
            public void onCallback(List<String> userNames) {
                ArrayAdapter arrayAdapter = new ArrayAdapter(TeamActivity.this, android.R.layout.simple_list_item_1, userNames);
                listView.setAdapter(arrayAdapter);
            }
        });


    }

    // launch main activity
    public void launchHome(){
        Intent intent = new Intent(this, MainActivity.class);
        Log.d("launch","Home from team");
        startActivity(intent);
        finish();
    }
}
