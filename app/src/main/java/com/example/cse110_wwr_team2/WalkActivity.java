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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class WalkActivity extends AppCompatActivity {
    public TextView timer;
    private LocalTime base;
    private MyTimer myTimer;
    private boolean isCancel;
    private String route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG","onCreate");
        setContentView(R.layout.activity_walk);

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

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        protected  void onCancelled(){
            super.onCancelled();
            Log.d("TAG","onCancelled");
        }
    }
}
