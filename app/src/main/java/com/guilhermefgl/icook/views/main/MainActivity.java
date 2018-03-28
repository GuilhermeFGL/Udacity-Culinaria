package com.guilhermefgl.icook.views.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.guilhermefgl.icook.views.details.DetailsActivity;
import com.guilhermefgl.icook.R;
import com.guilhermefgl.icook.databinding.ActivityMainBinding;
import com.guilhermefgl.icook.models.Recipe;
import com.guilhermefgl.icook.services.loaders.RecipeLoader;
import com.guilhermefgl.icook.views.BaseActivity;

import java.util.List;

public class MainActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<List<Recipe>>,
        RecipeAdapter.EventHandler, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private ActivityMainBinding mBinding;
    private RecipeAdapter recipeAdapter;
    private Snackbar errorSnackbar;

    public static void startActivity(BaseActivity activity) {
        activity.startActivity(
                new Intent(activity, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(mBinding.mainToolbar);

        errorSnackbar = Snackbar.make(
                mBinding.mainLayout,
                R.string.error_main_connection_msg,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.error_main_connection_action, this);

        mBinding.mainList.setLayoutManager(new GridLayoutManager(this, 1));
        recipeAdapter = new RecipeAdapter(this);
        mBinding.mainList.setAdapter(recipeAdapter);
        mBinding.mainRefresh.setOnRefreshListener(this);

        getRecipes();
    }

    @NonNull
    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
        mBinding.mainLoading.setVisibility(View.VISIBLE);
        if (errorSnackbar.isShown()) {
            errorSnackbar.dismiss();
        }
        return new RecipeLoader(MainActivity.this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Recipe>> loader, List<Recipe> data) {
        mBinding.mainLoading.setVisibility(View.GONE);
        mBinding.mainRefresh.setRefreshing(false);
        if (data != null) {
            recipeAdapter.setRecipes(data);
        } else {
            errorSnackbar.show();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Recipe>> loader) { }

    @Override
    public void onRefresh() {
        getRecipes();
    }

    @Override
    public void onClick(View v) {
        getRecipes();
    }

    @Override
    public void onItemClick(Recipe recipe) {
        Bundle extras = new Bundle();
        extras.putParcelable(DetailsActivity.BUNDLE_RECIPE, recipe);
        DetailsActivity.startActivity(this, extras);
    }

    private void getRecipes() {
        if (isDeviceConnected()) {
            LoaderManager loaderManager = getSupportLoaderManager();
            if (loaderManager.getLoader(RecipeLoader.LOADER_ID) == null) {
                loaderManager.initLoader(RecipeLoader.LOADER_ID, null, this);
            } else {
                loaderManager.restartLoader(RecipeLoader.LOADER_ID, null, this);
            }
        } else {
            errorSnackbar.show();
        }
    }
}
