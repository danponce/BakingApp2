package com.creativeprojects.android.bakingapp.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

import com.creativeprojects.android.bakingapp.models.Step;

import java.util.List;

/**
 * Created by Dan on 03-06-17.
 */

public class StepListViewWidgetService extends RemoteViewsService
{
    private static String TAG = StepListViewWidgetService.class.getSimpleName();

    @Override
    public void onCreate()
    {
        Log.i(TAG, "Service created");
        super.onCreate();
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent)
    {
        return new StepListWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
