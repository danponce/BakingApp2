package com.creativeprojects.android.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.creativeprojects.android.bakingapp.models.Recipe;
import com.creativeprojects.android.bakingapp.models.Step;

import org.greenrobot.eventbus.EventBus;

public class RecipeDescriptionActivity extends AppCompatActivity implements StepSelectionFragment.OnStepSelectionFragmentListener
{
    Recipe mRecipe;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_description);

        mRecipe = EventBus.getDefault().getStickyEvent(Recipe.class);

        if(findViewById(R.id.description_fragment_container) != null)
            mTwoPane = true;
        else
        {
            TextView recipeNameTextView = (TextView) findViewById(R.id.recipe_name_textview);

            // Set the name of the recipe chosen
            recipeNameTextView.setText(mRecipe.getName());
        }

        if(savedInstanceState == null)
        {
            /*StepSelectionFragment stepSelectionFragment = StepSelectionFragment.newInstance(recipe);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, stepSelectionFragment)
                    .addToBackStack(null)
                    .commit();*/
        }
    }

    @Override
    public void onStepSelectionListener(Step step, int position)
    {
        if(mTwoPane)
        {
            replaceStepDescriptionFragment(step);
        }
        else
        {
            Intent intent = new Intent(RecipeDescriptionActivity.this, StepDescriptionActivity.class);
            intent.putExtra(StepDescriptionActivity.STEP_POSITION, position);

            EventBus.getDefault().postSticky(mRecipe);

            startActivity(intent);
        }

        /*StepDescriptionFragment stepDescriptionFragment = StepDescriptionFragment.newInstance(step);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, stepDescriptionFragment)
                .addToBackStack(null)
                .commit();*/
    }

    /**
     * Replaces the actual fragment container
     * with a new StepDescriptionFragment
     * @param step      The step selected
     */
    private void replaceStepDescriptionFragment(Step step)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();

        StepDescriptionFragment fragment = StepDescriptionFragment.newInstance(0, 0, step);
        fragment.setTwoPane(true);

        fragmentManager.beginTransaction()
                .replace(R.id.description_fragment_container, fragment)
                .commit();
    }
}
