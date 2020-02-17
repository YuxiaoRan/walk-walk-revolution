package com.example.cse110_wwr_team2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.Context.MODE_PRIVATE;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class MainActivityUnitTest {

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

    @Test
    public void testMainValue() {

        mActivityTestRule.launchActivity(new Intent().putExtra("test_label", true));
        mainActivity = mActivityTestRule.getActivity();

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
    }

    @After
     public void tearDown() {
        mainActivity = null;
    }
}
