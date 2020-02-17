package com.example.cse110_wwr_team2;

import android.content.Intent;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.shadows.ShadowToast;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class InputMockTimeActivityUnitTest {

    @Rule
    public ActivityTestRule<InputMockTime> tActivityTestRule = new ActivityTestRule<>(InputMockTime.class);
    private InputMockTime timeActivity = null;


    @Before
    public void setUp() {
        timeActivity = tActivityTestRule.getActivity();

    }

    @Test
    public void testMockTimerLaunch() {
        EditText time = timeActivity.findViewById(R.id.mock_curr_time);

        assertEquals(time.getText().toString(), "");
    }

    @Test
    public void testInvalidTimeFormat() {
        Button btn = timeActivity.findViewById(R.id.mock_time_btn);
        EditText time = timeActivity.findViewById(R.id.mock_curr_time);
        time.setText("17:44:00");
        btn.performClick();
        assertEquals(ShadowToast.getTextOfLatestToast(), "Check the format of your input");
    }

    @Test
    public void testInvalidTimeFormatLetters() {
        Button btn = timeActivity.findViewById(R.id.mock_time_btn);
        EditText time = timeActivity.findViewById(R.id.mock_curr_time);
        time.setText("abc");
        btn.performClick();
        assertEquals(ShadowToast.getTextOfLatestToast(), "Check the format of your input");
    }

    @Test
    public void testInvalidTimeFormatSymbols() {
        Button btn = timeActivity.findViewById(R.id.mock_time_btn);
        EditText time = timeActivity.findViewById(R.id.mock_curr_time);
        time.setText("@+_");
        btn.performClick();
        assertEquals(ShadowToast.getTextOfLatestToast(), "Check the format of your input");
    }

    @Test
    public void testTimeTooSmall() {
        long base_millis = System.currentTimeMillis();
        LocalTime base = LocalDateTime.ofInstant(Instant.ofEpochMilli(base_millis),
                ZoneId.systemDefault()).toLocalTime();


        Button btn = timeActivity.findViewById(R.id.mock_time_btn);
        EditText time = timeActivity.findViewById(R.id.mock_curr_time);
        time.setText("1");

        btn.performClick();
        // Extract out the main message since time will always differ
        int messSize = ShadowToast.getTextOfLatestToast().length();
        String mainMess = ShadowToast.getTextOfLatestToast().substring(0, messSize - 8);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        assertEquals(mainMess, "Please enter a time later than ");
    }

    @After
    public void tearDown() {
        timeActivity = null;
    }
}
