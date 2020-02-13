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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class WalkActivity extends AppCompatActivity {
    private String TAG = "WalkActivity";
    public TextView timer;
    private LocalTime base;
    private long baseStep;
    private MyTimer myTimer;
    private boolean isCancel;
    private String route;
    private TextView stepCount;
    private String walkKey;
    private FitnessService fitnessService;
    private final long TEN_SEC = 10 * 1000;
    private WalkTracker walkTracker;
    private TextView distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        setContentView(R.layout.activity_walk);

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        int height = sharedPreferences.getInt("height", 0);

        stepCount = findViewById(R.id.step_count);
        distance = findViewById(R.id.distance);

        // get the string passed from route activity
        Intent intent = getIntent();
        route = intent.getStringExtra("routeName");
        if(route != null) {
            // set the text in UI
            TextView RouteName = findViewById(R.id.routeName);
            RouteName.setText(route);
        }


        walkKey = intent.getStringExtra("walkKey");
        fitnessService = FitnessServiceFactory.create(walkKey, this);
        fitnessService.setup();

        isCancel = false;

        Button stopBtn = findViewById(R.id.stop_walking);
        timer = findViewById(R.id.timer);
        base = LocalTime.now();
        myTimer = new MyTimer();
        myTimer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        walkTracker= new WalkTracker();
        walkTracker.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fitnessService.updateStepCount(); //to finalize the total steps
                isCancel = true;
                myTimer.cancel(isCancel);
                walkTracker.cancel(isCancel);
                launchAddRoute();

            }
        });
    }

    public void setStepCount(long total){
        stepCount.setText(String.valueOf(total));
    }
    public void setDistance(double d){distance.setText(String.valueOf(d));}
    public void setBaseStep(long baseStep){this.baseStep = baseStep;}
    public long getBaseStep(){return this.baseStep;}
    public int getUserHeight(){
        SharedPreferences spfs = getSharedPreferences("user", MODE_PRIVATE);
        int height = spfs.getInt("height",0);
        return height;
    }

    public void launchAddRoute(){
        if(route == null) {
            Intent intent = new Intent(this, AddRouteActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(this, RouteActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed(){
        if (route == null){
            finish();
        } else {
            Intent intent = new Intent(this, RouteActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private class MyTimer extends AsyncTask<String, String, String>{
        private String resp;

        @Override
        protected String doInBackground(String... param){
            try{
                Log.d(TAG,"In MyTimer Task");
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
            //fitnessService.updateStepCount();
        }

        @Override
        protected  void onCancelled(){
            super.onCancelled();
            Log.d(TAG,"onCancelled");
        }
    }

    private class WalkTracker extends AsyncTask<String, String, String> {
        private String resp;

        @Override
        protected String doInBackground(String... param){
            try{
                Log.d(TAG,"In WalkTracker Task");
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
            Log.d(TAG,"onCancelled");
        }
    }
}
