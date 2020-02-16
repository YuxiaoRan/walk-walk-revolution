package com.example.cse110_wwr_team2;

import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.content.Context.MODE_PRIVATE;
import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNotEquals;

public class MockActivityUnitTest {

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

        assertNotNull(mockTimer);
        assertNotNull(mockDistance);
        assertNotNull(mockStep);

    }

    @Test
    public void testMockValue() {

        TextView mockTimer = mockActivity.findViewById(R.id.mock_timer);

        Button btn = mockActivity.findViewById(R.id.mock_update_btn);

        try {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    btn.performClick();
                    TextView mockStep = mockActivity.findViewById(R.id.mock_step_count);
                    TextView mockDistance = mockActivity.findViewById(R.id.mock_distance);

                    int mockStepNum = Integer.parseInt(mockStep.getText().toString());
                    float mockDistanceNum = Float.parseFloat(mockDistance.getText().toString());


                    assertEquals(500, mockStepNum);
                    assertEquals(0.21, mockDistanceNum, 0.01);

                    btn.performClick();
                    TextView mockStep2 = mockActivity.findViewById(R.id.mock_step_count);
                    int mockStepNum2 = Integer.parseInt(mockStep2.getText().toString());
                    TextView mockDistance2 = mockActivity.findViewById(R.id.mock_distance);
                    float mockDistanceNum2 = Float.parseFloat(mockDistance2.getText().toString());
                    assertEquals(1000, mockStepNum2);
                    assertEquals(0.42, mockDistanceNum2, 0.01);

                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

    @After
    public void tearDown() {
        mockActivity = null;
    }
}
