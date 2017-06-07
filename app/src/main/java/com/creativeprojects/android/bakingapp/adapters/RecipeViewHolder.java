package com.creativeprojects.android.bakingapp.adapters;

import android.support.v7.widget.RecyclerView;

import com.creativeprojects.android.bakingapp.models.Recipe;
import com.creativeprojects.android.bakingapp.databinding.ItemRecipeBinding;

/**
 * Created by Dan on 24-05-17.
 */

public class RecipeViewHolder extends RecyclerView.ViewHolder
{
    private ItemRecipeBinding mItemRecipeBinding;

    public RecipeViewHolder(ItemRecipeBinding itemRecipeBinding)
    {
        super(itemRecipeBinding.getRoot());

        mItemRecipeBinding = itemRecipeBinding;
    }

    public void bind(Recipe recipe)
    {
        mItemRecipeBinding.setRecipe(recipe);
        mItemRecipeBinding.executePendingBindings();
    }

    public ItemRecipeBinding getBinding()
    {
        return mItemRecipeBinding;
    }
}
