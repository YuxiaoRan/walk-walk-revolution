package com.example.cse110_wwr_team2;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cse110_wwr_team2.fitness.FitnessService;
import com.example.cse110_wwr_team2.fitness.FitnessServiceFactory;
import com.example.cse110_wwr_team2.fitness.MainFitAdapter;
import com.example.cse110_wwr_team2.fitness.WalkFitAdapter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {
    private String mainKey = "main";
    private String walkKey = "walk";
    private FitnessService fitnessService;

    private Button toRoute;
    private Button startRoute;
    private TextView stepCount;

    private WalkTracker walkTracker;
    private boolean isCancel;
    private final long TEN_SEC = 10 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // NOTE: for InputHeight page test only
        //clearUserInfo();

        // NOTE: for route details test only
        //clearRouteDetails();

        // check if user has input height
        checkUserInputHeight();

        toRoute = (Button) findViewById(R.id.button_route);
        toRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRoute();
            }
        });

        stepCount = findViewById(R.id.main_step_count);

        FitnessServiceFactory.put(mainKey, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(AppCompatActivity mainActivity) {
                return new MainFitAdapter((MainActivity) mainActivity);
            }
        });
        FitnessServiceFactory.put(walkKey, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(AppCompatActivity walkActivity) {
                return new WalkFitAdapter((WalkActivity) walkActivity);
            }
        });
        fitnessService = FitnessServiceFactory.create(mainKey, this);
        fitnessService.setup();
        walkTracker = new WalkTracker();
        isCancel = false;
        walkTracker.execute();

        startRoute = (Button) findViewById(R.id.button_start);
        startRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCancel = true;
                walkTracker.cancel(isCancel);
                goToWalk();
            }
        });
    }

    private void goToRoute() {
        Intent intent = new Intent(this, RouteActivity.class);
        startActivity(intent);
    }
  
    private void goToWalk() {
        Intent intent = new Intent(this, WalkActivity.class);
        String routeName = null;
        intent.putExtra("routeName", routeName);
        intent.putExtra("walkKey",walkKey);
        startActivity(intent);
    }

    private void clearUserInfo() {
        SharedPreferences spfs = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = spfs.edit();
        editor.clear().commit();
    }

    private void clearRouteDetails() {
        SharedPreferences spfs = getSharedPreferences("route_list", MODE_PRIVATE);
        SharedPreferences.Editor editor = spfs.edit();
        editor.clear().commit();
    }

    private void checkUserInputHeight() {
        SharedPreferences spfs = getSharedPreferences("user", MODE_PRIVATE);

        int heightInt = spfs.getInt("height", 0);
        boolean isFirstTime = spfs.getBoolean("firstLogin", true);

        if(heightInt > 0 && !isFirstTime) {
            return;
        } else {
            // if it's first-time login, go to InputHeight page
            goToInputHeight();
        }
    }

    private void goToInputHeight() {
        Intent intent = new Intent(this, InputHeightActivity.class);
        startActivity(intent);
    }

    public void setStepCount(long total){
        stepCount.setText(String.valueOf(total));
    }

    public void setMainKey(String mainKey) {
        this.mainKey = mainKey;
    }

    private class WalkTracker extends AsyncTask<String, String, String> {
        private String resp;

        @Override
        protected String doInBackground(String... param){
            try{
                Log.d("TAG","In Task");
                while(!isCancel) {
                    publishProgress(resp);
                    long time = TEN_SEC;
                    Thread.sleep(time);
                }
            }catch(Exception e){
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result){

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            fitnessService.updateStepCount();
        }

        @Override
        protected  void onCancelled(){
            super.onCancelled();
            Log.d("TAG","onCancelled");
        }
    }
}
