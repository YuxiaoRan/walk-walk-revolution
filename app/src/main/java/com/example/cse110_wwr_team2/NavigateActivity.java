package com.example.cse110_wwr_team2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class NavigateActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);

        intent = getIntent();
        String dest = intent.getStringExtra("startpoint");
        String destUri = getDestUri(dest);

        launchGoogleMaps(destUri);
    }

    private String getDestUri(String dest) {
        String[] destArr = dest.split("\\s+");
        String query = "";
        for(String str: destArr) {
            query += str;
            query += "+";
        }
        if(query.length() > 0) {
            query = query.substring(0, query.length() - 1);
        }
        String base = "http://maps.google.com/maps/search/?api=1&query=";
        return base + query;
    }

    private void launchGoogleMaps(String destUri) {
        System.out.println(destUri);
        Intent launchMapIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(destUri));
        startActivity(launchMapIntent);
        finish();
    }
}
