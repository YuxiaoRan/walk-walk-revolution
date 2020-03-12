package com.example.cse110_wwr_team2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cse110_wwr_team2.Route.Route;
import com.example.cse110_wwr_team2.Route.RouteSaver;
import com.example.cse110_wwr_team2.User.User;
import com.example.cse110_wwr_team2.User.UserOnlineSaver;
import com.example.cse110_wwr_team2.firebasefirestore.UserCallBack;
import com.example.cse110_wwr_team2.firebasefirestore.WalkedRouteAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * The page for user to add a route, either manually or just walked
 */
public class AddRouteActivity extends AppCompatActivity {
    private String TAG ="AddRouteActivity";
    FloatingActionButton fab;
    AutoCompleteTextView start;
    AutoCompleteTextView name;
    EditText note;
    int[] features;
    boolean ifNewFinish;
    private WalkedRouteAdapter walkedRouteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);
        Intent intent = getIntent();
        final int step_cnt = intent.getIntExtra("step_cnt", 0);
        features = new int[5];
        final float distance = intent.getFloatExtra("distance", 0);
        ifNewFinish = intent.getBooleanExtra("ifNewFinish", false); // indicates if it is a new walk that's not previously saved

        fab = findViewById(R.id.done_add);
        start = findViewById(R.id.start_point);
        name = findViewById(R.id.route_name);
        note = findViewById(R.id.note);

        walkedRouteAdapter = new WalkedRouteAdapter();

        // autocomplete options
        String[] nameSuggestion = getResources().getStringArray(R.array.route_names);
        String[] startSuggestion = getResources().getStringArray(R.array.start_points);
        ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, nameSuggestion);
        ArrayAdapter<String> startAdapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, startSuggestion);
        name.setAdapter(nameAdapter);
        start.setAdapter(startAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // If the user does not enter anything in the field
                if (name.getText().toString().equals("")){
                    Toast.makeText(AddRouteActivity.this,
                            "Please input your route name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (start.getText().toString().equals("")){
                    Toast.makeText(AddRouteActivity.this,
                            "Please input your start point", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check if the route name is already in the list
                if (checkName(name.getText().toString())){
                    String userID = getSharedPreferences("user", MODE_PRIVATE).getString("id", null);
                    // Saving the route
                    RouteSaver routeSaver = new RouteSaver(AddRouteActivity.this);
                    Route currRoute = new Route(start.getText().toString(), name.getText().toString(),step_cnt,
                            note.getText().toString(),returnFeatures(), distance, userID);
                    routeSaver.addNewRoute(currRoute);
                    Log.d(TAG, "onClick: "+returnFeatures());
                    // if this route is a finished walk
                    if ( ifNewFinish ){
                        // save the Route as walked
                        walkedRouteAdapter.addToWalked(userID,currRoute.getId());

                        UserOnlineSaver userSaver = new UserOnlineSaver();
                        userSaver.updateLatestWalk(userID, currRoute.getId(), new UserCallBack() {
                            @Override
                            public void onCallBack() {
                                startActivity(intent);
                                finish();
                            }
                            public void onCallback(ArrayList<User> users){return;}
                        });
                    }

                    launchRoute();
                } else {
                    Toast.makeText(AddRouteActivity.this, "Please choose another name. This route name has been chosen",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // backend part of tags
        ChipGroup shapeTags = findViewById(R.id.shape_tags);
        ChipGroup flatnessTags = findViewById(R.id.flatness_tags);
        ChipGroup streetTags = findViewById(R.id.street_tags);
        ChipGroup surfaceTags = findViewById(R.id.surface_tags);
        ChipGroup difficultyTags = findViewById(R.id.difficulty_tags);
        ChipGroup[] allChips = {shapeTags, flatnessTags, streetTags, surfaceTags, difficultyTags};
        setChipChangeListener(allChips);
    }

    public void setChipChangeListener(ChipGroup[] chips){
        for (int i = 0; i < chips.length; i ++){
            final int index = i;
            chips[i].setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(ChipGroup chipGroup, int j) {
                    Chip chip = chipGroup.findViewById(j);
                    j = j % 11;
                    if (j == 0){
                        j = 11;
                    }
                    if (chip != null){
                        int num = j - index * 2;
                        //Toast.makeText(getApplicationContext(), "The id is " + j + " and the number is" + num, Toast.LENGTH_SHORT).show();
                        features[index] = j - index * 2;
                    }
                }
            });
        }
    }

    public void launchRoute(){
        Intent intent = new Intent(this, RouteActivity.class);
        startActivity(intent);
        finish();
    }

    private String returnFeatures(){
        String f = "";
        for(int i = 0; i < features.length; i ++){
            f += features[i];
        }
        System.out.println(f);
        return f;
    }

    /*
     * This function checks whether the route_name already exists in the routes_list,
     * since all the routes are stored with the key of its route_name. Therefore, if
     * the name is already in the route_list, we will ask the user to re-enter a new
     * route name.
     */
    public boolean checkName(String route_name){
        SharedPreferences spfs = getSharedPreferences("all_routes", MODE_PRIVATE);
        Set<String> route_list = spfs.getStringSet("route_list", new TreeSet<String>());
        return (route_list.size() == 0) || !route_list.contains(route_name);
    }

}