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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Set;
import java.util.TreeSet;

public class AddRouteActivity extends AppCompatActivity {
    private String TAG ="AddRouteActivity";
    FloatingActionButton fab;
    AutoCompleteTextView start;
    AutoCompleteTextView name;
    EditText note;
    int[] features;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);
        Intent intent = getIntent();
        final int step_cnt = intent.getIntExtra("step_cnt", 0);
        features = new int[5];
        final float distance = intent.getFloatExtra("distance", 0);

        fab = findViewById(R.id.done_add);
        start = findViewById(R.id.start_point);
        name = findViewById(R.id.route_name);
        note = findViewById(R.id.note);

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
                    RouteSaver.addNewRoute(name.getText().toString(), start.getText().toString(),
                            step_cnt, distance,note.getText().toString(), returnFeatures(),AddRouteActivity.this);
                    Log.d(TAG, "onClick: "+returnFeatures());
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

    /*
     * This function will add a new route into the file, by writing a new name
     * into the Set<String> and update "{route_name}_start_point" and "{route_name}_step_cnt"
     * accordingly
     */
  /*
//<<<<<<< jerryxu
    public void addNewRoute(int step_cnt){
        String route_name = name.getText().toString();
        String start_point = start.getText().toString();
        String note_txt = note.getText().toString();
        SharedPreferences spfs = getSharedPreferences("all_routes", MODE_PRIVATE);
        Set<String> routes_list = spfs.getStringSet("route_list", new TreeSet<String>());
        SharedPreferences.Editor editor = spfs.edit();
        try {
            routes_list.add(route_name);
            editor.putStringSet("route_list", routes_list);
            editor.putString(route_name + "_start_point", start_point);
            editor.putInt(route_name + "_step_cnt", step_cnt);
            editor.putString(route_name + "_note", note_txt);
            editor.putString(route_name + "_features", returnFeatures());
            editor.apply();
        }catch (Exception e){
            System.err.println(e);
        }
    }
=======
//    public void addNewRoute(String route_name, String start_point, int step_cnt, float distance, String note_txt, String feature){
//        SharedPreferences spfs = getSharedPreferences("all_routes", MODE_PRIVATE);
//        Set<String> routes_list = spfs.getStringSet("route_list", new TreeSet<String>());
//        SharedPreferences.Editor editor = spfs.edit();
//        try {
//            routes_list.add(route_name);
//            editor.putStringSet("route_list", routes_list);
//            editor.putString(route_name + "_start_point", start_point);
//            editor.putInt(route_name + "_step_cnt", step_cnt);
//            editor.putString(route_name + "_note", note_txt);
//            editor.putString(route_name + "_features", feature);
//            editor.apply();
//        }catch (Exception e){
//            System.err.println(e);
//        }
//    }
//>>>>>>> Leo*/
}