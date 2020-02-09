package com.example.cse110_wwr_team2.fitness;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import com.example.cse110_wwr_team2.MainActivity;
import com.example.cse110_wwr_team2.WalkActivity;

public class FitnessServiceFactory {

    private static final String TAG = "[FitnessServiceFactory]";

    private static Map<String, BluePrint> blueprints = new HashMap<>();
    private static Map<String, BluePrint2> blueprints2 = new HashMap<>();

    public static void put(String key, BluePrint bluePrint) {
        blueprints.put(key, bluePrint);
    }
    public static void put(String key, BluePrint2 bluePrint) {
        blueprints2.put(key, bluePrint);
    }

    public static FitnessService create(String key, MainActivity stepCountActivity) {
        Log.i(TAG, String.format("creating FitnessService with key %s", key));
        return blueprints.get(key).create(stepCountActivity);
    }

    public static FitnessService create(String key, WalkActivity stepCountActivity) {
        Log.i(TAG, String.format("creating FitnessService with key %s", key));
        return blueprints2.get(key).create(stepCountActivity);
    }

    public interface BluePrint {
        FitnessService create(MainActivity stepCountActivity);
    }
    public interface BluePrint2 {
        FitnessService create(WalkActivity stepCountActivity);
    }

}
