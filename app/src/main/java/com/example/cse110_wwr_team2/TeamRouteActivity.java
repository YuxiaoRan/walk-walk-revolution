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
import com.example.cse110_wwr_team2.firebasefirestore.TeamRouteCallback;

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
        saver.getTeamRoutes(new TeamRouteCallback() {
            @Override
            public void onCallback(List<Route> routes, List<String> routes_info) {
                //ArrayAdapter arrayAdapter = new ArrayAdapter(TeamRouteActivity.this, android.R.layout.simple_list_item_1, routes_info);
                RouteListAdapter routeListAdapter = new RouteListAdapter(TeamRouteActivity.this, android.R.layout.simple_list_item_1,routes, routes_info);
                listView.setAdapter(routeListAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    // int position is the position index of the clicked item in the list
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("button", "item " + position + "in list");
                        //launchRouteDetails(position);
                        Intent intent = new Intent(TeamRouteActivity.this, TeamRouteDetailActivity.class);
                        Bundle args = new Bundle();
                        args.putSerializable("route_list",(Serializable)routes);
                        intent.putExtra("BUNDLE",args);
                        intent.putExtra("index", position);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }
}
