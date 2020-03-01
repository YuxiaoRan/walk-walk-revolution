package com.example.cse110_wwr_team2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.cse110_wwr_team2.fitness.FitnessService;
import com.example.cse110_wwr_team2.fitness.FitnessServiceFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class IntentActivityUnitTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";
    private Intent intent;
    private int stepCount;
    private final long Fifteen_SEC = 15 * 1000;
    private final double STEP_OVER_HEIGHT = 0.414;
    private final double INCH_PER_MILE = 63360;
    private final int height = 69;


    @Before
    public void setUp() {
        FitnessServiceFactory.put(TEST_SERVICE, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(AppCompatActivity walkActivity) {
                return new TestFitnessService((WalkActivity) walkActivity);
            }
        });
        intent = new Intent(ApplicationProvider.getApplicationContext(), WalkActivity.class);
        //index = intent.getIntExtra("index",-1);
        intent.putExtra("walkKey", TEST_SERVICE);
    }

    @Test
    public void testWalking() {
        ActivityScenario<WalkActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            SharedPreferences sharedPreferences = activity.getSharedPreferences("user", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("height", height);

            TextView steps = activity.findViewById(R.id.step_count);
            TextView distance = activity.findViewById(R.id.distance);
            stepCount = 0;
            assertEquals("0",steps.getText().toString());
            assertEquals("0", distance.getText().toString());

            stepCount = 10;
        });
        //wait for 15 seconds to wait for the step to update
        try {
            Thread.sleep(Fifteen_SEC);
        }catch (Exception e){
            System.err.println("There is an exception in thread sleep");
            assertFalse(true);
        }
        scenario.onActivity(activity -> {
            TextView steps = activity.findViewById(R.id.step_count);
            TextView distance = activity.findViewById(R.id.distance);
            assertEquals("10", steps.getText().toString());
        });


    }

    private class TestFitnessService implements FitnessService {
        private static final String TAG = "[TestFitnessService]: ";
        private WalkActivity walkActivity;

        public TestFitnessService(WalkActivity walkActivity) {
            this.walkActivity = (WalkActivity)walkActivity;
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
            walkActivity.setStepCount(stepCount);
        }

        private double getDistance(long stepCount){
            return height * stepCount * STEP_OVER_HEIGHT / INCH_PER_MILE;
        }
    }

}
