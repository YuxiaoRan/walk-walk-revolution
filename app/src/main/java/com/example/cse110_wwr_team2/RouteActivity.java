package com.example.cse110_wwr_team2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class RouteActivity extends AppCompatActivity {

    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        listView = (ListView)findViewById(R.id.route_list);

        // should be a list of route
        final ArrayList<String> routes = new ArrayList<String>();

        routes.add("route1 start1");
        routes.add("route2 start2");
        routes.add("route3 start3");
        routes.add("route4 start4");

        // set the route list to the adapter and display on listView
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,routes);
        listView.setAdapter(arrayAdapter);

        // set listener on clicking an item in the list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            // int position is the position index of the clicked item in the list
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("button", "item " + position + "in list");
                launchRouteDetails(routes.get(position));
            }
        });

        // the floating add button
        FloatingActionButton fab = findViewById(R.id.add_route);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("button","add route in RouteActivity");
                launchAddRoute();
            }
        });

    }

    public void launchAddRoute(){
        Intent intent = new Intent(this, AddRouteActivity.class);
        Log.d("launch","Add Route from Route");
        startActivity(intent);
    }

    public void launchRouteDetails(String route){
        Intent intent = new Intent(this, RouteDetailsActivity.class);
        intent.putExtra("route", route);
        Log.d("launch","RouteDetails from Route");
        startActivity(intent);
    }

}
