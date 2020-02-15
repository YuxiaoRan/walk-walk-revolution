package com.example.cse110_wwr_team2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class InputMockTime extends AppCompatActivity {
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

        base = LocalTime.now();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    curr = LocalTime.parse(inputTime.getText().toString());
                    if(curr.isBefore(base)){
                        laterToast();
                        return;
                    }
                }catch (DateTimeParseException date_exception){
                    formatToast();
                    return;
                }
                startMocking();
            }
        });
    }
    private void laterToast(){
        Toast.makeText(this, "Please enter a time later than"+base.toString(), Toast.LENGTH_LONG);
    }
    private void formatToast(){
        Toast.makeText(this, "Check the format of your input", Toast.LENGTH_LONG);
    }
    private void startMocking(){
        Intent inIntent = getIntent();
        Intent outIntent = new Intent(this, MockActivity.class);
        String duration = getDuration();
        outIntent.putExtra("timer", duration);
        outIntent.putExtra("BUNDLE", inIntent.getBundleExtra("BUNDLE"));
        outIntent.putExtra("index", inIntent.getIntExtra("index",-1));
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
