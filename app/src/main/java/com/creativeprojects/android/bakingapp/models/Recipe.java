package com.creativeprojects.android.bakingapp.models;

import java.util.List;

/**
 * Created by Dan on 22-05-17.
 */

public class Recipe
{
    int id;
    String name;
    int servings;
    String image;
    List<Ingredient> ingredients;
    List<Step> steps;

    public String getName()
    {
        return name;
    }

    public List<Step> getSteps()
    {
        return steps;
    }

    public int getId()
    {
        return id;
    }

    public List<Ingredient> getIngredients()
    {
        return ingredients;
    }
}
