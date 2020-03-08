package com.example.cse110_wwr_team2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.cse110_wwr_team2.ProposedRoute.ProposedRoute;
import com.example.cse110_wwr_team2.ProposedRoute.ProposedRouteSaver;
import com.example.cse110_wwr_team2.Team.Team;
import com.example.cse110_wwr_team2.Team.TeamAdapter;
import com.example.cse110_wwr_team2.User.CurrentUserInfo;
import com.example.cse110_wwr_team2.User.User;
import com.example.cse110_wwr_team2.firebasefirestore.ProposedRouteCallback;
import com.example.cse110_wwr_team2.firebasefirestore.TeammateCallBack;
import com.example.cse110_wwr_team2.firebasefirestore.UserCallBack;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AllProposedActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_proposed);

        listView = findViewById(R.id.proposed_list);

        FloatingActionButton fabHome = findViewById(R.id.proposed_back_home);
        fabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("button","back to home activity");
                launchHome();
            }
        });
        ProposedRouteSaver saver = new ProposedRouteSaver(AllProposedActivity.this);
        saver.getAllRoutes(new ProposedRouteCallback() {
            @Override
            public void onCallback(List<ProposedRoute> routes) {
                ArrayAdapter arrayAdapter = new ArrayAdapter(AllProposedActivity.this, android.R.layout.simple_list_item_1, routes);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    // int position is the position index of the clicked item in the list
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("button", "item " + position + "in list");
                        TeamAdapter adapter = new TeamAdapter();
                        adapter.getTeammates(new UserCallBack() {
                            @Override
                            public void onCallback(ArrayList<User> users) {
                                Intent intent = new Intent(AllProposedActivity.this, ProposedWalkDetailActivity.class);
                                Bundle args = new Bundle();
                                args.putSerializable("route",(Serializable) routes);
                                args.putSerializable("users", (Serializable) users);
                                intent.putExtra("BUNDLE",args);
                                intent.putExtra("index", position);
                                intent.putExtra("isScheduler", CurrentUserInfo.getId(AllProposedActivity.this).equals(routes.get(position).getProposerID()));
                                startActivity(intent);
                                finish();
                            }
                        }, AllProposedActivity.this);
                    }
                });
            }
        });
    }
    public void launchHome(){
        Intent intent = new Intent(this, MainActivity.class);
        Log.d("launch","Home from team");
        startActivity(intent);
        finish();
    }
}
