package com.example.cse110_wwr_team2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.internal.runner.junit4.statement.UiThreadStatement;
import androidx.test.rule.ActivityTestRule;

import com.example.cse110_wwr_team2.fitness.FitnessService;
import com.example.cse110_wwr_team2.fitness.FitnessServiceFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.w3c.dom.Text;

import static android.content.Context.MODE_PRIVATE;
import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNull;

public class IntentActivityUnitTest {
    Button btn;
    @Rule
    public ActivityTestRule<MainActivity> rActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    private MainActivity routeActivity = null;


    @Before
    public void setUp() {
        routeActivity = rActivityTestRule.getActivity();
    }

    @Test
    public void testWalkLaunch() {


        btn = routeActivity.findViewById(R.id.start_walk);
        try {
            UiThreadStatement.runOnUiThread(() -> {
                btn.performClick();
                TextView steps = routeActivity.findViewById(R.id.step_count);
                TextView distance = routeActivity.findViewById(R.id.distance);


                assertNull(steps);
                assertNull(distance);
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

    @After
    public void tearDown() {
        routeActivity = null;
    }
}
