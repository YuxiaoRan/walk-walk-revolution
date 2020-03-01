package com.example.cse110_wwr_team2;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.AutoCompleteTextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Set;
import java.util.TreeSet;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class AddRouteUnitTest {
    private FloatingActionButton btn;
    private AutoCompleteTextView rName;
    private AutoCompleteTextView start;

    @Rule
    public ActivityScenarioRule<AddRouteActivity> rActivityTestRule = new ActivityScenarioRule<>(AddRouteActivity.class);
    private ActivityScenario<AddRouteActivity> scenario;

    @Before
    public void setUp() {
        scenario = rActivityTestRule.getScenario();
    }

    private void init(AddRouteActivity routeActivity) {
        btn = routeActivity.findViewById(R.id.done_add);
        rName = routeActivity.findViewById(R.id.route_name);
        start = routeActivity.findViewById(R.id.start_point);
    }

    /*
    @Test
    public void testRouteLaunch() {
        ListView routeList = routeActivity.findViewById(R.id.route_list);
        assertNull(routeList);
    }*/

    @Test
    public void testAddNewRoute() {

        scenario.onActivity(routeActivity->{
            init(routeActivity);
            rName.setText("Test1");
            start.setText("UCSD");
            btn.performClick();
            SharedPreferences spfs = routeActivity.getSharedPreferences("all_routes", Context.MODE_PRIVATE);
            Set<String> routes_list = spfs.getStringSet("route_list", new TreeSet<String>());
            // Testing whether the route is added
            assertTrue(routes_list.contains("Test1"));
            // Testing if the information is added
            String startPnt = spfs.getString("Test1"+"_start_point", "");
            // Testing if the value is correct
            assertEquals("UCSD", startPnt);
        });
    }

    @Test
    public void testAddRouteWithDuplicateName(){
        scenario.onActivity(routeActivity->{
            init(routeActivity);
            rName.setText("Test1");
            start.setText("UCSD");
            btn.performClick();
        });
        scenario.onActivity(routeActivity->{
            init(routeActivity);
            rName.setText("Test1");
            start.setText("UCB");
            btn.performClick();
            // it should still be the same as before because the two route have the duplicate name
            SharedPreferences spfs = routeActivity.getSharedPreferences("all_routes", Context.MODE_PRIVATE);
            String startPnt = spfs.getString("Test1"+"_start_point", "");
            // Testing if the value is correct
            assertEquals("UCSD", startPnt);
        });
    }
}
