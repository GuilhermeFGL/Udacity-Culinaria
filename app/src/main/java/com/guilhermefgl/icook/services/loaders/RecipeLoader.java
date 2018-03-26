package com.guilhermefgl.icook.services.loaders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.guilhermefgl.icook.models.Recipe;
import com.guilhermefgl.icook.services.BakingService;

import java.util.List;

public class RecipeLoader extends AsyncTaskLoader<List<Recipe>> {

    public static final Integer LOADER_ID = 1001;

    private List<Recipe> recipesCached;

    public RecipeLoader(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (recipesCached != null) {
            deliverResult(recipesCached);
        } else {
            forceLoad();
        }
    }

    @Nullable
    @Override
    public List<Recipe> loadInBackground() {
        try {
            return BakingService.getClient().listRecipes().execute().body();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void deliverResult(List<Recipe> data) {
        this.recipesCached = data;
        super.deliverResult(data);
    }
}
