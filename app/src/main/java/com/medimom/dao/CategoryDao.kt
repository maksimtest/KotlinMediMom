package com.medimom.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.medimom.entity.CategoryEntity
import com.medimom.info.CategoryWithMedicineInfo

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(categoryEntity: CategoryEntity)

    @Update
    suspend fun update(categoryEntity: CategoryEntity)


    @Query("SELECT * FROM categories")
    suspend fun getAll(): List<CategoryEntity>

    @Transaction
    @Query("SELECT * FROM categories")
    suspend fun getAllCategoryWithMedicinesInfo(): List<CategoryWithMedicineInfo>
}