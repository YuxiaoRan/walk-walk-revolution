package com.example.cse110_wwr_team2;

import android.widget.Button;
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

    private TextView CurrStep;
    private Button mock;

    @Before
    public void setup() {
        scenariomain = scenarioMain.getScenario();
    }


    @Test
    public void testStepDisplayed(){
        scenariomain.onActivity(activity -> {
             CurrStep = activity.findViewById(R.id.main_step_count);
             assertTrue(CurrStep.isShown());
        });
    }


}
