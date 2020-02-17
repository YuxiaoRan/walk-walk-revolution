package com.example.cse110_wwr_team2;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.cse110_wwr_team2.fitness.FitnessService;
import com.example.cse110_wwr_team2.fitness.FitnessServiceFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class IntentActivityUnitTest {

    private String walkKey;
    private FitnessService fitnessService;
    private int index;

    @Rule
    public ActivityTestRule<WalkActivity> wActivityTestRule = new ActivityTestRule<>(WalkActivity.class);
    private WalkActivity walkActivity = null;


    @Before
    public void setUp() {
        walkActivity = wActivityTestRule.getActivity();
        Intent intent = walkActivity.getIntent();
        index = intent.getIntExtra("index",-1);
        walkKey = intent.getStringExtra("walkKey");
        fitnessService = FitnessServiceFactory.create(walkKey, walkActivity);
        fitnessService.setup();
    }

    @Test
    public void testWalkLaunch() {



        TextView steps = walkActivity.findViewById(R.id.step_count);
        TextView distance = walkActivity.findViewById(R.id.distance);


        assertNull(steps);
        assertNull(distance);

    }

    @After
    public void tearDown() {
        walkActivity = null;
    }
}
