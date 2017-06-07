package com.creativeprojects.android.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.creativeprojects.android.bakingapp.adapters.StepRecyclerAdapter;
import com.creativeprojects.android.bakingapp.models.Recipe;
import com.creativeprojects.android.bakingapp.models.Step;

import org.greenrobot.eventbus.EventBus;

public class StepSelectionFragment extends Fragment
{
    Recipe mRecipe;

    private OnStepSelectionFragmentListener mListener;

    public StepSelectionFragment()
    {
        // Required empty public constructor
    }

    public static StepSelectionFragment newInstance(Recipe recipe)
    {
        StepSelectionFragment fragment = new StepSelectionFragment();
        fragment.mRecipe = recipe;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {

        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnStepSelectionFragmentListener)
        {
            mListener = (OnStepSelectionFragmentListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString() + " must implement OnStepDescriptionFragmentListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        EventBus.getDefault().postSticky(mRecipe);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Recipe savedRecipe = EventBus.getDefault().getStickyEvent(Recipe.class);

        if(savedRecipe != null)
            mRecipe = savedRecipe;

        View view = inflater.inflate(R.layout.fragment_step_selection, container, false);

        RecyclerView recyclerView  = (RecyclerView) view.findViewById(R.id.step_selection_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new StepRecyclerAdapter(mRecipe.getSteps(), mListener));

        return view;
    }

    public interface OnStepSelectionFragmentListener
    {
        void onStepSelectionListener(Step step, int position);
    }
}
