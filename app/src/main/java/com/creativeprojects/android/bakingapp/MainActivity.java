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

    // In case the widget called this activity, this variable it's for storing the widget id
    int mWidgetId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        /*AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        RemoteViews views = new RemoteViews(this.getPackageName(),
                                            R.layout.step_list_widget);

        appWidgetManager.updateAppWidget(mWidgetId, views);

        Intent intent = new Intent();
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mWidgetId);
        setResult(Activity.RESULT_OK, intent);
        finish();*/

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

    public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeViewHolder>
    {
        private String TAG = RecipeRecyclerAdapter.class.getSimpleName();

        private List<Recipe> mRecipeList;
        private Context mContext;
        private int mWidgetId = -1;

        public RecipeRecyclerAdapter(Context context, List<Recipe> recipeList, int widgetId)
        {
            mContext = context;
            mRecipeList = recipeList;
            mWidgetId = widgetId;
        }

        public RecipeRecyclerAdapter(Context context, List<Recipe> recipeList)
        {
            mContext = context;
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
                if(mWidgetId != -1)
                {
                    SharedPreferences prefs = getSharedPreferences("Recipes", MODE_PRIVATE);

                    SharedPreferences.Editor prefsEditor = prefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(mRecipe);
                    prefsEditor.putString("Id: " + mWidgetId, json);
                    prefsEditor.commit();

                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(MainActivity.this);

                    RemoteViews views = new RemoteViews(getPackageName(),
                                                        R.layout.step_list_widget);

                    appWidgetManager.updateAppWidget(mWidgetId, views);

                    Intent intent = new Intent();
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mWidgetId);
                    setResult(Activity.RESULT_OK, intent);
                    finish();



                /*Intent updateIntent = new Intent(mContext, StepListWidgetProvider.class);

                EventBus.getDefault().postSticky(mRecipe);

                mContext.sendBroadcast(updateIntent);*/

                    return;
                }

                Intent intent = new Intent(MainActivity.this, RecipeDescriptionActivity.class);

                // Send the recipe object
                EventBus.getDefault().postSticky(mRecipeList.get(mRecipePosition));

                startActivity(intent);
            }
        }
    }
}
