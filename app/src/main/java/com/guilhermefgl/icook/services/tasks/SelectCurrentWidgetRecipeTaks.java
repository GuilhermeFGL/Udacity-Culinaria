package com.guilhermefgl.icook.services.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.guilhermefgl.icook.models.DataBase;
import com.guilhermefgl.icook.models.entitys.Ingredient;
import com.guilhermefgl.icook.models.entitys.Recipe;
import com.guilhermefgl.icook.models.entitys.Step;

import java.util.ArrayList;

public class SelectCurrentWidgetRecipeTaks extends AsyncTask<Void, Void, Recipe> {

    private final DataBase mDb;
    private final SelectRecipeCallBack mCallBack;

    public SelectCurrentWidgetRecipeTaks(Context context, SelectRecipeCallBack callBack) {
        mDb = DataBase.getInstance(context);
        mCallBack = callBack;
    }

    @Override
    protected Recipe doInBackground(Void... voids) {
        Recipe recipe = mDb.recipeDao().get();
        if (recipe != null) {
            recipe.setIngredients((ArrayList<Ingredient>) mDb.ingredientDao().getAll());
            recipe.setSteps((ArrayList<Step>) mDb.stepDao().getAll());
        }
        return recipe;
    }

    @Override
    protected void onPostExecute(Recipe recipe) {
        super.onPostExecute(recipe);
        if (mCallBack != null) {
            mCallBack.onSelect(recipe);
        }
    }

    public interface SelectRecipeCallBack {
        void onSelect(Recipe recipe);
    }
}
