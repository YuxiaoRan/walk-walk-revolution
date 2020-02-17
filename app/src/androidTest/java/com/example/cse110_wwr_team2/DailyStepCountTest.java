package com.example.cse110_wwr_team2;

import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class DailyStepCountTest {
    @Rule
    public ActivityScenarioRule<MainActivity> scenarioMain = new ActivityScenarioRule<>(MainActivity.class);
    private ActivityScenario<MainActivity> scenariomain;

    private double Height;
    private TextView DistanceTraveled;

    @Before
    public void setup() {
        scenariomain = scenarioMain.getScenario();
    }

    // fill in id when master is updated
    private void init(MainActivity mainActivity) {
        DistanceTraveled = mainActivity.findViewById(R.id.main_intention_distance);
    }

    @Test
    public void testDistanceDisplayed(){
        scenariomain.onActivity(activity -> {
             DistanceTraveled = activity.findViewById(R.id.main_intention_distance);
             assertTrue(DistanceTraveled.isShown());
        });
    }
}
