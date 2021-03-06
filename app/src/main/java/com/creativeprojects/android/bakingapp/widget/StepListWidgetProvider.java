package com.creativeprojects.android.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

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
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds)
    {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Log.i(TAG, "On Update widget");

        // update each of the app widgets with the remote adapter
        for (int i = 0; i < appWidgetIds.length; ++i)
        {
            updateWidget(context, appWidgetIds[i]);
        }
    }

    public static void updateWidget(Context context, int widgetId)
    {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        // Set up the intent that starts the StackViewService, which will
        // provide the views for this collection.
        Intent intent = new Intent(context, StepListViewWidgetService.class);
        // Add the app widget ID to the intent extras.
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        // Instantiate the RemoteViews object for the app widget layout.
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.step_list_widget);
        // Set up the RemoteViews object to use a RemoteViews adapter.
        // This adapter connects
        // to a RemoteViewsService  through the specified intent.
        // This is how you populate the data.
        rv.setRemoteAdapter(R.id.list_view, intent);

        // The empty view is displayed when the collection has no items.
        // It should be in the same layout used to instantiate the RemoteViews
        // object above.

        //
        // Do additional processing specific to this app widget...
        //

        appWidgetManager.updateAppWidget(widgetId, rv);

    }
}
