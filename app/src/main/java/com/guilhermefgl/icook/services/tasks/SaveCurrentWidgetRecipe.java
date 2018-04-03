package com.guilhermefgl.icook.services.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.guilhermefgl.icook.models.DataBase;
import com.guilhermefgl.icook.models.Ingredient;
import com.guilhermefgl.icook.models.Recipe;

import java.util.ArrayList;

public class SaveCurrentWidgetRecipe extends AsyncTask<Recipe, Void, Recipe> {

    private DataBase mDb;
    private SaveRecipeCallBack mCallBack;

    public SaveCurrentWidgetRecipe(Context context, SaveRecipeCallBack callBack) {
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
