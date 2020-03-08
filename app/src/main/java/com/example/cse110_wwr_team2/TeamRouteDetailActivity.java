package com.example.cse110_wwr_team2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cse110_wwr_team2.Route.Route;
import com.example.cse110_wwr_team2.Route.RouteSaver;
import com.example.cse110_wwr_team2.User.CurrentUserInfo;
import com.example.cse110_wwr_team2.firebasefirestore.RouteCallback;
import com.google.android.material.chip.ChipGroup;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TeamRouteDetailActivity extends AppCompatActivity {

    private Route currRoute;
    private ArrayList<Route> routes;
    private EditText note;
    private int index;
    private String curr_note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_route_detail);

        // get the string passed from route activity
        Intent intent = getIntent();
        // final String[] route = intent.getStringArrayExtra("route");
        Bundle args = intent.getBundleExtra("BUNDLE");
        routes = (ArrayList<Route>) args.getSerializable("route_list");
        index = intent.getIntExtra("index",-1);

        currRoute = routes.get(index);
        ChipGroup shapeTags = findViewById(R.id.shape_tags);
        ChipGroup flatnessTags = findViewById(R.id.flatness_tags);
        ChipGroup streetTags = findViewById(R.id.street_tags);
        ChipGroup surfaceTags = findViewById(R.id.surface_tags);
        ChipGroup difficultyTags = findViewById(R.id.difficulty_tags);
        ChipGroup[] allChips = {shapeTags, flatnessTags, streetTags, surfaceTags, difficultyTags};
        setSelectChips(allChips, currRoute.getFeatures());

        //to find out whether there is your data
        Map<String, Float> myDistance = currRoute.getTeammateDistance();
        Map<String, Integer> myStepCnt = currRoute.getTeammateStepCount();
        String myID = getSharedPreferences("user", MODE_PRIVATE).getString("id", null);
        if(myDistance.containsKey(myID) && myStepCnt.containsKey(myID)){
            TextView StepCnt = findViewById(R.id.step_count);
            StepCnt.setText(""+myStepCnt.get(myID));
            TextView Distance = findViewById(R.id.distance);
            Distance.setText(""+myDistance.get(myID));
        }

        // set the text in UI
        TextView Creator = findViewById(R.id.creator_initial);
        Creator.setText(currRoute.getUserInitial());
        TextView RouteName = findViewById(R.id.route_name);
        RouteName.setText(currRoute.getName());
        TextView StepCnt = findViewById(R.id.teammate_step_count);
        StepCnt.setText(""+currRoute.getStepCnt());
        TextView Distance = findViewById(R.id.teammate_distance);
        Distance.setText(""+currRoute.getDistance());
        TextView StartPoint = findViewById(R.id.start_point);
        StartPoint.setText(currRoute.getStartPoint());
        note = findViewById(R.id.note);
        curr_note = currRoute.getNote();
        if (!curr_note.equals("")) {
            note.setText(currRoute.getNote());
        }

        Button back = findViewById(R.id.done_add);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchRoute();
            }
        });
        Button start = findViewById(R.id.start_walk);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchWalk(currRoute.getName());
            }
        });
        Button mock = findViewById(R.id.mock_route);
        mock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMock();
            }
        });
    }
    private void setSelectChips(ChipGroup[] allchips, String features){
        final int[] prefix = {findViewById(R.id.shape1).getId(),
                findViewById(R.id.flatness1).getId(),
                findViewById(R.id.street1).getId(),
                findViewById(R.id.surface1).getId(),
                findViewById(R.id.diff1).getId()};
        for (int i = 0; i < features.length(); i++){
            int index = Character.getNumericValue(features.charAt(i));
            if(index != 0){
                allchips[i].findViewById(prefix[i] + index - 1).setSelected(true);
            }
        }
    }
    public void launchRoute(){
        if(!curr_note.equals(note.getText().toString())) {
            SharedPreferences spfs = getSharedPreferences("all_routes", MODE_PRIVATE);
            SharedPreferences.Editor editor = spfs.edit();
            editor.putString(currRoute.getName()+"_note", note.getText().toString());
            editor.commit();
        }
        Intent intent = new Intent(this, TeamRouteActivity.class);
        startActivity(intent);
        finish();
    }

    public void launchWalk(String route){
        Intent intent = new Intent(this, WalkActivity.class);
        Bundle args = new Bundle();
        boolean flag = true; // the flag to mark this route as teammate's route
        args.putSerializable("route_list",(Serializable)routes);
        intent.putExtra("BUNDLE",args);
        intent.putExtra("index", index);
        intent.putExtra("routeName", route);
        intent.putExtra("ifTeammate", flag);
        intent.putExtra("walkKey", "walk");
        startActivity(intent);
        finish();
    }

    public void launchMock(){
        Intent intent = new Intent(this, InputMockTime.class);
        Bundle args = new Bundle();
        args.putSerializable("route_list",(Serializable)routes);
        intent.putExtra("BUNDLE",args);
        intent.putExtra("index", index);
        intent.putExtra("ifTeammate", true);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        launchRoute();
    }
}
