package com.example.cse110_wwr_team2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.cse110_wwr_team2.Route.Route;
import com.example.cse110_wwr_team2.Route.RouteSaver;
import com.example.cse110_wwr_team2.User.CurrentUserInfo;
import com.example.cse110_wwr_team2.User.User;
import com.example.cse110_wwr_team2.firebasefirestore.TeamRouteCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * The page with all the teammate's routes displayed
 */
public class TeamRouteActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<Route> Routes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_route);

        listView = findViewById(R.id.team_route_list);
        RouteSaver saver = new RouteSaver(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(CurrentUserInfo.getId(this))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            User user = task.getResult().toObject(User.class);
                            String teamID = (String)task.getResult().get("teamID");

                            SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("teamID",teamID);
                            editor.apply();

                            //callBack.onCallback(user.getTeamID());
                        }else{
                            Log.d(TAG, "Failure at attaining the team id");
                        }
                    }
                });

        saver.getTeamRoutes(new TeamRouteCallback() {
            @Override
            public void onCallback(List<Route> routes, List<String> routes_info) {
                //ArrayAdapter arrayAdapter = new ArrayAdapter(TeamRouteActivity.this, android.R.layout.simple_list_item_1, routes_info);
                RouteListAdapter routeListAdapter = new RouteListAdapter(TeamRouteActivity.this, android.R.layout.simple_list_item_1,routes, routes_info);
                listView.setAdapter(routeListAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    // int position is the position index of the clicked item in the list
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("button", "item " + position + "in list");
                        //launchRouteDetails(position);
                        Intent intent = new Intent(TeamRouteActivity.this, TeamRouteDetailActivity.class);
                        Bundle args = new Bundle();
                        args.putSerializable("route_list",(Serializable)routes);
                        intent.putExtra("BUNDLE",args);
                        intent.putExtra("index", position);

                        // This indicates that we comes from the team routes page to here
                        intent.putExtra("team", true);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });

        // add onClickListener to the floating button
        FloatingActionButton fabHome = findViewById(R.id.team_back_home);
        fabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("button","back to home activity");
                launchHome();
            }
        });

    }

    // launch main activity
    public void launchHome(){
        Intent intent = new Intent(this, MainActivity.class);
        Log.d("launch","Home from Route");
        startActivity(intent);
        finish();
    }
}
