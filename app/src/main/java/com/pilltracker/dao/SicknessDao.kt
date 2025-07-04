package com.pilltracker.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pilltracker.entity.CategoryEntity
import com.pilltracker.entity.ChildEntity
import com.pilltracker.entity.SicknessEntity
import com.pilltracker.info.CategoryWithMedicineInfo
import java.time.LocalDate

@Dao
interface SicknessDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: SicknessEntity):Long

    @Update
    suspend fun update(item: SicknessEntity)


    @Query("SELECT * FROM sicknesses WHERE finished=0")
    suspend fun getAllActiveSicknesses(): List<SicknessEntity>

    @Query("SELECT * FROM sicknesses WHERE finished=0")
    suspend fun getActiveSicknesses(): List<SicknessEntity>?

    @Query("SELECT * FROM sicknesses WHERE childId=:childId AND finished=0")
    suspend fun getActiveSicknessByChild(childId: Int): SicknessEntity?


}