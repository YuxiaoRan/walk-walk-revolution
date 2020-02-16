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

public class RouteActivityUnitTest {

    @Rule
    public ActivityTestRule<RouteActivity> rActivityTestRule = new ActivityTestRule<>(RouteActivity.class);
    private RouteActivity routeActivity = null;


    @Before
    public void setUp() {
        routeActivity = rActivityTestRule.getActivity();
    }

    @Test
    public void testRouteLaunch() {

        AutoCompleteTextView rName = routeActivity.findViewById(R.id.route_name);
        AutoCompleteTextView start = routeActivity.findViewById(R.id.start_point);

        assertNull(rName);
        assertNull(start);

    }

    @Test
    public void testRouteValues() {


        Button btn = routeActivity.findViewById(R.id.stop_walking);
        try {
            UiThreadStatement.runOnUiThread(() -> {

                btn.performClick();
                // Stop Walk/Add Walk and test that values are not null
                AutoCompleteTextView rName = routeActivity.findViewById(R.id.route_name);
                AutoCompleteTextView start = routeActivity.findViewById(R.id.start_point);
                rName.setText("Test1");
                start.setText("UCSD");

                assertEquals(rName, "Test1");
                assertEquals(start, "UCSD");

                rName.setText("Test2");
                start.setText("Market St.");

                assertEquals(rName, "Test2");
                assertEquals(start, "Market St.");

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
