package com.creativeprojects.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
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

    // In case the widget called this activity, this variable it's for storing the widget id
    int mWidgetId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // If the activity was opened with the widget,
        // then set result to canceled to ensure
        // that if user exits the activity, the widget
        // it's not created

        Bundle extras = getIntent().getExtras();

        if(extras != null)
        {
            mWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // Figure out what kind of display we have
        int screenLayout = getResources().getConfiguration().screenLayout;

        if ((screenLayout & Configuration.SCREENLAYOUT_SIZE_SMALL) == Configuration.SCREENLAYOUT_SIZE_SMALL)
            Log.i("Info", "Screen size is Small");
        else if ((screenLayout & Configuration.SCREENLAYOUT_SIZE_NORMAL) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
            Log.i( "Info", "Screen size is Normal");
        else if ((screenLayout & Configuration.SCREENLAYOUT_SIZE_LARGE) == Configuration.SCREENLAYOUT_SIZE_LARGE)
            Log.i("Info", "Screen size is Large");

        if ((screenLayout & Configuration.SCREENLAYOUT_LONG_YES) == Configuration.SCREENLAYOUT_LONG_YES)
            Log.i("Info", "Screen size is Long");

        // Get the metrics
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int heightPixels = metrics.heightPixels;
        int widthPixels = metrics.widthPixels;
        int densityDpi = metrics.densityDpi;
        float density = metrics.density;
        float scaledDensity = metrics.scaledDensity;
        float xdpi = metrics.xdpi;
        float ydpi = metrics.ydpi;

        Log.i("Info", "Screen W x H pixels: " + widthPixels  + " x " + heightPixels);
        Log.i("Info", "Screen X x Y dpi: " + xdpi + " x " + ydpi);
        Log.i("Info", "density = " + density + "  scaledDensity = " + scaledDensity +
                "  densityDpi = " + densityDpi);

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

                if(mWidgetId != -1)
                {
                    mRecipeRecyclerView.setAdapter(new RecipeRecyclerAdapter(MainActivity.this, recipeList, mWidgetId));
                    return;
                }

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
