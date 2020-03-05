package com.example.cse110_wwr_team2;

import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.Context.MODE_PRIVATE;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class MyRouteTest {

    @Rule
    public ActivityTestRule<MockActivity> mActivityTestRule = new ActivityTestRule<>(MockActivity.class);

    private MockActivity mockActivity = null;

    @Before
    public void setUp() {
        mockActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void testMockLaunch() {
        TextView mockStep = mockActivity.findViewById(R.id.mock_step_count);
        TextView mockDistance = mockActivity.findViewById(R.id.mock_distance);

        TextView mockTimer = mockActivity.findViewById(R.id.mock_timer);

        int mockStepNum = Integer.parseInt(mockStep.getText().toString());
        float mockDistanceNum = Float.parseFloat(mockDistance.getText().toString());

        assertNotNull(mockTimer);
        assertNotNull(mockDistance);
        assertNotNull(mockStep);

    }

    @Test
    public void testInitialValues() {
        TextView mockStep = mockActivity.findViewById(R.id.mock_step_count);
        TextView mockDistance = mockActivity.findViewById(R.id.mock_distance);
        TextView mockTimer = mockActivity.findViewById(R.id.mock_timer);

        String timer = mockTimer.getText().toString();
        int mockStepNum = Integer.parseInt(mockStep.getText().toString());
        float mockDistanceNum = Float.parseFloat(mockDistance.getText().toString());

        assertEquals(timer, "");
        assertEquals(mockStepNum, 0);
        assertEquals(mockDistanceNum, 0.0, 0.01);
    }

    @Test
    public void testMockValue() {

        TextView mockTimer = mockActivity.findViewById(R.id.mock_timer);

        Button btn = mockActivity.findViewById(R.id.mock_update_btn);

        // Mock the user height to get the distance, here the user will be 5ft. 5in.
        SharedPreferences spfs = mockActivity.getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = spfs.edit();
        editor.putInt("height", 65);
        editor.apply();

        TextView mockStep = mockActivity.findViewById(R.id.mock_step_count);
        TextView mockDistance = mockActivity.findViewById(R.id.mock_distance);
        btn.performClick();
        int mockStepNum = Integer.parseInt(mockStep.getText().toString());
        float mockDistanceNum = Float.parseFloat(mockDistance.getText().toString());

        assertEquals(500, mockStepNum);
        assertEquals(0.21, mockDistanceNum, 0.01);

        // 2nd click
        btn.performClick();
        TextView mockStep2 = mockActivity.findViewById(R.id.mock_step_count);
        int mockStepNum2 = Integer.parseInt(mockStep2.getText().toString());
        TextView mockDistance2 = mockActivity.findViewById(R.id.mock_distance);
        float mockDistanceNum2 = Float.parseFloat(mockDistance2.getText().toString());
        assertEquals(1000, mockStepNum2);
        assertEquals(0.42, mockDistanceNum2, 0.01);
    }

    @After
    public void tearDown() {
        mockActivity = null;
    }
}
