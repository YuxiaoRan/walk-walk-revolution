package com.example.cse110_wwr_team2;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class AddRouteUnitTest {

    @Rule
    public ActivityTestRule<AddRouteActivity> rActivityTestRule = new ActivityTestRule<>(AddRouteActivity.class);
    private AddRouteActivity routeActivity = null;


    @Before
    public void setUp() {
        routeActivity = rActivityTestRule.getActivity();
    }

    @Test
    public void testRouteLaunch() {
        ListView routeList = routeActivity.findViewById(R.id.route_list);
        assertNull(routeList);


    }

    @Test
    public void test1Route() {

        FloatingActionButton btn = routeActivity.findViewById(R.id.done_add);

        // Stop Walk/Add Walk and test that values are not null
        AutoCompleteTextView rName = routeActivity.findViewById(R.id.route_name);
        AutoCompleteTextView start = routeActivity.findViewById(R.id.start_point);
        rName.setText("Test1");
        start.setText("UCSD");
        btn.performClick();

        assertEquals(rName.getText().toString(), "Test1");
        assertEquals(start.getText().toString(), "UCSD");

        assertNotEquals(rName.getText().toString(), "Test2");
        assertNotEquals(start.getText().toString(), "Market St.");



        ArrayList<Route> allRoutes = RouteSaver.getAllRoutes(routeActivity);
        assertEquals(allRoutes.size(), 1);

    }

    @Test
    public void test2Route() {

        FloatingActionButton btn = routeActivity.findViewById(R.id.done_add);

        // Stop Walk/Add Walk and test that values are not null
        AutoCompleteTextView rName = routeActivity.findViewById(R.id.route_name);
        AutoCompleteTextView start = routeActivity.findViewById(R.id.start_point);
        rName.setText("Test1");
        start.setText("UCSD");
        btn.performClick();

        assertEquals(rName.getText().toString(), "Test1");
        assertEquals(start.getText().toString(), "UCSD");

        AutoCompleteTextView rName2 = routeActivity.findViewById(R.id.route_name);
        AutoCompleteTextView start2 = routeActivity.findViewById(R.id.start_point);
        rName2.setText("Test2");
        start2.setText("Market St.");
        btn.performClick();
        assertEquals(rName2.getText().toString(), "Test2");
        assertEquals(start2.getText().toString(), "Market St.");



        ArrayList<Route> allRoutes = RouteSaver.getAllRoutes(routeActivity);
        assertEquals(allRoutes.size(), 2);

    }

    @Test
    public void test15Routes() {

        FloatingActionButton btn = routeActivity.findViewById(R.id.done_add);
        // Save 15 routes
        for(int i = 0; i < 15; i++) {
            AutoCompleteTextView rName = routeActivity.findViewById(R.id.route_name);
            AutoCompleteTextView start = routeActivity.findViewById(R.id.start_point);
            rName.setText("Test" + i);
            start.setText("Route" + i);
            btn.performClick();
            assertEquals(rName.getText().toString(), "Test" + i);
            assertEquals(start.getText().toString(), "Route" + i);
        }

        ArrayList<Route> allRoutes = RouteSaver.getAllRoutes(routeActivity);
        assertEquals(allRoutes.size(), 15);

    }




    @After
    public void tearDown() {
        routeActivity = null;
    }
}
