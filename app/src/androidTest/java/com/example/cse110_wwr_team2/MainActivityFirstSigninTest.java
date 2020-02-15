package com.example.cse110_wwr_team2;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityFirstSigninTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class, true, false);

    @Test
    public void testMainActivityFirstSignin() throws InterruptedException {
        mActivityTestRule.launchActivity(null);



        SharedPreferences spfs = mActivityTestRule.getActivity()
                .getSharedPreferences("user", Context.MODE_PRIVATE);
        spfs.edit().clear().commit();

        Thread.sleep(1000);
        ViewInteraction button = onView(withId(R.id.button_done));
        button.check(matches(isDisplayed()));

        ViewInteraction appCompatEditText = onView(withId(R.id.input_height));
        appCompatEditText.perform(replaceText("175"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(withId(R.id.button_done));
        appCompatButton.perform(click());

        ViewInteraction button2 = onView(withId(R.id.button_start));
        button2.check(matches(isDisplayed()));

        ViewInteraction button3 = onView(withId(R.id.button_route));
        button3.check(matches(isDisplayed()));

        spfs.edit().clear().commit();
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
