package com.example.cse110_wwr_team2.fitness;

import android.util.Log;

import com.example.cse110_wwr_team2.MainActivity;
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

    public static FitnessService create(String key, MainActivity mainActivity) {
        Log.i(TAG, String.format("creating FitnessService for MainActivity with key %s", key));
        return blueprints.get(key).create(mainActivity);
    }

    public static FitnessService create(String key, WalkActivity walkActivity) {
        Log.i(TAG, String.format("creating FitnessService for WalkActivity with key %s", key));
        return blueprints.get(key).create(walkActivity);
    }

    public interface BluePrint {
        FitnessService create(AppCompatActivity activity);
    }
}