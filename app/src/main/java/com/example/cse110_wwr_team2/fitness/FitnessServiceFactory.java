package com.example.cse110_wwr_team2.fitness;

import android.util.Log;

import com.example.cse110_wwr_team2.MainActivity;
import com.example.cse110_wwr_team2.MockActivity;
import com.example.cse110_wwr_team2.WalkActivity;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class FitnessServiceFactory {

    private static final String TAG = "[FitnessServiceFactory]";

    private static Map<String, BluePrint> blueprints = new HashMap<>();

    public static void put(String key, BluePrint bluePrint) {
        blueprints.put(key, bluePrint);
    }

    public static FitnessService create(String key, AppCompatActivity activity) {
        Log.i(TAG, String.format("creating FitnessService for activity with key %s", key));
        return blueprints.get(key).create(activity);
    }

    /*
    public static FitnessService create(String key, WalkActivity walkActivity) {
        Log.i(TAG, String.format("creating FitnessService for WalkActivity with key %s", key));
        return blueprints.get(key).create(walkActivity);
    }

    public static FitnessService create(String key, MockActivity mockActivity) {
        Log.i(TAG, String.format("creating FitnessService for MockActivity with key %s", key));
        return blueprints.get(key).create(mockActivity);
    }*/

    public interface BluePrint {
        FitnessService create(AppCompatActivity activity);
    }
}