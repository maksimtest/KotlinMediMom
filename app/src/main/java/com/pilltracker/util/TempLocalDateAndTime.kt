package com.pilltracker.util

import java.time.LocalDate
import java.time.LocalTime


class TempLocalDateAndTime {
    companion object {
        @Volatile
        private var INSTANCE: TempLocalDateAndTime = TempLocalDateAndTime()

        fun getInstance(): TempLocalDateAndTime{
            return INSTANCE
        }
    }
    lateinit var currentDate: LocalDate
    lateinit var currentTime: LocalTime

}