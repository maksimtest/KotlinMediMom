package com.medimom.info

import java.time.LocalDate

class StatisticByOneInfo(
    val date: LocalDate,
    val pickTemperature: Double,
    val countDays: Int,
    val medicines: List<String>
)