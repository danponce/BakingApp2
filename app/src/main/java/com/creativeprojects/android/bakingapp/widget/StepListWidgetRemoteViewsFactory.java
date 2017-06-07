package com.creativeprojects.android.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.creativeprojects.android.bakingapp.R;
import com.creativeprojects.android.bakingapp.models.Ingredient;
import com.creativeprojects.android.bakingapp.models.Recipe;
import com.creativeprojects.android.bakingapp.models.Step;
import com.creativeprojects.android.bakingapp.network.APIInterface;
import com.creativeprojects.android.bakingapp.network.RetrofitAPIClient;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Dan on 03-06-17.
 */

public class StepListWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory
{
    private static String TAG = StepListWidgetRemoteViewsFactory.class.getSimpleName();
    private Context mContext;
    private List<Ingredient> mIngredientList;
    private Intent mIntent;
    private int mRecipeId;

    public StepListWidgetRemoteViewsFactory(Context context, Intent intent)
    {
        mContext = context;
        mIntent = intent;
    }

    @Override
    public void onCreate()
    {
        int widgetId = mIntent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        Log.i(TAG, "In service...");

        SharedPreferences prefs = mContext.getSharedPreferences("Recipes", MODE_PRIVATE);

        // Search if the widget id is on the shared preferences
        if(prefs.contains("Id: " + widgetId))
        {
            // Get the Recipe
            Gson gson = new Gson();
            String json = prefs.getString("Recipe", "");
            Recipe recipe = gson.fromJson(json, Recipe.class);

            mRecipeId = recipe.getId();

            Log.i(TAG, "Recipe in service : " + recipe.getId());
        }
    }

    @Override
    public void onDataSetChanged()
    {
        APIInterface apiInterface = RetrofitAPIClient.getClient().create(APIInterface.class);

        Call<List<Recipe>> recipesCall = apiInterface.getRecipes();

        try
        {
            List<Recipe> recipeList = recipesCall.execute().body();

            for (Recipe recipe : recipeList)
            {
                if(recipe.getId() == mRecipeId)
                    mIngredientList = recipe.getIngredients();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy()
    {

    }

    @Override
    public int getCount()
    {
        return mIngredientList.size();
    }

    @Override
    public RemoteViews getViewAt(int i)
    {
        // Get the step
        Ingredient ingredient = mIngredientList.get(i);

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.item_step);

        rv.setTextViewText(R.id.ingredient_desc, ingredient.getIngredient());
        return null;
    }

    @Override
    public RemoteViews getLoadingView()
    {
        return null;
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }
}
