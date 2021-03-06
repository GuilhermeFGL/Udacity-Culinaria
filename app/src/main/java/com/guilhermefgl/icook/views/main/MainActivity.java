package com.guilhermefgl.icook.views.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.guilhermefgl.icook.R;
import com.guilhermefgl.icook.databinding.ActivityMainBinding;
import com.guilhermefgl.icook.models.entitys.Recipe;
import com.guilhermefgl.icook.services.loaders.RecipeLoader;
import com.guilhermefgl.icook.views.BaseActivity;
import com.guilhermefgl.icook.helpers.SimpleIdlingResource;
import com.guilhermefgl.icook.views.recipe.RecipeActivity;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<ArrayList<Recipe>>,
        RecipeAdapter.EventHandler, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private static final String STATE_RECIPE = MainActivity.class.getName().concat(".STATE_RECIPE");

    private ActivityMainBinding mBinding;
    private RecipeAdapter recipeAdapter;
    private Snackbar errorSnackBar;
    private ArrayList<Recipe> mRecipes;
    private SimpleIdlingResource mIdlingResource;

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

        mIdlingResource = new SimpleIdlingResource();
        errorSnackBar = Snackbar.make(
                mBinding.mainLayout,
                R.string.error_main_connection_msg,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.error_main_connection_action, this);
        ((TextView) errorSnackBar.getView().findViewById(android.support.design.R.id.snackbar_text))
                .setTextColor(getResources().getColor(android.R.color.white));

        int spanCount = 1;
        if (getResources().getBoolean(R.bool.isTablet)) {
            spanCount = 3;
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 2;
        }
        mBinding.mainList.setLayoutManager(new GridLayoutManager(this, spanCount));
        recipeAdapter = new RecipeAdapter(this);
        mBinding.mainList.setAdapter(recipeAdapter);
        mBinding.mainRefresh.setOnRefreshListener(this);

        if(savedInstanceState != null && savedInstanceState.containsKey(STATE_RECIPE)) {
            mRecipes = savedInstanceState.getParcelableArrayList(STATE_RECIPE);
            recipeAdapter.setRecipes(mRecipes);
        } else {
            getRecipes();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelableArrayList(STATE_RECIPE, mRecipes);
        super.onSaveInstanceState(bundle);
    }

    @NonNull
    @Override
    public Loader<ArrayList<Recipe>> onCreateLoader(int id, Bundle args) {
        mBinding.mainLoading.setVisibility(View.VISIBLE);
        if (errorSnackBar.isShown()) {
            errorSnackBar.dismiss();
        }
        return new RecipeLoader(MainActivity.this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> data) {
        mRecipes = data;
        mBinding.mainLoading.setVisibility(View.GONE);
        mBinding.mainRefresh.setRefreshing(false);
        if (data != null) {
            recipeAdapter.setRecipes(data);
        } else {
            errorSnackBar.show();
        }
        mIdlingResource.setIdleState(true);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Recipe>> loader) { }

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
        extras.putParcelable(RecipeActivity.BUNDLE_RECIPE, recipe);
        RecipeActivity.startActivity(this, extras);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        return mIdlingResource;
    }

    private void getRecipes() {
        mIdlingResource.setIdleState(false);
        if (isDeviceConnected()) {
            LoaderManager loaderManager = getSupportLoaderManager();
            if (loaderManager.getLoader(RecipeLoader.LOADER_ID) == null) {
                loaderManager.initLoader(RecipeLoader.LOADER_ID, null, this);
            } else {
                loaderManager.restartLoader(RecipeLoader.LOADER_ID, null, this);
            }
        } else {
            errorSnackBar.show();
        }
    }
}
