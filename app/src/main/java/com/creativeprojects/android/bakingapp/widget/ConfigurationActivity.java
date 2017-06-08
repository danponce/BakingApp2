package com.creativeprojects.android.bakingapp.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RemoteViews;

import com.creativeprojects.android.bakingapp.R;
import com.creativeprojects.android.bakingapp.adapters.RecipeViewHolder;
import com.creativeprojects.android.bakingapp.databinding.ItemRecipeBinding;
import com.creativeprojects.android.bakingapp.models.Recipe;
import com.creativeprojects.android.bakingapp.network.APIInterface;
import com.creativeprojects.android.bakingapp.network.RetrofitAPIClient;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfigurationActivity extends AppCompatActivity
{
    private final String TAG = ConfigurationActivity.class.getSimpleName();

    RecyclerView mRecipeRecyclerView;

    // In case the widget called this activity, this variable it's for storing the widget id
    int mWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        Bundle extras = getIntent().getExtras();

        if(extras != null)
        {
            mWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

            // If the activity was opened with the widget,
            // then set result to canceled to ensure
            // that if user exits the activity, the widget
            // it's not created
            Intent intent = new Intent();
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mWidgetId);
            setResult(Activity.RESULT_CANCELED, intent);
        }

        mRecipeRecyclerView = (RecyclerView) findViewById(R.id.recipe_recycler_view);
        mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        APIInterface apiInterface = RetrofitAPIClient.getClient().create(APIInterface.class);

        Call<List<Recipe>> recipesCall = apiInterface.getRecipes();

        recipesCall.enqueue(new Callback<List<Recipe>>()
        {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response)
            {
                List<Recipe> recipeList = response.body();

                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
                progressBar.setVisibility(View.GONE);

                if(mWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID)
                {
                    mRecipeRecyclerView.setAdapter(new RecipeRecyclerAdapter(recipeList));
                    return;
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t)
            {
                Log.e(TAG, "Error : " + t.getMessage());
            }
        });
    }

    private class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeViewHolder>
    {
        private String TAG = RecipeRecyclerAdapter.class.getSimpleName();

        private List<Recipe> mRecipeList;

        public RecipeRecyclerAdapter(List<Recipe> recipeList)
        {
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
                Log.i(TAG, "WidgetId : " + mWidgetId);

                if(mWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID)
                {
                    SharedPreferences prefs = getSharedPreferences("Recipes", MODE_PRIVATE);

                    SharedPreferences.Editor prefsEditor = prefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(mRecipe);
                    prefsEditor.putString("Id: " + mWidgetId, json);
                    prefsEditor.commit();

                    StepListWidgetProvider.updateWidget(ConfigurationActivity.this, mWidgetId);

                    Intent intent = new Intent();
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mWidgetId);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        }
    }
}
