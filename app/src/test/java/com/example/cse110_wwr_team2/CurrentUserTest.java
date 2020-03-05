package com.example.cse110_wwr_team2;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.cse110_wwr_team2.User.CurrentUserInfo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RuntimeEnvironment;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.robolectric.shadows.ShadowInstrumentation.getInstrumentation;
@RunWith(AndroidJUnit4.class)
public class CurrentUserTest {
    @Rule
    public ActivityTestRule<MockActivity> mActivityTestRule = new ActivityTestRule<>(MockActivity.class);

    private MockActivity mockActivity = null;

    @Before
    public void setUp() {
        mockActivity = mActivityTestRule.getActivity();
    }


    @Test
    public void testCurrentUserSaved(){
        SharedPreferences sharedPreferences = mockActivity.getSharedPreferences("user",Context.MODE_PRIVATE);
        String currentID = sharedPreferences.getString("id", null);
        assertNull(currentID);
    }

}
