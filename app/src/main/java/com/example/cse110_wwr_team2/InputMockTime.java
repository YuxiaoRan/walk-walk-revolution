package com.example.cse110_wwr_team2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class InputMockTime extends AppCompatActivity {
    private String TAG = "InputMockTime";
    private LocalTime base;
    private LocalTime curr;
    private EditText inputTime;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_mock_time);
        inputTime = findViewById(R.id.mock_curr_time);
        btn = findViewById(R.id.mock_time_btn);


        long base_millis = System.currentTimeMillis(); // the current milli second
        base = LocalDateTime.ofInstant(Instant.ofEpochMilli(base_millis),
                ZoneId.systemDefault()).toLocalTime();
        Log.d(TAG, "Base Millisecond: "+base_millis);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    //curr = LocalTime.parse(inputTime.getText().toString());
                    long curr_millis = Long.parseLong(inputTime.getText().toString()); // the user input time
                    curr = LocalDateTime.ofInstant(Instant.ofEpochMilli(curr_millis),
                            ZoneId.systemDefault()).toLocalTime();
                    Log.d(TAG, "Current Millisecond: "+curr_millis);
                    if(curr.isBefore(base)){
                        Log.d(TAG, "The input time is too early");
                        laterToast();
                        return;
                    }
                }catch (Exception e){
                    Log.d(TAG, "There is an exception"+e.toString());
                    formatToast();
                    return;
                }
                startMocking();
            }
        });
    }
    private void laterToast(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        Toast.makeText(InputMockTime.this, "Please enter a time later than"+formatter.format(base), Toast.LENGTH_LONG).show();
    }
    private void formatToast(){
        Toast.makeText(InputMockTime.this, "Check the format of your input", Toast.LENGTH_LONG).show();
    }
    private void startMocking(){
        Intent inIntent = getIntent();
        Intent outIntent = new Intent(this, MockActivity.class);
        String duration = getDuration();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        outIntent.putExtra("time", formatter.format(base));
        outIntent.putExtra("timer", duration);
        outIntent.putExtra("BUNDLE", inIntent.getBundleExtra("BUNDLE"));
        outIntent.putExtra("index", inIntent.getIntExtra("index",-1));
        outIntent.putExtra("ifTeammate", inIntent.getBooleanExtra("ifTeammate", false));
        startActivity(outIntent);
        finish();
    }
    private String getDuration(){
        curr = curr.minusSeconds(base.getSecond());
        curr = curr.minusMinutes(base.getMinute());
        curr = curr.minusHours(base.getHour());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String duration = formatter.format(curr);
        return duration;
    }
}
