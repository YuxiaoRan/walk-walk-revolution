package com.example.cse110_wwr_team2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.example.cse110_wwr_team2.fitness.FitnessService;
import com.example.cse110_wwr_team2.fitness.FitnessServiceFactory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.Context.MODE_PRIVATE;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class MainActivityUnitTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";
    private final long Fifteen_SEC = 15 * 1000;
    private int stepCount;

    @Rule
    public ActivityScenarioRule<MainActivity> activityTestRule = new ActivityScenarioRule<>(MainActivity.class);
    private ActivityScenario<AddRouteActivity> scenario;

    @Before
    public void setUp() {
        FitnessServiceFactory.put(TEST_SERVICE, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(AppCompatActivity activity) {
                return new MainActivityUnitTest.TestFitnessService((MainActivity) activity);
            }
        });
        //scenario =
    }

    @Test
    public void testMainLaunch() {
    }

    /*
    @Test
    public void testMainValue() {
        SharedPreferences sharedPreferences = mainActivity.getSharedPreferences("recent_route", MODE_PRIVATE);
        int lastStep = sharedPreferences.getInt("recent_step_cnt", 0);
        float lastDistance = sharedPreferences.getFloat("recent_distance", 0);

        TextView mainSteps = mainActivity.findViewById(R.id.main_step_count);
        TextView mainDistance = mainActivity.findViewById(R.id.main_distance);

        TextView lastStep2 = mainActivity.findViewById(R.id.main_intention_step_count);
        TextView lastDistance2 = mainActivity.findViewById(R.id.main_intention_distance);
        int lastStepNum = Integer.parseInt(lastStep2.getText().toString());
        float lastDistanceNum = Float.parseFloat(lastDistance2.getText().toString());


        assertEquals(lastStep, lastStepNum);
        assertEquals(lastDistance, lastDistanceNum);
    }*/

    private class TestFitnessService implements FitnessService {
        private static final String TAG = "[TestFitnessService]: ";
        private MainActivity activity;

        public TestFitnessService(MainActivity mainActivity) {
            this.activity = mainActivity;
        }

        @Override
        public int getRequestCode() {
            return 0;
        }

        @Override
        public void setup() {
            System.out.println(TAG + "setup");
        }

        @Override
        public void updateStepCount() {
            System.out.println(TAG + "updateStepCount");
            activity.setStepCount(stepCount);
        }
    }
}
