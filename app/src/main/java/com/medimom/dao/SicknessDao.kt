package com.medimom.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.medimom.entity.SicknessEntity

@Dao
interface SicknessDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: SicknessEntity)

    @Update
    suspend fun update(item: SicknessEntity)

    @Query("SELECT * FROM sicknesses")
    suspend fun getAll(): List<SicknessEntity>

    @Query("SELECT * FROM sicknesses WHERE finished=0")
    suspend fun getAllActiveSicknesses(): List<SicknessEntity>

    @Query("SELECT * FROM sicknesses WHERE finished=0")
    suspend fun getActiveSicknesses(): List<SicknessEntity>

    @Query("SELECT * FROM sicknesses WHERE childId=:childId AND finished=0")
    suspend fun getActiveSicknessByChild(childId: Int): SicknessEntity?


}