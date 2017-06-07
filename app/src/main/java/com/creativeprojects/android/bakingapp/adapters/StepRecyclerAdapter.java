package com.creativeprojects.android.bakingapp.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.creativeprojects.android.bakingapp.R;
import com.creativeprojects.android.bakingapp.StepSelectionFragment;
import com.creativeprojects.android.bakingapp.databinding.ItemStepBinding;
import com.creativeprojects.android.bakingapp.models.Step;

import java.util.List;

/**
 * Created by Dan on 24-05-17.
 */

public class StepRecyclerAdapter extends RecyclerView.Adapter<StepViewHolder>
{
    private List<Step> mStepList;
    private StepSelectionFragment.OnStepSelectionFragmentListener mListener;

    public StepRecyclerAdapter(List<Step> stepList, StepSelectionFragment.OnStepSelectionFragmentListener listener)
    {
        mStepList = stepList;
        mListener = listener;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        ItemStepBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                                                                              R.layout.item_step,
                                                                              parent,
                                                                              false);
        return new StepViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position)
    {
        Step step = mStepList.get(position);

        holder.getBinding().stepLinearContainer.setOnClickListener(new StepClickListener(step, position));

        holder.bind(step);
    }

    @Override
    public int getItemCount()
    {
        return mStepList.size();
    }

    private class StepClickListener implements View.OnClickListener
    {
        private Step mStep;
        private int mPosition;

        public StepClickListener(Step step, int position)
        {
            mStep = step;
            mPosition = position;
        }

        @Override
        public void onClick(View view)
        {
            mListener.onStepSelectionListener(mStep, mPosition);
        }
    }

}
