package com.pilltracker.info

import java.time.LocalTime

class DailyTemperatureByOneInfo(
    val time: LocalTime,
    val temperature: String,
    val moreThanUsual: Boolean,
    val medicine: String
)