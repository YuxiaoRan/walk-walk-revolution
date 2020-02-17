package com.example.cse110_wwr_team2;


import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class, true, false);



    @Test
    public void mainActivityTest() {


        Intent i = new Intent();
        i.putExtra("test_label", true);
        mActivityTestRule.launchActivity(i);

        ViewInteraction appCompatEditText = onView(withId(R.id.input_height_ft));
        appCompatEditText.perform(replaceText("5"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(withId(R.id.input_height_in));
        appCompatEditText2.perform(replaceText("5"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(withId(R.id.button_done));
        appCompatButton.perform(click());

        ViewInteraction button2 = onView(withId(R.id.button_start));
        button2.check(matches(isDisplayed()));

        ViewInteraction button3 = onView(withId(R.id.button_route));
        button3.check(matches(isDisplayed()));

        ViewInteraction button4 = onView(withId(R.id.mock_btn));
        button4.check(matches(isDisplayed()));

        // deprecated tests
        /*
        ViewInteraction button = onView(withId(R.id.stop_walking));
        button.check(matches(isDisplayed()));

        ViewInteraction textView = onView(withId(R.id.routeName));
        textView.check(matches(withText("New Route")));

        ViewInteraction textView2 = onView(withId(R.id.Time_Title));
        textView2.check(matches(isDisplayed()));

        ViewInteraction textView3 = onView(withId(R.id.step_title));
        textView3.check(matches(isDisplayed()));

        ViewInteraction textView4 = onView(withId(R.id.distance_title));
        textView4.check(matches(isDisplayed()));

        ViewInteraction textView5 = onView(withId(R.id.distance_title));
        textView5.check(matches(isDisplayed()));

        ViewInteraction appCompatButton3 = onView(withId(R.id.stop_walking));
        appCompatButton3.perform(click());

        ViewInteraction appCompatAutoCompleteTextView = onView(withId(R.id.route_name));
        appCompatAutoCompleteTextView.perform(replaceText("aaaaa"), closeSoftKeyboard());

        ViewInteraction appCompatAutoCompleteTextView2 = onView(withId(R.id.start_point));
        appCompatAutoCompleteTextView2.perform(replaceText("b"), closeSoftKeyboard());

        ViewInteraction floatingActionButton = onView(withId(R.id.done_add));
        floatingActionButton.perform(click());

        ViewInteraction textView6 = onView(withId(android.R.id.text1));
        textView6.check(matches(isDisplayed()));
        */
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
