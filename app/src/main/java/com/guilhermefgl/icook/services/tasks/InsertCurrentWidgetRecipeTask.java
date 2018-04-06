package com.guilhermefgl.icook.services.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.guilhermefgl.icook.models.DataBase;
import com.guilhermefgl.icook.models.entitys.Ingredient;
import com.guilhermefgl.icook.models.entitys.Recipe;
import com.guilhermefgl.icook.models.entitys.Step;

import java.util.ArrayList;

public class InsertCurrentWidgetRecipeTask extends AsyncTask<Recipe, Void, Recipe> {

    private final DataBase mDb;
    private final SaveRecipeCallBack mCallBack;

    public InsertCurrentWidgetRecipeTask(Context context, SaveRecipeCallBack callBack) {
        mDb = DataBase.getInstance(context);
        mCallBack = callBack;
    }

    @Override
    protected Recipe doInBackground(Recipe... recipes) {
        Recipe recipe = recipes[0];
        if (recipe != null) {
            mDb.recipeDao().clear();
            mDb.recipeDao().insert(recipe);

            ArrayList<Ingredient> ingredientList = recipe.getIngredients();
            for(Ingredient ingredient : ingredientList) {
                ingredient.setRecipeId(recipe.getId());
            }
            mDb.ingredientDao().insertAll(ingredientList);

            ArrayList<Step> stepList = recipe.getSteps();
            for(Step step : stepList) {
                step.setRecipeId(recipe.getId());
            }
            mDb.stepDao().insertAll(stepList);
        }
        return recipe;
    }

    @Override
    protected void onPostExecute(Recipe recipe) {
        super.onPostExecute(recipe);
        if (mCallBack != null) {
            mCallBack.onSave(recipe);
        }
    }

    public interface SaveRecipeCallBack {
        void onSave(Recipe recipe);
    }

}
