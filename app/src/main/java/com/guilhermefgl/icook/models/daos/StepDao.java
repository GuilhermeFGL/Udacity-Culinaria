package com.guilhermefgl.icook.models.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.guilhermefgl.icook.models.entitys.Step;

import java.util.List;

@Dao
public interface StepDao {

    @Query("SELECT * FROM step")
    List<Step> getAll();

    @Insert
    void insertAll(List<Step> steps);

}
