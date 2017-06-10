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
        ViewInteraction recyclerView = onView(allOf(withId(R.id.recipe_recycler_view), withParent(allOf(withId(R.id.activity_main), withParent(withId(android.R.id.content)))), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction recyclerView2 = onView(allOf(withId(R.id.step_selection_recycler_view), withParent(withId(R.id.fragment_container)), isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction textView = onView(withId(R.id.description_textview));
        textView.check(matches(withText("Recipe Introduction")));

        ViewInteraction button = onView(allOf(withId(R.id.next_step_button), childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 2), 0), isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction view = onView(allOf(childAtPosition(allOf(withId(R.id.exo_content_frame), childAtPosition(withId(R.id.playerView), 1)), 0), isDisplayed()));
        view.check(matches(isDisplayed()));

        ViewInteraction appCompatButton = onView(allOf(withId(R.id.next_step_button), withText("Next"), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction textView2 = onView(allOf(withId(R.id.description_textview), withText("1. Preheat the oven to 350�F. Butter the bottom and sides of a 9\"x13\" pan."), childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 0), 1), isDisplayed()));
        textView2.check(matches(withText("1. Preheat the oven to 350�F. Butter the bottom and sides of a 9\"x13\" pan.")));

        ViewInteraction button2 = onView(allOf(withId(R.id.back_step_button), childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 1), 0), isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction button3 = onView(allOf(withId(R.id.next_step_button), childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 1), 1), isDisplayed()));
        button3.check(matches(isDisplayed()));

        ViewInteraction button4 = onView(allOf(withId(R.id.next_step_button), childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 1), 1), isDisplayed()));
        button4.check(matches(isDisplayed()));

        pressBack();

        ViewInteraction recyclerView3 = onView(allOf(withId(R.id.step_selection_recycler_view), withParent(withId(R.id.fragment_container)), isDisplayed()));
        recyclerView3.perform(actionOnItemAtPosition(9, click()));

        ViewInteraction view2 = onView(allOf(childAtPosition(allOf(withId(R.id.exo_content_frame), childAtPosition(withId(R.id.playerView), 2)), 0), isDisplayed()));
        view2.check(matches(isDisplayed()));

        ViewInteraction textView3 = onView(allOf(withId(R.id.description_textview), withText("9. Cut and serve."), childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 1), 1), isDisplayed()));
        textView3.check(matches(withText("9. Cut and serve.")));

        ViewInteraction button5 = onView(allOf(withId(R.id.back_step_button), childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 2), 0), isDisplayed()));
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
