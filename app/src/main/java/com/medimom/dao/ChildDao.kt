package com.medimom.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.medimom.entity.ChildEntity

@Dao
interface ChildDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(childEntity: ChildEntity):Long

    @Update
    suspend fun update(child: ChildEntity)

    @Query("SELECT * FROM children")
    suspend fun getAll(): List<ChildEntity>
}
