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
        SELECT * 
        FROM facts f
        JOIN sicknesses s 
        WHERE f.sicknessId=s.id AND s.finished=0
        """)
    suspend fun getAllActiveFact(): List<FactEntity>

    @Update
    suspend fun update(factEntity: FactEntity)

}

/*

FactEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val childId: Int,
    val temperature: Double,
    val moreThanUsual: Boolean,
    val medicineId: Int,
    val temperatureMode: Boolean,
    val date: LocalDate,
    val time: LocalTime
)
data class FactWithDetails(
    val id: Int,
    val childId: Int,
    val childName: String,
    val childPhoto: Int,
    val childAge: Int,
    val temperature: Double,
    val moreThanUsual: Boolean,
    val medicineId: Int,
    val medicineName: String,
    val temperatureMode: Boolean,
    val date: LocalDate,
    val time: LocalTime
)
* */