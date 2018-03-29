package com.guilhermefgl.icook.services;

import com.guilhermefgl.icook.BuildConfig;
import com.guilhermefgl.icook.models.Recipe;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class BakingService {

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    private static final OkHttpClient httpClient;
    static {
        httpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(
                        BuildConfig.DEBUG ?
                                HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE)
                ).build();
    }

    private BakingService() { }

    public static BakingServiceClient getClient() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BakingServiceClient.class);
    }

    public interface BakingServiceClient {

        @GET("baking.json")
        Call<ArrayList<Recipe>> listRecipes();
    }
}
