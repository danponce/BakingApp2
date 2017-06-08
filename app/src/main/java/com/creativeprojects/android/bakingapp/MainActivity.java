package com.creativeprojects.android.bakingapp;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RemoteViews;

import com.creativeprojects.android.bakingapp.adapters.RecipeRecyclerAdapter;
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


public class MainActivity extends AppCompatActivity
{
    private final String TAG = MainActivity.class.getSimpleName();

    RecyclerView mRecipeRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.grid_recyclerview) != null)
        {
            mRecipeRecyclerView = (RecyclerView) findViewById(R.id.grid_recyclerview);
            mRecipeRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }
        else
        {
            mRecipeRecyclerView = (RecyclerView) findViewById(R.id.recipe_recycler_view);
            mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

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

                mRecipeRecyclerView.setAdapter(new RecipeRecyclerAdapter(MainActivity.this, recipeList));
                return;
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t)
            {
                Log.e(TAG, "Error : " + t.getMessage());
            }
        });
    }
}
