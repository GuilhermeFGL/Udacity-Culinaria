package com.guilhermefgl.icook.views.recipe;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.guilhermefgl.icook.R;
import com.guilhermefgl.icook.databinding.ActivityRecipeBinding;
import com.guilhermefgl.icook.models.entitys.Recipe;
import com.guilhermefgl.icook.services.tasks.InsertCurrentWidgetRecipeTask;
import com.guilhermefgl.icook.views.BaseActivity;
import com.guilhermefgl.icook.views.widget.IngredientsWidgetProvider;

public class RecipeActivity extends BaseActivity
        implements View.OnClickListener, InsertCurrentWidgetRecipeTask.SaveRecipeCallBack {

    public static final String BUNDLE_RECIPE =  RecipeActivity.class.getName().concat(".BUNDLE_RECIPE");

    private Recipe mRecipe;

    public static void startActivity(BaseActivity activity, Bundle extras) {
        activity.startActivity(new Intent(activity, RecipeActivity.class).putExtras(extras));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRecipeBinding biding =
                DataBindingUtil.setContentView(this, R.layout.activity_recipe);
        setSupportActionBar(biding.recipeToolbar);

        if (getIntent().getExtras() != null && getIntent().hasExtra(BUNDLE_RECIPE)) {
            mRecipe = getIntent().getParcelableExtra(BUNDLE_RECIPE);

            biding.detailsPager.setAdapter(
                    new ViewAdapter(getSupportFragmentManager(), this, mRecipe));
            biding.detailsTab.setupWithViewPager(biding.detailsPager);
            biding.fab.setOnClickListener(this);

            if (getResources().getBoolean(R.bool.isTablet)) {
                biding.appBar.setExpanded(false);
            }

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(mRecipe.getName());
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        } else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        if (mRecipe != null) {
            new InsertCurrentWidgetRecipeTask(getApplicationContext(), this).execute(mRecipe);
        } else {
            Toast.makeText(this, R.string.recipe_widget_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSave(Recipe recipe) {
        if (recipe != null) {
            IngredientsWidgetProvider.update(this);
            Toast.makeText(this, R.string.recipe_widget_success, Toast.LENGTH_LONG).show();
        }
    }
}
