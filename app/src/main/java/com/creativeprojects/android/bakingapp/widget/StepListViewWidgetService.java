package com.creativeprojects.android.bakingapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.creativeprojects.android.bakingapp.models.Step;

import java.util.List;

/**
 * Created by Dan on 03-06-17.
 */

public class StepListViewWidgetService extends RemoteViewsService
{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent)
    {
        return new StepListWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
