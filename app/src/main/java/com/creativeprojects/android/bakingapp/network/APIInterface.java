package com.creativeprojects.android.bakingapp.network;

import com.creativeprojects.android.bakingapp.models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Dan on 22-05-17.
 */

public interface APIInterface
{
    @GET("android-baking-app-json")
    Call<List<Recipe>> getRecipes();
}
