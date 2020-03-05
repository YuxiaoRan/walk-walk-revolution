package com.example.cse110_wwr_team2;


import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.example.cse110_wwr_team2.fitness.FitnessService;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.Context.MODE_PRIVATE;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class LoginTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";
    private final long Fifteen_SEC = 15 * 1000;
    private int stepCount;

    @Rule

    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, false);

    private MainActivity mainActivity = null;
    private SharedPreferences.Editor editor;
    private SharedPreferences spfs;
    private Context context;

    @Before
    public void setUp() {
        //mainActivity = mActivityTestRule.getActivity();
        context = getInstrumentation().getTargetContext();
        spfs = context.getSharedPreferences("user", MODE_PRIVATE);
        editor = spfs.edit();
        editor.putInt("height", 65);
        editor.putBoolean("firstLogin", false);
        editor.commit();

    }

    @Test
    public void testMainLaunch() {

        mActivityTestRule.launchActivity(new Intent().putExtra("test_label", true));
        mainActivity = mActivityTestRule.getActivity();

        TextView mainSteps = mainActivity.findViewById(R.id.main_step_count);
        TextView mainDistance = mainActivity.findViewById(R.id.main_distance);

        TextView lastSteps = mainActivity.findViewById(R.id.main_intention_step_count);
        TextView lastDistance = mainActivity.findViewById(R.id.main_intention_distance);

        assertNotNull(mainSteps);
        assertNotNull(mainDistance);
        assertNotNull(lastSteps);
        assertNotNull(lastDistance);

    }

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
