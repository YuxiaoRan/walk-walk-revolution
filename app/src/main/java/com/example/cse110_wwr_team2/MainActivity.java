package com.example.cse110_wwr_team2;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cse110_wwr_team2.fitness.FitnessService;
import com.example.cse110_wwr_team2.fitness.FitnessServiceFactory;
import com.example.cse110_wwr_team2.fitness.GoogleFitAdapter;

public class MainActivity extends AppCompatActivity {
    private String fitnessServiceKey = "GOOGLE_FIT";

    //For main activity step count
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    private static final String TAG = "StepCountActivity";
    private TextView textSteps;
    private FitnessService fitnessService;



    private Button toRoute;
    private Button startRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Step counter stuff
        textSteps = findViewById(R.id.totalMileDisplay);

        String fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
        FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(MainActivity stepCountActivity) {
                return new GoogleFitAdapter(stepCountActivity);
            }
        });
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);


                fitnessService.updateStepCount();

        fitnessService.setup();

        //fitnessService.updateStepCount();



        // NOTE: for InputHeight page test only
        // clearUserInfo();

        // NOTE: for route details test only
        // clearRouteDetails();

        // check if user has input height
        checkUserInputHeight();

        toRoute = (Button) findViewById(R.id.button_route);
        toRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRoute();
            }
        });

        startRoute = (Button) findViewById(R.id.button_start);
        startRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    // Set the initial step count
    public void setStepCount(long stepCount) {
        textSteps.setText(String.valueOf(stepCount));
    }
}
