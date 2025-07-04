package com.pilltracker.util

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.pilltracker.R
import java.text.DateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.Period
import java.time.Year
import java.time.temporal.ChronoField

class StringUtil {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun convertDateToString(date: LocalDate): String {
            val year = date.year
            val month = date.monthValue
            val day = date.dayOfMonth
            val result = "$year.$month.$day"
            return result
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun convertDateFromString(str: String): LocalDate {
            val arr = str.split(".")
            try {
                var year = arr[0].toInt()
                var month = arr[1].toInt()
                var day = arr[2].toInt()
                //LocalDate.now().dayOfMonth
                val result = LocalDate.of(year, month, day)
                return result
            } catch (e: Exception) {
                Log.d("MyTag", "StringUtil ????????????? 01")
            }
            return LocalDate.now()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun convertDateToShortString(date: LocalDate): String {
            val month = date.monthValue + 1
            val day = date.dayOfMonth
            return "$day.$month"
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun convertTimeToString(time: LocalTime): String {
            val hrs = time.hour
            val minute = time.minute
            return "$hrs:$minute"
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun convertTimeFromString(str: String): LocalTime {
            val arr = str.split(":")
            val hrs = arr[0].toInt()
            val minute = arr[1].toInt()
            return LocalTime.of(hrs, minute)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun convertTemperatureFromString(str: String): Double {
            val arr = str.split(".")
            val p1 = arr[0].toInt()
            val p2 = arr[1].toInt()
            return p1 * 0.1 + p2 / 10
        }

        fun convertStringToInt(str: String, default: Int): Int {
            try {
                return str.toInt()
            } catch (e: Exception) {
            }
            return default
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun calculateAge(birthDay: LocalDate): Int {
            return Period.between(birthDay, LocalDate.now()).years
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun convertStringToLocalDate(dateStr: String): LocalDate {
            val default = LocalDate.now()
            val arr: List<String> = dateStr.split("_")
            if (arr.size != 3) {
                return default
            }
            val year = arr[0].toInt()
            val month = arr[0].toInt() - 1
            val day = arr[0].toInt()
            val date = LocalDate.of(year, month, day)
            return date
        }

        fun defineGenderByName(name: String): Int {
            val defaultImageId = R.drawable.ic_boy
            val boyImageId = R.drawable.ic_boy
            val girlImageId = R.drawable.ic_girl
            var listBoyNames = mutableListOf(
                "Serafim",
                "Seriphim",
                "Серафим",
                "Серафім",
                "Досік",
                "Феодосій",
                "Феодосий",
                "Feodosiy",
                "Feod",
                "Pheodosiy",
                "Matvey",
                "Matviy",
                "Valik",
                "Валик",
                "Валентин",
                "Макс",
                "Макс",
                "Максим",
                "Максім",
                "Максим",
                "Max",
                "Maks",
                "Maksim",
                "Maksum"
            )
            val listGirlNames = mutableListOf(
                "Настя",
                "Натя",
                "Натя",
                "Настя",
                "Анастасія",
                "Анастасия",
                "Nastya",
                "Ivanka",
                "Ivanna",
                "Іванка",
                "Іванна",
                "Алена",
                "Олена",
                "Алена",
                "Аленка",
                "Olena",
                "Alena"
            )
            if (listBoyNames.filter { it.lowercase().equals(name.lowercase()) }.isNotEmpty()) {
                return boyImageId
            }
            if (listGirlNames.filter { it.lowercase().equals(name.lowercase()) }.isNotEmpty()) {
                return girlImageId
            }
            return defaultImageId
        }

        fun substringToCountStr(str: String, search: String, num: Int): String {
            var indexEnd = -1
            for (i in 1..num) {
                val indexCandidate = str.indexOf(search, indexEnd + 1, true)
                if (indexCandidate < 0) {
                    break;
                }
                indexEnd = indexCandidate
            }
            return str.substring(0, indexEnd)
        }
    }

}