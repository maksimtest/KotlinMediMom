package com.medimom.info

import java.time.LocalDate

class DailyTemperatureByDayInfo(
    val date: LocalDate,
    var countDays: Int,
    val list: List<DailyTemperatureByOneInfo>
)