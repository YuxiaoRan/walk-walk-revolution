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
import com.example.cse110_wwr_team2.fitness.GoogleFitAdapter;

import org.w3c.dom.Text;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

public class WalkActivity extends AppCompatActivity {
    public TextView timer;
    private LocalTime base;
    private MyTimer myTimer;
    private boolean isCancel;
    private String route;

    //For main activity step count
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    private static final String TAG = "WalkActivity";
    private TextView textSteps;
    private FitnessService fitnessService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG","onCreate");
        setContentView(R.layout.activity_walk);


        // Step counter stuff
        textSteps = findViewById(R.id.step_count);

        final String fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
        FitnessServiceFactory.put2(fitnessServiceKey, new FitnessServiceFactory.BluePrint2() {
            @Override
            public FitnessService create(WalkActivity stepCountActivity) {
                return new GoogleFitAdapter(stepCountActivity);
            }
        });
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);

        //fitnessService.updateStepCountWalk();

        fitnessService.setupWalk();




        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        int height = sharedPreferences.getInt("height", 0);

        // get the string passed from route activity
        Intent intent = getIntent();
        route = intent.getStringExtra("routeName");
        if(route != null) {
            // set the text in UI
            TextView RouteName = findViewById(R.id.routeName);
            RouteName.setText(route);
        }

        Button stopBtn = findViewById(R.id.stop_walking);
        timer = findViewById(R.id.timer);
        base = LocalTime.now();
        myTimer = new MyTimer();
        isCancel = false;
        myTimer.execute();

        // Update walks
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                fitnessService.updateStepCountWalk();
            }
        };
        final Timer t = new Timer();
        long delay = 0;
        long duration = 10;
        t.scheduleAtFixedRate(task, delay, duration);


        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCancel = true;
                myTimer.cancel(isCancel);
                launchAddRoute();
                t.cancel();
                TextView g = findViewById(R.id.step_count);
                String re = Long.toString(fitnessService.returntotalStep());
                g.setText(re);

            }
        });


    }


    public void launchAddRoute(){
        if(route == null) {
            Intent intent = new Intent(this, AddRouteActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, RouteActivity.class);
            startActivity(intent);
        }
    }

    private class MyTimer extends AsyncTask<String, String, String>{
        private String resp;

        @Override
        protected String doInBackground(String... param){
            try{
                Log.d("TAG","In Task");
                while(!isCancel){
                    long baseSec = base.getSecond();
                    long baseMin = base.getMinute();
                    long baseHr = base.getHour();
                    LocalTime curr = LocalTime.now();
                    curr = curr.minusSeconds(baseSec);
                    curr = curr.minusMinutes(baseMin);
                    curr = curr.minusHours(baseHr);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    resp = formatter.format(curr);
                    //fitnessService.updateStepCountWalk();
                    publishProgress(resp);

                }
            }catch(Exception e){
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result){
            timer.setText(result);

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            timer.setText(values[0]);
        }

        @Override
        protected  void onCancelled(){
            super.onCancelled();
            Log.d("TAG","onCancelled");
        }
    }

    public void setStepCount(long stepCount) {
        textSteps.setText(String.valueOf(stepCount));
    }
}