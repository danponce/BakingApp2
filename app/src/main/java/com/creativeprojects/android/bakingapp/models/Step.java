package com.creativeprojects.android.bakingapp.models;

/**
 * Created by Dan on 23-05-17.
 */

public class Step
{
    int id;
    String shortDescription;
    String description;
    String videoURL;
    String thumbnailURL;

    public String getShortDescription()
    {
        return shortDescription;
    }

    public String getDescription()
    {
        return description;
    }

    public String getVideoURL()
    {
        return videoURL;
    }
}
