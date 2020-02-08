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
    FloatingActionButton fab = findViewById(R.id.done_add);
    AutoCompleteTextView start = findViewById(R.id.start_point);
    AutoCompleteTextView name = findViewById(R.id.route_name);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().equals("")){
                    Toast.makeText(AddRouteActivity.this,
                            "Please input your route name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (start.getText().toString().equals("")){
                    Toast.makeText(AddRouteActivity.this,
                            "Please input your start point", Toast.LENGTH_SHORT).show();
                    return;
                }
                addNewRoute(name.getText().toString(), start.getText().toString());
                launchRoute();
            }
        });

    }

    public void launchRoute(){
        Intent intent = new Intent(this, RouteActivity.class);
        startActivity(intent);
    }

    public void addNewRoute(String route_name, String start_point){
        SharedPreferences spfs = getSharedPreferences("all_routes", MODE_PRIVATE);
        SharedPreferences.Editor editor = spfs.edit();
        Set<String> routes_list = spfs.getStringSet("route_list", new TreeSet<String>());
        try {
            routes_list.add(route_name);
            editor.putStringSet("route_list", routes_list);
            editor.putString(route_name + "_start_point", start_point);
            editor.putInt(route_name + "_step_cnt", 0);
            editor.apply();
        }catch (Exception e){
            System.err.println(e);
        }
    }

}