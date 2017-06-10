package com.creativeprojects.android.bakingapp;


import android.os.SystemClock;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecipesAndStepsSelectionTest
{

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void recipesAndStepsSelectionTest()
    {
        ViewInteraction recyclerView = onView(withId(R.id.recipe_recycler_view));
        recyclerView.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction recyclerView2 = onView(withId(R.id.step_selection_recycler_view));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction textView = onView(withId(R.id.description_textview));
        textView.check(matches(withText("Recipe Introduction")));

        ViewInteraction button = onView(withId(R.id.next_step_button));
        button.check(matches(isDisplayed()));

        ViewInteraction view = onView(withId(R.id.exo_content_frame));
        view.check(matches(isDisplayed()));

        ViewInteraction appCompatButton = onView(withId(R.id.next_step_button));
        appCompatButton.perform(click());

        String description = "1. Preheat the oven to 350�F. Butter the bottom and sides of a 9\"x13\" pan.";

        ViewInteraction textView2 = onView(withId(R.id.description_textview));
        textView2.check(matches(withText(description)));

        ViewInteraction button2 = onView(withId(R.id.back_step_button));
        button2.check(matches(isDisplayed()));

        ViewInteraction button3 = onView(withId(R.id.next_step_button));
        button3.check(matches(isDisplayed()));

        ViewInteraction button4 = onView(withId(R.id.next_step_button));
        button4.check(matches(isDisplayed()));

        pressBack();

        ViewInteraction recyclerView3 = onView(withId(R.id.step_selection_recycler_view));
        recyclerView3.perform(actionOnItemAtPosition(9, click()));

        ViewInteraction view2 = onView(withId(R.id.exo_content_frame));
        view2.check(matches(isDisplayed()));

        ViewInteraction textView3 = onView(withId(R.id.description_textview));
        textView3.check(matches(withText("9. Cut and serve.")));

        ViewInteraction button5 = onView(withId(R.id.back_step_button));
        button5.check(matches(isDisplayed()));

    }

    private static Matcher<View> childAtPosition(final Matcher<View> parentMatcher, final int position)
    {

        return new TypeSafeMatcher<View>()
        {
            @Override
            public void describeTo(Description description)
            {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view)
            {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent) && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
