package com.example.cse110_wwr_team2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.cse110_wwr_team2.Route.Route;
import com.example.cse110_wwr_team2.Route.RouteSaver;
import com.example.cse110_wwr_team2.firebasefirestore.RouteCallback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TeamRouteActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<Route> Routes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_route);

        listView = findViewById(R.id.team_route_list);
        RouteSaver saver = new RouteSaver(this);
        saver.getTeamRoutes(new RouteCallback() {
            @Override
            public void onCallback(List<Route> routes) {
                ArrayAdapter arrayAdapter = new ArrayAdapter(TeamRouteActivity.this, android.R.layout.simple_list_item_1, routes);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    // int position is the position index of the clicked item in the list
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("button", "item " + position + "in list");
                        //launchRouteDetails(position);
                        Intent intent = new Intent(TeamRouteActivity.this, RouteDetailsActivity.class);
                        Bundle args = new Bundle();
                        args.putSerializable("route_list",(Serializable)routes);
                        intent.putExtra("BUNDLE",args);
                        intent.putExtra("index", position);

                        // This indicates that we comes from the team routes page to here
                        intent.putExtra("team", true);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }
}
