package com.example.cse110_wwr_team2;

import android.widget.TextView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DisplayDistanceTraveledTest {
    @Rule
    public ActivityScenarioRule<MainActivity> scenarioRule = new ActivityScenarioRule<>(MainActivity.class);
    private ActivityScenario<MainActivity> scenario;

    private TextView DistanceTraveled;

    @Before
    public void setup() {
        scenario = scenarioRule.getScenario();
    }


    @Test
    public void testDistanceDisplayed(){
        scenario.onActivity(activity -> {
             DistanceTraveled = activity.findViewById(R.id.main_distance);
             assertTrue(DistanceTraveled.isShown());
        });
    }


}
