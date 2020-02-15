package com.example.cse110_wwr_team2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Set;
import java.util.TreeSet;

public class AddRouteActivity extends AppCompatActivity {

    FloatingActionButton fab;
    AutoCompleteTextView start;
    AutoCompleteTextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);
        Intent intent = getIntent();
        final int step_cnt = intent.getIntExtra("step_cnt", 0);
        final float distance = intent.getFloatExtra("distance", 0);

        fab = findViewById(R.id.done_add);
        start = findViewById(R.id.start_point);
        name = findViewById(R.id.route_name);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If the user does not enter anything in the field
                if (name.getText().toString().equals("")){
                    Toast.makeText(AddRouteActivity.this,
                            "Please input your route name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (start.getText().toString().equals("")){
                    Toast.makeText(AddRouteActivity.this,
                            "Please input your start point", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check if the route name is already in the list
                if (checkName(name.getText().toString())){
                    addNewRoute(name.getText().toString(), start.getText().toString(), step_cnt, distance);
                    launchRoute();
                } else {
                    Toast.makeText(AddRouteActivity.this, "Please choose another name. This route name has been chosen",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void launchRoute(){
        Intent intent = new Intent(this, RouteActivity.class);
        startActivity(intent);
        finish();
    }


    /*
     * This function checks whether the route_name already exists in the routes_list,
     * since all the routes are stored with the key of its route_name. Therefore, if
     * the name is already in the route_list, we will ask the user to re-enter a new
     * route name.
     */
    public boolean checkName(String route_name){
        SharedPreferences spfs = getSharedPreferences("all_routes", MODE_PRIVATE);
        Set<String> route_list = spfs.getStringSet("route_list", new TreeSet<String>());
        return (route_list.size() == 0) || !route_list.contains(route_name);
    }

    /*
     * This function will add a new route into the file, by writing a new name
     * into the Set<String> and update "{route_name}_start_point" and "{route_name}_step_cnt"
     * accordingly
     */
    public void addNewRoute(String route_name, String start_point, int step_cnt, float distance){
        SharedPreferences spfs = getSharedPreferences("all_routes", MODE_PRIVATE);
        Set<String> routes_list = spfs.getStringSet("route_list", new TreeSet<String>());
        SharedPreferences.Editor editor = spfs.edit();
        try {
            routes_list.add(route_name);
            editor.putStringSet("route_list", routes_list);
            editor.putString(route_name + "_start_point", start_point);
            editor.putInt(route_name + "_step_cnt", step_cnt);
            editor.putFloat(route_name + "_distance", distance);
            editor.apply();
        }catch (Exception e){
            System.err.println(e);
        }
    }

}