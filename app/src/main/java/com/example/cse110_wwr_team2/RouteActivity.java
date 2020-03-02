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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;

public class RouteActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Route> routes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        listView = (ListView)findViewById(R.id.route_list);
        routes = RouteSaver.getAllRoutes(this);

        // set the route list to the adapter and display on listView
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, routes);
        listView.setAdapter(arrayAdapter);

        // set listener on clicking an item in the list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            // int position is the position index of the clicked item in the list
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("button", "item " + position + "in list");
                launchRouteDetails(position);
            }
        });

        // the floating add button
        FloatingActionButton fabAdd = findViewById(R.id.add_route);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("button","add route in RouteActivity");
                launchAddRoute();
            }
        });

        FloatingActionButton fabHome = findViewById(R.id.back_home);
        fabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("button","back to home activity");
                launchHome();
            }
        });

    }

    public void launchAddRoute(){
        Intent intent = new Intent(this, AddRouteActivity.class);
        Log.d("launch","Add Route from Route");
        startActivity(intent);
        finish();
    }

    public void launchRouteDetails(int i){
        Intent intent = new Intent(this, RouteDetailsActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("route_list",(Serializable)routes);
        intent.putExtra("BUNDLE",args);
        intent.putExtra("index", i);
        startActivity(intent);
        finish();
    }

    // launch main activity
    public void launchHome(){
        Intent intent = new Intent(this, MainActivity.class);
        Log.d("launch","Home from Home");
        startActivity(intent);
        finish();
    }
}
