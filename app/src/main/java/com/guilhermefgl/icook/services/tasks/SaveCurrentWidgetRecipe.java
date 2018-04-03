package com.guilhermefgl.icook.services.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.guilhermefgl.icook.models.DataBase;
import com.guilhermefgl.icook.models.Recipe;

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
            mDb.ingredientDao().clear();
            mDb.ingredientDao().insertAll(recipe.getIngredients());
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
