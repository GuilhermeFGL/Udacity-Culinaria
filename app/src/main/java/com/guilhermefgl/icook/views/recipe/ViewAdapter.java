package com.guilhermefgl.icook.views.recipe;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.guilhermefgl.icook.R;
import com.guilhermefgl.icook.models.Recipe;

public class ViewAdapter extends FragmentPagerAdapter {

    private String[] tabTitles;
    private Recipe mRecipe;

    ViewAdapter(FragmentManager fm, Context context, Recipe recipe) {
        super(fm);
        tabTitles = context.getResources().getStringArray(R.array.details_tab_title);
        mRecipe = recipe;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Bundle stepArguments = new Bundle();
                stepArguments.putParcelableArrayList(
                        StepListFragment.BUNDLE_STEPS, mRecipe.getSteps());
                StepListFragment stepFragment =  new StepListFragment();
                stepFragment.setArguments(stepArguments);
                return stepFragment;
            case 1:
                Bundle ingredientArguments = new Bundle();
                ingredientArguments.putParcelableArrayList(
                        IngredientListFragment.BUNDLE_INGREDIENTS, mRecipe.getIngredients());
                IngredientListFragment ingredienFragment =  new IngredientListFragment();
                ingredienFragment.setArguments(ingredientArguments);
                return ingredienFragment;
            default:
                return new StepListFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
