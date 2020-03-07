package com.example.cse110_wwr_team2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cse110_wwr_team2.Route.Route;
import com.example.cse110_wwr_team2.Route.RouteSaver;
import com.example.cse110_wwr_team2.firebasefirestore.RouteUpdateCallback;
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
    private String startTime;

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
            currRoute = routes.get(index); // the current Route object
            TextView RouteName = findViewById(R.id.mock_routeName);
            RouteName.setText(currRoute.getName());
        }
        String time = intent.getStringExtra("timer");
        timer.setText(time);

        startTime = intent.getStringExtra("time");

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
    public int getCurrStep(){
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
        double adjDist = Math.floor(dist*100) / 100;
        distance.setText(Double.toString(adjDist));


    }
    public void launchAddRoute(){
        // use shared preferences to store the most recent mocked step and distance
        saveMockData();

        Log.d(TAG, "launchAddRoute: "+"currStep: "+currStep+" distance: "+distance.getText().toString());
        if(currRoute == null) {
            Intent intent = new Intent(this, AddRouteActivity.class);
            intent.putExtra("step_cnt", currStep);
            intent.putExtra("distance", Float.parseFloat(distance.getText().toString()));
            saveRecent();
            startActivity(intent);
            finish();
        }else{
            currRoute.updateStep(currStep);
            currRoute.updateDistance(Float.parseFloat(distance.getText().toString()));
            RouteSaver saver = new RouteSaver();
            Intent intent = new Intent(this, RouteActivity.class);
            saver.UpdateRoute(currRoute, new RouteUpdateCallback() {
                @Override
                public void onCallback() {
                    saveRecent();
                    startActivity(intent);
                    finish();
                }
            });
            /*
            currRoute.updateStep(currStep);
            currRoute.updateDistance(Float.parseFloat(distance.getText().toString()));
            //UpdateRoute(currRoute.getName(),currRoute.getStartPoint(),(int)currStep,Float.parseFloat(distance.getText().toString()));
            RouteSaver routeSaver = new RouteSaver();
            currRoute.updateDistance(Float.parseFloat(distance.getText().toString()));
            currRoute.updateStep(currStep);
            routeSaver.UpdateRoute(currRoute);
            //RouteSaver.UpdateRoute(currRoute.getId(),currRoute.getName(),currRoute.getStartPoint(),currStep,Float.parseFloat(distance.getText().toString()),this);
            Intent intent = new Intent(this, RouteDirectorActivity.class);
            saveRecent();
            startActivity(intent);
            finish();*/
        }
    }

    // save the mocking data for displaying on main page, etc
    private void saveMockData(){
        SharedPreferences sharedPreferences = getSharedPreferences("MOCKING",MODE_PRIVATE);
        int last_mock_step = sharedPreferences.getInt("mock_step",0);
        float last_mock_distance = sharedPreferences.getFloat("mock_distance",0);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("mock_step", currStep+last_mock_step);
        editor.putFloat("mock_distance",Float.parseFloat(distance.getText().toString()) + last_mock_distance);
        editor.commit();
    }


    private void saveRecent(){
        SharedPreferences spfs = getSharedPreferences("recent_route", MODE_PRIVATE);
        float dist = Float.parseFloat(distance.getText().toString());
        SharedPreferences.Editor editor = spfs.edit();
        try{
            editor.clear();
            editor.putInt("recent_step_cnt", currStep);
            editor.putFloat("recent_distance", dist);
            editor.putString("time", startTime);
            editor.apply();
        }catch (Exception e){
            System.err.println(e);
            Log.d(TAG, "saveRecent: "+e.toString());
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
