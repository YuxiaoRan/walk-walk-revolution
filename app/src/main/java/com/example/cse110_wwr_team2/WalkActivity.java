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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class WalkActivity extends AppCompatActivity {
    public TextView timer;
    private LocalTime base;
    private MyTimer myTimer;
    private MyStepCounter myStepCounter;
    private boolean isCancel;
    private String route;
    private long step_orig;
    private long updated_step_cnt;

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

        // Get the route name
        Intent intent = getIntent();
        route = intent.getStringExtra("routeName");
        if(route != null) {
            // set the text in UI
            TextView RouteName = findViewById(R.id.routeName);
            RouteName.setText(route);
        }

        // Step counter stuff
        textSteps = findViewById(R.id.step_count);

        String fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
        FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint2() {
            @Override
            public FitnessService create(WalkActivity stepCountActivity) {
                return new GoogleFitAdapter(stepCountActivity);
            }
        });
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        step_orig = fitnessService.getTotalStep();
        fitnessService.updateStepCount();
        fitnessService.setup();


        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        int height = sharedPreferences.getInt("height", 0);

        Button stopBtn = findViewById(R.id.stop_walking);
        timer = findViewById(R.id.timer);
        base = LocalTime.now();
        myTimer = new MyTimer();
        myStepCounter = new MyStepCounter();
        isCancel = false;
        myTimer.execute();
        myStepCounter.execute();

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updated_step_cnt = fitnessService.getTotalStep() - step_orig;
                isCancel = true;
                myTimer.cancel(isCancel);
                launchAddRoute();
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

    private class MyStepCounter extends AsyncTask<String, String, String>{
        private String resp;
        private long step_cnt;
        @Override
        protected String doInBackground(String... param){
            Log.d("Tag", "In Task");
            while(!isCancel){
                fitnessService.updateStepCount();
                long step_now = fitnessService.getTotalStep();
                step_cnt = step_now - step_orig;
                publishProgress(Long.toString(step_cnt));
                try {
                    Thread.sleep(1000);
                } catch (Exception e){
                    e.printStackTrace();
                    resp = e.getMessage();
                }
            }
            resp = "done";
            return resp;
        }

        public long getStepCnt(){return step_cnt;}

        @Override
        protected void onPreExecute() {textSteps.setText("0");}

        @Override
        protected void onProgressUpdate(String... param){
            textSteps.setText(param[0]);
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
        protected void onCancelled(){
            super.onCancelled();
            Log.d("TAG","onCancelled");
        }
    }

    public void setStepCount(long stepCount) {
        textSteps.setText(String.valueOf(stepCount - step_orig));
    }
}
