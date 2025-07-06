package com.pilltracker.info

import androidx.room.Embedded
import androidx.room.Relation
import com.pilltracker.entity.CategoryEntity
import com.pilltracker.entity.MedicineEntity

data class CategoryWithMedicineInfo (
    @Embedded
    val category: CategoryEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val medicineEntities: List<MedicineEntity>
)