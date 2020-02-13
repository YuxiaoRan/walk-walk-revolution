package com.example.cse110_wwr_team2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.Serializable;
import java.util.ArrayList;

public class RouteDetailsActivity extends AppCompatActivity {

    private Route currRoute;
    private ArrayList<Route> routes;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_details);

        // get the string passed from route activity
        Intent intent = getIntent();
        // final String[] route = intent.getStringArrayExtra("route");
        Bundle args = intent.getBundleExtra("BUNDLE");
        routes = (ArrayList<Route>) args.getSerializable("route_list");
        index = intent.getIntExtra("index",-1);

        currRoute = routes.get(index);


        // set the text in UI
        TextView RouteName = findViewById(R.id.route_name);
        RouteName.setText(currRoute.getName());
        TextView StartPoint = findViewById(R.id.start_point);
        StartPoint.setText(currRoute.getStartPoint());


        Button back = findViewById(R.id.done_add);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchRoute();
            }
        });
        Button start = findViewById(R.id.start_walk);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchWalk(currRoute.getName());
            }
        });

    }

    public void launchRoute(){
        Intent intent = new Intent(this, RouteActivity.class);
        startActivity(intent);
        finish();
    }

    public void launchWalk(String route){
        Intent intent = new Intent(this, WalkActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("route_list",(Serializable)routes);
        intent.putExtra("BUNDLE",args);
        intent.putExtra("index", index);
        intent.putExtra("routeName", route);
        intent.putExtra("walkKey", "walk");
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        launchRoute();
    }
}
