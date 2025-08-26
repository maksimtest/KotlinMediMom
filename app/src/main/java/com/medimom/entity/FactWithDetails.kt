package com.medimom.entity

import java.time.LocalDate
import java.time.LocalTime

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