package com.example.cse110_wwr_team2;

import android.content.Intent;
import android.os.Bundle;

import com.example.cse110_wwr_team2.Route.Route;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ProposeWalkActivity extends AppCompatActivity {
    private Route route;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propose_walk);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        route = (Route) args.getSerializable("route");

        TextView name = findViewById(R.id.textView2);
        name.setText(route.toString());

        Button propose = findViewById(R.id.button2);
        propose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}
