package com.creativeprojects.android.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.creativeprojects.android.bakingapp.R;
import com.creativeprojects.android.bakingapp.models.Recipe;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Dan on 03-06-17.
 */

public class StepListWidgetProvider extends AppWidgetProvider
{
    private static String TAG = StepListWidgetProvider.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds)
    {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Log.i(TAG, "On Update widget");

        // update each of the app widgets with the remote adapter
        for (int i = 0; i < appWidgetIds.length; ++i) {

            // Set up the intent that starts the StackViewService, which will
            // provide the views for this collection.
            Intent intent = new Intent(context, StepListViewWidgetService.class);
            // Add the app widget ID to the intent extras.
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            // Instantiate the RemoteViews object for the app widget layout.
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.step_list_widget);
            // Set up the RemoteViews object to use a RemoteViews adapter.
            // This adapter connects
            // to a RemoteViewsService  through the specified intent.
            // This is how you populate the data.
            rv.setRemoteAdapter(appWidgetIds[i], R.id.list_view, intent);

            // The empty view is displayed when the collection has no items.
            // It should be in the same layout used to instantiate the RemoteViews
            // object above.

            //
            // Do additional processing specific to this app widget...
            //

            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
        }
    }

    /*@Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i(TAG, "Receive in provider");

        Recipe recipe = EventBus.getDefault().getStickyEvent(Recipe.class);

        if(recipe == null)
            return;

        // Get the widget id
        int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);

        Intent svcIntent=new Intent(context, StepListViewWidgetService.class);

        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        svcIntent.putExtra("recipe_id", recipe.getId());
        intent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.step_list_widget);

        rv.setRemoteAdapter(R.id.list_view, svcIntent);

        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        widgetManager.updateAppWidget(widgetId, rv);

        Log.i(TAG, "Recipe name: " + recipe.getName() + " id: " + recipe.getId());
    }*/

    /*@Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        Log.i(TAG, "Update in provider");

        SharedPreferences prefs = context.getSharedPreferences("Recipes", MODE_PRIVATE);

        for (int appWidgetId : appWidgetIds)
        {
            // Search if the widget id is on the shared preferences
            if(prefs.contains("Id: " + appWidgetId))
            {
                // Get the Recipe
                Gson gson = new Gson();
                String json = prefs.getString("Recipe", "");
                Recipe recipe = gson.fromJson(json, Recipe.class);

                Intent svcIntent=new Intent(context, StepListViewWidgetService.class);

                svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                svcIntent.putExtra("recipe_id", recipe.getId());
                svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

                RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.step_list_widget);

                rv.setRemoteAdapter(R.id.list_view, svcIntent);

                AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
                widgetManager.updateAppWidget(appWidgetId, rv);

                //updateAppWidget(context,appWidgetManager,appWidgetId, recipe);
            }
        }
        super.onUpdate(context,appWidgetManager,appWidgetIds);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Recipe recipe) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.step_list_widget);

        //set Adapter
        setRemoteAdapter(context,views);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static void setRemoteAdapter(Context context,RemoteViews remoteViews){
        remoteViews.setRemoteAdapter(R.id.list_view,
                                     new Intent(context,StepListWidgetRemoteViewsFactory.class));
    }*/
}
