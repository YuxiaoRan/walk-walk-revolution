package com.example.cse110_wwr_team2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cse110_wwr_team2.fitness.FitnessService;
import com.example.cse110_wwr_team2.fitness.FitnessServiceFactory;
import com.example.cse110_wwr_team2.fitness.MockWalkAdapter;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class MockActivity extends AppCompatActivity {
    private String TAG = "MockActivity";
    private int currStep;
    private Button btn;
    private Button stop;
    private TextView stepCount;
    private TextView distance;
    private TextView timer;
    private FitnessService fitnessService;
    private Route currRoute;
    private ArrayList<Route> routes;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);

        btn = findViewById(R.id.mock_update_btn);
        stepCount = findViewById(R.id.mock_step_count);
        distance = findViewById(R.id.mock_distance);
        stop = findViewById(R.id.mock_stop_walking);
        timer = findViewById(R.id.mock_timer);

        FitnessServiceFactory.put("mock", new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(AppCompatActivity mockActivity) {
                return new MockWalkAdapter((MockActivity)mockActivity);
            }
        });
        fitnessService = FitnessServiceFactory.create("mock", this);
        fitnessService.setup();

        // get the string passed from route activity
        Intent intent = getIntent();
        index = intent.getIntExtra("index",-1);

        /* change of logic, using the object directly to easier modify steps saved
            set currRoute only when it is actually passed
         */
        if(index != -1){
            Bundle args = intent.getBundleExtra("BUNDLE");
            routes = (ArrayList<Route>) args.getSerializable("route_list");
            Log.d("route_list", routes.toString());
            currRoute = routes.get(index);
            TextView RouteName = findViewById(R.id.mock_routeName);
            RouteName.setText(currRoute.getName());
        }
        String time = intent.getStringExtra("timer");
        timer.setText(time);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fitnessService.updateStepCount();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAddRoute();
            }
        });
    }
    public void setCurrStep(int stepCount){
        currStep = stepCount;
    }
    public long getCurrStep(){
        return currStep;
    }
    public void incrementStep(){
        currStep += 500;
    }
    public void setStepCount(){
        stepCount.setText(Integer.toString((int)currStep));
    }
    public double getUserHeight(){
        SharedPreferences spfs = getSharedPreferences("user", MODE_PRIVATE);
        double height = spfs.getInt("height",0);
        return height;
    }
    public void setDistance(double dist){
        distance.setText(Double.toString(dist));
    }
    public void launchAddRoute(){
        Log.d(TAG, "launchAddRoute: "+"currStep: "+currStep+" distance: "+distance.getText().toString());
        if(currRoute == null) {
            Intent intent = new Intent(this, AddRouteActivity.class);
            intent.putExtra("step_cnt", currStep);
            intent.putExtra("distance", Float.parseFloat(distance.getText().toString()));
            startActivity(intent);
            finish();
        }else{
            currRoute.updateStep(currStep);
            currRoute.updateDistance(Float.parseFloat(distance.getText().toString()));
            //UpdateRoute(currRoute.getName(),currRoute.getStartPoint(),(int)currStep,Float.parseFloat(distance.getText().toString()));
            RouteSaver.UpdateRoute(currRoute.getName(),currRoute.getStartPoint(),currStep,Float.parseFloat(distance.getText().toString()),this);
            Intent intent = new Intent(this, RouteActivity.class);
            startActivity(intent);
            finish();
        }
    }
    /*
     * This function will add a new route into the file, by writing a new name
     * into the Set<String> and update "{route_name}_start_point" and "{route_name}_step_cnt"
     * accordingly
     */
    public void UpdateRoute(String route_name, String start_point, int step_cnt, float distance){
        SharedPreferences spfs = getSharedPreferences("all_routes", MODE_PRIVATE);
        Set<String> routes_list = spfs.getStringSet("route_list", new TreeSet<String>());
        SharedPreferences.Editor editor = spfs.edit();
        try {
            routes_list.remove(route_name);
            routes_list.add(route_name);
            editor.putStringSet("route_list", routes_list);
            editor.putString(route_name + "_start_point", start_point);
            editor.putInt(route_name + "_step_cnt", step_cnt);
            editor.putFloat(route_name+"_distance",distance);
            editor.apply();
        }catch (Exception e){
            System.err.println(e);
        }
    }

}
