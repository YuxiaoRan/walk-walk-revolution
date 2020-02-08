package com.example.cse110_wwr_team2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
        getAllRoutes();

        // set the route list to the adapter and display on listView
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, routes);
        listView.setAdapter(arrayAdapter);

        // set listener on clicking an item in the list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            // int position is the position index of the clicked item in the list
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchRouteDetails(routes.get(position));
            }
        });

        // the floating add button
        FloatingActionButton fab = findViewById(R.id.add_route);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAddRoute();
            }
        });

    }

    public void launchAddRoute(){
        Intent intent = new Intent(this, AddRouteActivity.class);
        startActivity(intent);
    }

    public void launchRouteDetails(Route route){
        Intent intent = new Intent(this, RouteDetailsActivity.class);
        intent.putExtra("route", route.toList());
        startActivity(intent);
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
    public void getAllRoutes(){
        SharedPreferences spfs = getSharedPreferences("all_routes", MODE_PRIVATE);
        Set<String> routes_list = spfs.getStringSet("route_list", new TreeSet<String>());
        Iterator<String> itr = routes_list.iterator();
        routes = new ArrayList<Route>();
        while(itr.hasNext()){
            String route_name = itr.next();
            String route_start_point = spfs.getString(route_name + "_start_point", "");
            int step_cnt = spfs.getInt(route_name+"_step_cnt", 0);
            routes.add(new Route(route_name, route_start_point, step_cnt));
        }
    }

}
