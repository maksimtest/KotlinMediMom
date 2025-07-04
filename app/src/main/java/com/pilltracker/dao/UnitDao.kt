package com.pilltracker.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pilltracker.entity.UnitEntity

@Dao
interface UnitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(unit: UnitEntity)

    @Query("SELECT * FROM units")
    suspend fun getAll(): List<UnitEntity>

}