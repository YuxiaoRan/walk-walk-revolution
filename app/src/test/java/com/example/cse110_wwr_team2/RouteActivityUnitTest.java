package com.example.cse110_wwr_team2;

import android.widget.AutoCompleteTextView;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.shadows.ShadowToast;

import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class RouteActivityUnitTest {

    @Rule
    public ActivityTestRule<AddRouteActivity> rActivityTestRule = new ActivityTestRule<>(AddRouteActivity.class);
    private AddRouteActivity routeActivity = null;


    @Before
    public void setUp() {
        routeActivity = rActivityTestRule.getActivity();

    }

    @Test
    public void testRouteLaunch() {

            AutoCompleteTextView rName = routeActivity.findViewById(R.id.route_name);
            AutoCompleteTextView start = routeActivity.findViewById(R.id.start_point);

            assertEquals(rName.getText().toString(), "");
            assertEquals(start.getText().toString(), "");
        //});

    }

    @Test
    public void testRouteValues() {
        AutoCompleteTextView rName = routeActivity.findViewById(R.id.route_name);
        AutoCompleteTextView start = routeActivity.findViewById(R.id.start_point);
        rName.setText("Test1");
        start.setText("UCSD");

        assertEquals(rName.getText().toString(), "Test1");
        assertEquals(start.getText().toString(), "UCSD");

        AutoCompleteTextView rName2 = routeActivity.findViewById(R.id.route_name);
        AutoCompleteTextView start2 = routeActivity.findViewById(R.id.start_point);
        rName2.setText("Test2");
        start2.setText("Market St.");

        assertEquals(rName2.getText().toString(), "Test2");
        assertEquals(start2.getText().toString(), "Market St.");


    }

    @Test
    public void testNoRouteValues() {
        AutoCompleteTextView rName = routeActivity.findViewById(R.id.route_name);
        AutoCompleteTextView start = routeActivity.findViewById(R.id.start_point);
        FloatingActionButton btn = routeActivity.findViewById(R.id.done_add);
        btn.performClick();

        assertEquals(ShadowToast.getTextOfLatestToast(), "Please input your route name");

    }

    @Test
    public void testNoStartValues() {
        AutoCompleteTextView rName = routeActivity.findViewById(R.id.route_name);
        AutoCompleteTextView start = routeActivity.findViewById(R.id.start_point);
        rName.setText("Test2");
        FloatingActionButton btn = routeActivity.findViewById(R.id.done_add);
        btn.performClick();

        assertEquals(ShadowToast.getTextOfLatestToast(), "Please input your start point");


    }
    @After
    public void tearDown() {
        routeActivity = null;
    }
}
