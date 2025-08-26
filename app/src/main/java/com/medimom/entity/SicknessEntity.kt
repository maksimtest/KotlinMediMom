package com.medimom.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "sicknesses")
data class SicknessEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val childId: Int,
    val dateStart: LocalDate,
    val dateEnd: LocalDate,
    val finished: Boolean
)