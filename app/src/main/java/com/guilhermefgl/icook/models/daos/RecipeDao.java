package com.guilhermefgl.icook.models.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.guilhermefgl.icook.models.entitys.Recipe;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipe LIMIT 1")
    Recipe get();

    @Insert
    void insert(Recipe recipe);

    @Query("DELETE FROM recipe")
    void clear();
}
