package com.example.cse110_wwr_team2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;

import org.junit.*;
import org.junit.runner.RunWith;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public class InputHeightActivityUnitTest {

    private Intent intent;
    private static final Integer MY_HEIGHT = 5;
    private static final int EXPECTED_HEIGHT = 65;
    private static final Integer INVALID_HEIGHT = -2;
    private static final String WRONG_FORMAT = "abc";
    private static final int DEFAULT_HEIGHT = 0;
    private static final boolean DEFAULT_FIRST_LOGIN = true;

    @Before
    public void setUp() {
        intent = new Intent(ApplicationProvider.getApplicationContext(), InputHeightActivity.class);
    }

    // automate test
    public void automatedTestInputHeight(
            Object input, int expectedHeight, boolean expectedFirstLogin) {
        ActivityScenario<InputHeightActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {

            // edit text and click button
            EditText heightInputFt = activity.findViewById(R.id.input_height_ft);
            EditText heightInputIn = activity.findViewById(R.id.input_height_in);
            Button btnDone = activity.findViewById(R.id.button_done);
            heightInputFt.setText(input.toString());
            heightInputIn.setText(input.toString());
            btnDone.performClick();

            // test shared prefs
            SharedPreferences spfs = activity.getSharedPreferences("user",
                    Context.MODE_PRIVATE);
            int storedHeight = spfs.getInt("height", DEFAULT_HEIGHT);
            boolean storedFirstLogin = spfs.getBoolean("firstLogin", DEFAULT_FIRST_LOGIN);
            assertThat(storedHeight).isEqualTo(expectedHeight);
            assertThat(storedFirstLogin).isEqualTo(expectedFirstLogin);

            // clear shared prefs after test
            SharedPreferences.Editor editor = spfs.edit();
            editor.clear().commit();
        });
    }

    @Test
    public void testInputHeight() {
        // input 175
        automatedTestInputHeight(MY_HEIGHT, EXPECTED_HEIGHT, false);

        // invalid inputs: non-positive height and wrong format
        automatedTestInputHeight(INVALID_HEIGHT, DEFAULT_HEIGHT, DEFAULT_FIRST_LOGIN);
        automatedTestInputHeight(WRONG_FORMAT, DEFAULT_HEIGHT, DEFAULT_FIRST_LOGIN);
    }

}
