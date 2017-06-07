package com.creativeprojects.android.bakingapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.creativeprojects.android.bakingapp.models.Recipe;
import com.creativeprojects.android.bakingapp.models.Step;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class StepDescriptionActivity extends AppCompatActivity implements StepDescriptionFragment.OneStepBackButtonListener,
        StepDescriptionFragment.OneStepForwardButtonListener
{
    public static final String STEP_POSITION = "step_position";

    Recipe mRecipe;
    int mStepPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_description);

        Bundle extras = getIntent().getExtras();

        if(extras != null)
        {
            mStepPosition = extras.getInt(STEP_POSITION);
        }

        mRecipe = EventBus.getDefault().getStickyEvent(Recipe.class);

        if(savedInstanceState == null)
        {
            // Get list of step related to the recipe
            List<Step> stepList = mRecipe.getSteps();

            Fragment stepDescriptionFragment = StepDescriptionFragment.newInstance(mStepPosition, stepList.size(), stepList.get(mStepPosition));

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.description_container,
                             stepDescriptionFragment)
                    .commit();
        }
    }

    @Override
    public void goOneStepBack(int actualStepPosition)
    {
        // Update the position
        mStepPosition = actualStepPosition;

        // Get list of step related to the recipe
        List<Step> stepList = mRecipe.getSteps();

        mStepPosition--;

        Fragment stepDescriptionFragment = StepDescriptionFragment.newInstance(mStepPosition, stepList.size(), stepList.get(mStepPosition));

        replaceFragment(stepDescriptionFragment);
    }

    @Override
    public void goOneStepForward(int actualStepPosition)
    {
        // Update the position
        mStepPosition = actualStepPosition;

        // Get list of step related to the recipe
        List<Step> stepList = mRecipe.getSteps();

        mStepPosition++;

        Fragment stepDescriptionFragment = StepDescriptionFragment.newInstance(mStepPosition, stepList.size(), stepList.get(mStepPosition));

        replaceFragment(stepDescriptionFragment);
    }

    /**
     * Replace the description fragment
     * container with a new step description
     * fragment
     * @param newFragment       The new StepDescriptionFragment
     */
    private void replaceFragment(Fragment newFragment)
    {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.description_container,
                         newFragment)
                .commit();
    }
}
