package com.example.cse110_wwr_team2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.rule.ActivityTestRule;

import com.example.cse110_wwr_team2.fitness.FitnessService;
import com.example.cse110_wwr_team2.fitness.FitnessServiceFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

public class AddRouteUnitTest {

    @Rule
    public ActivityTestRule<MainActivity> rActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    private MainActivity routeActivity = null;


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
    public void testRouteValues() {

        Button btn = routeActivity.findViewById(R.id.add_route);
        Button btn2 = routeActivity.findViewById(R.id.done_add);
        try {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    btn.performClick();
                    // Stop Walk/Add Walk and test that values are not null
                    AutoCompleteTextView rName = routeActivity.findViewById(R.id.route_name);
                    AutoCompleteTextView start = routeActivity.findViewById(R.id.start_point);
                    rName.setText("Test1");
                    start.setText("UCSD");
                    btn2.performClick();

                    assertEquals(rName, "Test1");
                    assertEquals(start, "UCSD");

                    assertNotEquals(rName, "Test2");
                    assertNotEquals(start, "Market St.");

                    ListView routeList = routeActivity.findViewById(R.id.route_list);
                    assertNotNull(routeList);

                    ArrayList<Route> allRoutes = RouteSaver.getAllRoutes(routeActivity);
                    assertEquals(allRoutes.size(), 0);
                    System.out.println("THE SIZE");

                    System.out.println(allRoutes.size());

                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

    @Test
    public void testRouteSize() {

        Button btn = routeActivity.findViewById(R.id.add_route);
        Button btn2 = routeActivity.findViewById(R.id.done_add);
        try {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    ArrayList<Route> allRoutes = RouteSaver.getAllRoutes(routeActivity);
                    System.out.println("THE SIZE");

                    System.out.println(allRoutes.size());
                    btn.performClick();
                    // Stop Walk/Add Walk and test that values are not null
                    AutoCompleteTextView rName = routeActivity.findViewById(R.id.route_name);
                    AutoCompleteTextView start = routeActivity.findViewById(R.id.start_point);
                    rName.setText("Test1");
                    start.setText("UCSD");
                    btn2.performClick();
                    System.out.println("THE SIZE");

                    System.out.println(allRoutes.size());


                    assertEquals(allRoutes.size(), 0);


                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.out.println("NOT");
        }

    }
    @After
    public void tearDown() {
        routeActivity = null;
    }
}
