package com.guilhermefgl.icook.models;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.guilhermefgl.icook.BuildConfig;
import com.guilhermefgl.icook.models.daos.IngredientDao;
import com.guilhermefgl.icook.models.daos.RecipeDao;

@Database(entities = {Recipe.class, Ingredient.class}, version =  BuildConfig.db_version)
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
}
