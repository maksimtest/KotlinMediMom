package com.pilltracker.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.pilltracker.data.MedicineWithCategory
import com.pilltracker.entity.MedicineEntity

@Dao
interface MedicineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(medicineEntity: MedicineEntity)

    @Query("SELECT * FROM medicines")
    suspend fun getAll(): List<MedicineEntity>

//    @Query("SELECT * FROM medicines WHERE categoryId = :categoryId")
//    suspend fun getByCategory(categoryId: Int): List<Medicine>

    @Transaction
    @Query("""
        SELECT m.name as name, m.photo as photo, 
            m.unitId as description, c.name as categoryName
        FROM medicines m
        JOIN categories c ON m.categoryId = c.id
        WHERE m.categoryId = c.id
        """)
    suspend fun getAllWithMedicines(): List<MedicineWithCategory>

    //@Transaction
    @Query("""
        SELECT *
        FROM medicines m
        JOIN categories c ON m.categoryId = c.id
        WHERE c.isPyretyc = 1
        """)
    suspend fun getMedicineForTemperatures(): List<MedicineEntity>
}
/*
    name:String,
    val photo: Int,
    val description: String,
    val categoryName:String

    data class Medicine(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val categoryId: Int,
    val unitId: Int,
    val photo: Int
)
* */