package com.pilltracker.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.pilltracker.entity.CategoryEntity
import com.pilltracker.entity.ChildEntity
import com.pilltracker.entity.FactEntity
import com.pilltracker.entity.MedicineEntity
import com.pilltracker.entity.SicknessEntity
import com.pilltracker.entity.UnitEntity
import com.pilltracker.info.GroupedMedicineInfo

class AppRepository(private val db: AppDatabase) {
    val dataHelper = DataHelper()

    suspend fun insertUnit(unit: UnitEntity) = db.unitDao().insert(unit)
    suspend fun getUnits() = db.unitDao().getAll()

    suspend fun getAllCategories() = db.categoryDao().getAll()
    suspend fun getCategoryWithMedicineList() = db.categoryDao().getAllCategoryWithMedicinesInfo()
    suspend fun updateCategory(item: CategoryEntity){
        if(item.id == 0){
            db.categoryDao().insert(item)
        } else {
            db.categoryDao().update(item)
        }
    }

    suspend fun updateMedicine(item: MedicineEntity) {
        if (item.id == 0) {
            db.medicineDao().insert(item)
        } else {
            db.medicineDao().update(item)
        }
    }
    suspend fun getAllMedicines() = db.medicineDao().getAll()

    suspend fun insertSickness(item: SicknessEntity) = db.sicknessDao().insert(item)
    suspend fun getActiveSicknessByChild(childId: Int) =
        db.sicknessDao().getActiveSicknessByChild(childId)

    suspend fun getActiveSicknesses() = db.sicknessDao().getActiveSicknesses()
    suspend fun updateSicknesses(item: SicknessEntity) = db.sicknessDao().update(item)
    suspend fun getAllActiveSicknesses() = db.sicknessDao().getAllActiveSicknesses()
    suspend fun getAllSicknesses() = db.sicknessDao().getAll()

    suspend fun updateChild(item: ChildEntity) {
        if (item.id == 0) {
            db.childDao().insert(item)
        } else {
            db.childDao().update(item)
        }
    }

    suspend fun getChildren() = db.childDao().getAll()

    // TODO need analyze and add description information
    suspend fun getGroupedMedicinesByCategoryList():List<GroupedMedicineInfo> {
        val medicinesList = db.medicineDao().getAllWithMedicines()
        return DataHelper().generateMedicinesByCategory(medicinesList)
    }

    suspend fun getMedicineForTemperatures(): List<MedicineEntity> =
        db.medicineDao().getMedicineForTemperatures()

    suspend fun updateFact(fact: FactEntity){
        if(fact.id==0){
            db.factDao().insert(fact)
        } else {
            db.factDao().update(fact)
        }
    }
    suspend fun getAllActiveFacts() = db.factDao().getAllActiveFact()
    suspend fun getAllFacts() = db.factDao().getAll()


    suspend fun initDefaultData() {
        DataHelper().initDefaultDatabase(db)

    }
}
