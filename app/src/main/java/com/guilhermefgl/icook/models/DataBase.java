package com.guilhermefgl.icook.models;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.guilhermefgl.icook.BuildConfig;
import com.guilhermefgl.icook.models.daos.IngredientDao;
import com.guilhermefgl.icook.models.daos.RecipeDao;
import com.guilhermefgl.icook.models.daos.StepDao;
import com.guilhermefgl.icook.models.entitys.Ingredient;
import com.guilhermefgl.icook.models.entitys.Recipe;
import com.guilhermefgl.icook.models.entitys.Step;

@Database(entities = {Recipe.class, Ingredient.class, Step.class}, version =  BuildConfig.db_version)
public abstract class DataBase extends RoomDatabase {

    private static DataBase INSTANCE;

    public static DataBase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, DataBase.class, BuildConfig.db_name).build();
        }
        return INSTANCE;
    }

    public abstract RecipeDao recipeDao();

    public abstract IngredientDao ingredientDao();

    public abstract StepDao stepDao();
}
