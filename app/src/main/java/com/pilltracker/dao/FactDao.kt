package com.pilltracker.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pilltracker.entity.FactEntity

@Dao
interface FactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(factEntity: FactEntity)

    @Query("""
        SELECT f.id, f.childId, f.temperature, f.moreThanUsual, f.medicineId,
            f.sicknessId, f.temperatureMode, f.date, f.time
        FROM facts f
        JOIN sicknesses s 
        WHERE f.sicknessId=s.id AND s.finished=0
        """)
    suspend fun getAllActiveFact(): List<FactEntity>

    @Update
    suspend fun update(factEntity: FactEntity)

    @Query("""
        SELECT * 
        FROM facts
        """)
    suspend fun getAll(): List<FactEntity>

}
