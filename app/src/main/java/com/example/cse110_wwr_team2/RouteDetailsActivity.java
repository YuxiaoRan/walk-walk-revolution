package com.example.cse110_wwr_team2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RouteDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_details);

        // get the string passed from route activity
        Intent intent = getIntent();
        String route = intent.getExtras().getString("route");
        String[] info = route.split(" ");

        // set the text in UI
        TextView RouteName = findViewById(R.id.route_name);
        RouteName.setText(info[0]);
        TextView StartPoint = findViewById(R.id.start_point);
        StartPoint.setText(info[1]);

        Button back = findViewById(R.id.done_add);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchRoute();
            }
        });

        Button start = findViewById(R.id.done_add);




    }

    public void launchRoute(){
        Intent intent = new Intent(this, RouteActivity.class);
        startActivity(intent);
    }


}
