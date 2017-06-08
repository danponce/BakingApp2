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
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;

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

    public StepListWidgetRemoteViewsFactory(Context context, Intent intent)
    {
        mContext = context;
        mIntent = intent;
    }

    @Override
    public void onCreate()
    {
        Log.i(TAG, "In service...");

        int widgetId = mIntent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        SharedPreferences prefs = mContext.getSharedPreferences("Recipes", MODE_PRIVATE);

        // Search if the widget id is on the shared preferences
        if(prefs.contains("Id: " + widgetId))
        {
            // Get the Recipe
            Gson gson = new Gson();
            String json = prefs.getString("Id: " + widgetId, "");
            Type type = new TypeToken<List<Ingredient>>() {}.getType();

            List<Ingredient> ingredientList = gson.fromJson(json, type);

            if(ingredientList == null)
                return;

            mIngredientList = ingredientList;

            Log.i(TAG, "Ingredients size: " + ingredientList.size());

            for (int i = 0; i < mIngredientList.size(); i++)
            {
                Log.i(TAG, " - Ingredient : " + mIngredientList.get(i).getIngredient());
                Log.i(TAG, " -- Measure : " + mIngredientList.get(i).getMeasure());
            }
        }
    }

    @Override
    public void onDataSetChanged()
    {

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
        // Get the ingredient
        Ingredient ingredient = mIngredientList.get(i);

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.item_ingredient);

        rv.setTextViewText(R.id.ingredient_desc, ingredient.getIngredient());
        return rv;
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
