package com.guilhermefgl.icook.views.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.Loader;

import com.guilhermefgl.icook.R;
import com.guilhermefgl.icook.databinding.MainActivityBinding;
import com.guilhermefgl.icook.models.Recipe;
import com.guilhermefgl.icook.services.loaders.RecipeLoader;
import com.guilhermefgl.icook.views.BaseActivity;

import java.util.List;

public class MainActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<List<Recipe>> {

    MainActivityBinding mBinding;

    public static void startActivity(BaseActivity activity) {
        activity.startActivity(
                new Intent(activity, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        setSupportActionBar(mBinding.mainToolbar);

        LoaderManager loaderManager = getSupportLoaderManager();
        if (loaderManager.getLoader(RecipeLoader.LOADER_ID) == null) {
            loaderManager.initLoader(RecipeLoader.LOADER_ID, null, this);
        } else {
            loaderManager.restartLoader(RecipeLoader.LOADER_ID, null, this);
        }
    }


    @NonNull
    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
        return new RecipeLoader(MainActivity.this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Recipe>> loader, List<Recipe> data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Recipe>> loader) { }
}
