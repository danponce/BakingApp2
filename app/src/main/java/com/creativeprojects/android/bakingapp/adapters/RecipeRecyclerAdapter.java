package com.creativeprojects.android.bakingapp.adapters;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;

import com.creativeprojects.android.bakingapp.MainActivity;
import com.creativeprojects.android.bakingapp.R;
import com.creativeprojects.android.bakingapp.models.Recipe;
import com.creativeprojects.android.bakingapp.RecipeDescriptionActivity;
import com.creativeprojects.android.bakingapp.databinding.ItemRecipeBinding;
import com.creativeprojects.android.bakingapp.widget.StepListWidgetProvider;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Dan on 24-05-17.
 *
 * Recycler View adapter for showing a list of recipes
 */

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeViewHolder>
{
    private static String TAG = RecipeRecyclerAdapter.class.getSimpleName();

    private List<Recipe> mRecipeList;
    private MainActivity mMainActivity;

    public RecipeRecyclerAdapter(MainActivity mainActivity, List<Recipe> recipeList)
    {
        mMainActivity = mainActivity;
        mRecipeList = recipeList;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        ItemRecipeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                                                            R.layout.item_recipe,
                                                            parent,
                                                            false);

        return new RecipeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position)
    {
        Recipe recipe = mRecipeList.get(position);

        holder.getBinding().recipeLinearContainer.setOnClickListener(new RecipeClickListener(position, recipe));

        holder.bind(recipe);
    }

    @Override
    public int getItemCount()
    {
        return mRecipeList.size();
    }

    class RecipeClickListener implements View.OnClickListener
    {
        int mRecipePosition;
        Recipe mRecipe;

        public RecipeClickListener(int recipePosition, Recipe recipe)
        {
            mRecipePosition = recipePosition;
            mRecipe = recipe;
        }

        @Override
        public void onClick(View view)
        {
            Intent intent = new Intent(mMainActivity, RecipeDescriptionActivity.class);

            // Send the recipe object
            EventBus.getDefault().postSticky(mRecipeList.get(mRecipePosition));

            mMainActivity.startActivity(intent);
        }
    }
}
