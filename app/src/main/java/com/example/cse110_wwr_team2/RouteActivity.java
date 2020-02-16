package com.example.cse110_wwr_team2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.TreeSet;

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

    /*
     * This method gets all the routes from the local file and put into routes
     * which will be shown in the activity
     * The file is saved in such format:
     *          "route_list" is a Set<String>, and should be a TreeSet<String>
     *               where each String denotes the route_name of a route.
     *          "{route_name}_start_point" stores the String of the startpoint
     *               of the route with route_name
     *          "{route_name}_step_cnt" stores the int number of the step counts
     *               of the route with route_name
     */
/*
    public void getAllRoutes(){
        SharedPreferences spfs = getSharedPreferences("all_routes", MODE_PRIVATE);
        Set<String> routes_list = spfs.getStringSet("route_list", new TreeSet<String>());
        Iterator<String> itr = routes_list.iterator();
        routes = new ArrayList<>();
        while(itr.hasNext()){
            String route_name = itr.next();
            String route_start_point = spfs.getString(route_name + "_start_point", "");
            int step_cnt = spfs.getInt(route_name+"_step_cnt", 0);
            String note = spfs.getString(route_name+"_note", "");
            String features = spfs.getString(route_name+"_features", "00000");
            float distance = spfs.getFloat(route_name+"_distance", 0);
            routes.add(new Route(route_start_point, route_name, step_cnt, note, features,distance));
        }
    }
    */
}
