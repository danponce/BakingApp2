package com.creativeprojects.android.bakingapp;

import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Dan on 09-06-17.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityScreenTest
{
    public static final String RECIPE_NAME = "Nutella Pie";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickRecyclerViewItem_OpensRecipeDescriptionActivity() {

        // Let Retrofit get the recipes and teh recycler view sets
        SystemClock.sleep(1000);

        // Click item at position 0
        onView(withId(R.id.recipe_recycler_view))
                .perform(actionOnItemAtPosition(0, click()));


        // Checks that the RecipeDescriptionActivity opens with the correct recipe name displayed
        onView(withId(R.id.recipe_name_textview)).check(matches(withText(RECIPE_NAME)));


    }
}
