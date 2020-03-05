package com.example.cse110_wwr_team2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RouteDirectorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_director);

        Button MyRoutes = findViewById(R.id.my_routes);
        Button TeamRoutes = findViewById(R.id.team_routes);

        MyRoutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPersonalRoutes();
            }
        });

        TeamRoutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toTeamRoutes();
            }
        });
    }

    private void toPersonalRoutes(){
        Intent intent = new Intent(this, RouteActivity.class);
        startActivity(intent);
    }

    private void toTeamRoutes(){
        Intent intent = new Intent(this, RouteActivity.class);
        startActivity(intent);
    }
}
