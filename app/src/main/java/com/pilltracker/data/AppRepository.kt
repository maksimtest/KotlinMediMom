package com.pilltracker.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.pilltracker.entity.CategoryEntity
import com.pilltracker.entity.ChildEntity
import com.pilltracker.entity.FactEntity
import com.pilltracker.entity.MedicineEntity
import com.pilltracker.entity.SicknessEntity
import com.pilltracker.entity.UnitEntity

class AppRepository(private val db: AppDatabase) {
    val dataHelper = DataHelper()

    suspend fun insertUnit(unit: UnitEntity) = db.unitDao().insert(unit)
    suspend fun getUnits() = db.unitDao().getAll()

    suspend fun insertCategory(category:CategoryEntity) = db.categoryDao().insert(category)
    suspend fun getAllCategories() = db.categoryDao().getAll()
    suspend fun updateCategory(category:CategoryEntity) = db.categoryDao().update(category)

    suspend fun insertMedicine(medicine: MedicineEntity) = db.medicineDao().insert(medicine)
    suspend fun getAllMedicines() = db.medicineDao().getAll()

    suspend fun insertSickness(item: SicknessEntity):Long = db.sicknessDao().insert(item)
    suspend fun getActiveSicknessByChild(childId: Int) = db.sicknessDao().getActiveSicknessByChild(childId)
    suspend fun getActiveSicknesses() = db.sicknessDao().getActiveSicknesses()
    suspend fun updateSicknesses(item: SicknessEntity) = db.sicknessDao().update(item)
    suspend fun getAllActiveSicknesses() = db.sicknessDao().getAllActiveSicknesses()

    suspend fun insertChild(child: ChildEntity):Long = db.childDao().insert(child)
    suspend fun updateChild(child: ChildEntity) = db.childDao().update(child)
    suspend fun getChildren() = db.childDao().getAll()

    suspend fun getGroupedMedicinesByCategoryList() = DataHelper().groupMedicinesByCategory(
        db.medicineDao().getAllWithMedicines()
    )
    suspend fun getMedicineForTemperatures(): List<MedicineEntity> = db.medicineDao().getMedicineForTemperatures()
    suspend fun getCategoryWithMedicineList() = db.categoryDao().getAllCategoryWithMedicinesInfo()

    suspend fun insertFact(fact:FactEntity) = db.factDao().insert(fact)
    suspend fun updateFact(fact:FactEntity) = db.factDao().update(fact)
    suspend fun getAllActiveFacts() = db.factDao().getAllActiveFact()


    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun initDefaultData() {
        DataHelper().initDefaultDatabase(db)

    }
}
