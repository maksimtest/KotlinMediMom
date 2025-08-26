package com.medimom.info

import androidx.room.Embedded
import androidx.room.Relation
import com.medimom.entity.CategoryEntity
import com.medimom.entity.MedicineEntity

data class CategoryWithMedicineInfo (
    @Embedded
    val category: CategoryEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val medicineEntities: List<MedicineEntity>
)