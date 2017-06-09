package com.creativeprojects.android.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.creativeprojects.android.bakingapp.adapters.RecipeRecyclerAdapter;
import com.creativeprojects.android.bakingapp.models.Recipe;
import com.creativeprojects.android.bakingapp.network.APIInterface;
import com.creativeprojects.android.bakingapp.network.RetrofitAPIClient;

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

        mRecipeRecyclerView = (RecyclerView) findViewById(R.id.recipe_recycler_view);

        if(findViewById(R.id.test_view) != null)
        {
            mRecipeRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }
        else
        {
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
