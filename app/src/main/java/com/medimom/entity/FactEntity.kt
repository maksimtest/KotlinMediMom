package com.medimom.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(
    tableName = "facts",
    foreignKeys = [
        ForeignKey(
            entity = ChildEntity::class,
            parentColumns = ["id"],
            childColumns = ["childId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = SicknessEntity::class,
            parentColumns = ["id"],
            childColumns = ["sicknessId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = MedicineEntity::class,
            parentColumns = ["id"],
            childColumns = ["medicineId"],
            onDelete = ForeignKey.SET_NULL
        ),
    ],
    indices = [Index("childId"), Index("sicknessId"), Index("medicineId")]
)
data class FactEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val childId: Int,
    val temperature: Double,
    val moreThanUsual: Boolean,
    val medicineId: Int?,
    val sicknessId: Int?,
    val temperatureMode: Boolean,
    val date: LocalDate,
    val time: LocalTime
)