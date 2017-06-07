package com.creativeprojects.android.bakingapp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dan on 22-05-17.
 */

public class RetrofitAPIClient
{
    private static Retrofit sRetrofit = null;
    private static String BASE_URL = "http://go.udacity.com/";

    public static Retrofit getClient()
    {
        sRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return sRetrofit;
    }
}
