package com.guilhermefgl.icook.models.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.guilhermefgl.icook.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface IngredientDao {

    @Query("SELECT * FROM ingredient")
    List<Ingredient> getAll();

    @Insert
    void insertAll(ArrayList<Ingredient> ingredients);

    @Query("DELETE FROM ingredient")
    void clear();

}
