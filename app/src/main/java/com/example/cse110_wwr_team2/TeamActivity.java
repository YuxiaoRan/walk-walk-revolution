package com.example.cse110_wwr_team2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cse110_wwr_team2.Team.TeamAdapter;
import com.example.cse110_wwr_team2.firebasefirestore.TeammateCallBack;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.List;

/**
 * display teammates name, greyed out if not accpeted yet
 */
public class TeamActivity extends AppCompatActivity {

    ListView listView;
    TextView pendingListView;
    FloatingActionButton fabHome;
    FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);


        //listView = findViewById(R.id.teammate_list);
        //pendingListView = (TextView) findViewById(R.id.teammate_pending_list);
        fabHome = findViewById(R.id.team_back_home);
        fabAdd = fabAdd = findViewById(R.id.add_teammate);

        fabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("button","back to home activity");
                launchHome();
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("button","invitation activity");
                launchInvitation();
            }
        });

        TeamAdapter teamAdapter = new TeamAdapter(this);
        teamAdapter.getTeammatesNames(new TeammateCallBack() {
            LinearLayout layout = (LinearLayout) findViewById(R.id.teammate_list2);

            @Override
            public void onCallback(List<String> userNames) {
                //layout.addView(view);
                /*
                ArrayAdapter arrayAdapter = new ArrayAdapter(TeamActivity.this, android.R.layout.simple_list_item_1, userNames);
                listView.setAdapter(arrayAdapter);

                 */

                for(int i = 0; i < userNames.size(); i++) {
                    TextView newTextView = new TextView(getApplicationContext());
                    newTextView.setText(userNames.get(i));
                    newTextView.setTextSize(25);
                    newTextView.setTypeface(null, Typeface.BOLD);
                    newTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                    layout.addView(newTextView);
                }
            }

            @Override
            public void onCallbackPending(List<String> userNames) {

                for(int i = 0; i < userNames.size(); i++) {
                    TextView newTextView = new TextView(getApplicationContext());
                    newTextView.setText(userNames.get(i));
                    newTextView.setTextSize(25);
                    newTextView.setTypeface(null, Typeface.ITALIC);
                    newTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                    newTextView.setTextColor(Color.parseColor("#dadada"));
                    layout.addView(newTextView);
                }

                /*
                ArrayAdapter arrayAdapter = new ArrayAdapter(TeamActivity.this, android.R.layout.simple_list_item_1, userNames);

                listView.setAdapter(arrayAdapter);

                 */


            }
        });


    }

    // launch main activity
    public void launchHome(){
        Intent intent = new Intent(this, MainActivity.class);
        Log.d("launch","Home from team");
        startActivity(intent);
        finish();
    }

    // launch invitation activity
    public void launchInvitation(){
        Intent intent = new Intent(this, InvitationActivity.class);
        Log.d("launch","Invitation from team");
        startActivity(intent);
    }
}
