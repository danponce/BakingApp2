package com.creativeprojects.android.bakingapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.creativeprojects.android.bakingapp.databinding.ItemStepBinding;
import com.creativeprojects.android.bakingapp.models.Step;

/**
 * Created by Dan on 24-05-17.
 */

public class StepViewHolder extends RecyclerView.ViewHolder
{
    private ItemStepBinding mItemStepBinding;

    public StepViewHolder(ItemStepBinding itemStepBinding)
    {
        super(itemStepBinding.getRoot());
        mItemStepBinding = itemStepBinding;
    }

    public void bind(Step step)
    {
        mItemStepBinding.setStep(step);
        mItemStepBinding.executePendingBindings();
    }

    public ItemStepBinding getBinding()
    {
        return mItemStepBinding;
    }
}
