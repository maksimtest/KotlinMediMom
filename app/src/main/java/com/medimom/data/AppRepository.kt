package com.medimom.data

import com.medimom.entity.CategoryEntity
import com.medimom.entity.ChildEntity
import com.medimom.entity.FactEntity
import com.medimom.entity.MedicineEntity
import com.medimom.entity.SicknessEntity
import com.medimom.entity.UnitEntity
import com.medimom.info.GroupedMedicineInfo

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
