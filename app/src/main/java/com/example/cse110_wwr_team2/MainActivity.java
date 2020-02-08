package com.example.cse110_wwr_team2;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private String fitnessServiceKey = "GOOGLE_FIT";

    private Button toRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // NOTE: for InputHeight page test only
        // clearSharedPreferences();

        // check if user has input height
        checkUserInputHeight();

        toRoute = (Button) findViewById(R.id.button_route);
        toRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: jump to RouteActivity
                goToRoute();
            }
        });
    }

    private void goToRoute() {
        Intent intent = new Intent(this, RouteActivity.class);
        startActivity(intent);
    }

    private void clearSharedPreferences() {
        SharedPreferences spfs = getSharedPreferences("user", MODE_PRIVATE);
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
}
