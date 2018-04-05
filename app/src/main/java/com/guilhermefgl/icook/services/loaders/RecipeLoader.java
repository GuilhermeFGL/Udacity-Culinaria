package com.guilhermefgl.icook.services.loaders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.guilhermefgl.icook.models.entitys.Recipe;
import com.guilhermefgl.icook.services.BakingService;

import java.util.ArrayList;

public class RecipeLoader extends AsyncTaskLoader<ArrayList<Recipe>> {

    public static final Integer LOADER_ID = 1001;

    private ArrayList<Recipe> mRecipesCached;

    public RecipeLoader(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (mRecipesCached != null) {
            deliverResult(mRecipesCached);
        } else {
            forceLoad();
        }
    }

    @Nullable
    @Override
    public ArrayList<Recipe> loadInBackground() {
        try {
            return BakingService.getClient().listRecipes().execute().body();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void deliverResult(ArrayList<Recipe> data) {
        mRecipesCached = data;
        super.deliverResult(data);
    }
}
