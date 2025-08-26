package com.medimom.util

import android.annotation.SuppressLint
import android.util.Log
import com.medimom.R
import java.time.LocalDate
import java.time.LocalTime
import java.time.Period
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

class StringUtil {
    companion object {

        fun getLocalizedMonths(locale: Locale): List<String> {
            return Month.entries.map { month ->
                month.getDisplayName(TextStyle.SHORT, locale)
            }
        }

        fun getMonthNumberByName(name: String, locale: Locale): Int {
            var result = 0
            Month.entries.forEach {
                result++
                if(it.name.contains(name)) return result
            }
            return 0
        }

        fun convertDateToFullString(date: LocalDate): String {
            val year = date.year
            val month = date.monthValue
            val day = date.dayOfMonth
            val result = "$year.$month.$day"
            return result
        }

        fun convertDateFromFulString(str: String): LocalDate {
            val arr = str.split(".")
            try {
                var year = arr[0].toInt()
                var month = arr[1].toInt()
                var day = arr[2].toInt()
                val result = LocalDate.of(year, month, day)
                return result
            } catch (e: Exception) {
                Log.d("MyTag", "StringUtil Error 01")
            }
            return LocalDate.now()
        }
        fun convertDateFromShortString(str: String): LocalDate {
            val arr = str.split(".")
            try {
                var day = arr[0].toInt()
                var month = arr[1].toInt()
                val year = LocalDate.now().year
                val result = LocalDate.of(year, month, day)
                return result
            } catch (e: Exception) {
                Log.d("MyTag", "StringUtil ????????????? 01")
            }
            return LocalDate.now()
        }

        fun convertDateToShortString(date: LocalDate): String {
            val month = date.monthValue
            val day = date.dayOfMonth
            return "$day.$month"
        }
        fun convertDateToShortNameString(date: LocalDate): String {
            val month = date.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
            val day = date.dayOfMonth
            return "$day $month"
        }
        fun convertDateToYear(date: LocalDate): Int {
            return date.year
        }
        fun convertDateFromShortNameString(str: String): LocalDate {
            val arr = str.split(" ")
            try {
                var day = arr[0].toInt()

                var month = getMonthNumberByName(arr[1], Locale.ENGLISH)
                val year = LocalDate.now().year
                val result = LocalDate.of(year, month, day)
                return result
            } catch (e: Exception) {
                Log.d("MyTag", "StringUtil ????????????? 01")
            }
            return LocalDate.now()
        }

        fun convertTimeToString(time: LocalTime): String {
            val hrs = time.hour
            val minute = time.minute
            return "$hrs:$minute"
        }

        fun convertTimeFromString(str: String): LocalTime {
            val arr = str.split(":")
            val hrs = arr[0].toInt()
            val minute = arr[1].toInt()
            return LocalTime.of(hrs, minute)
        }

        fun convertTemperatureFromString(str: String): Double {
            val arr = str.split(".")
            val p1 = arr[0].toInt()
            val p2 = arr[1].toDouble()
            return p1 * 1.0 + p2 / 10
        }
        @SuppressLint("DefaultLocale")
        fun convertTemperatureToString(temp:Double): String {
            return String.format("%.1f", temp)
        }

        fun convertStringToInt(str: String, default: Int): Int {
            try {
                return str.toInt()
            } catch (e: Exception) {
            }
            return default
        }
        fun convertCountDaysToShortString(value: Int):String{
            when(value){
                1-> return "1st"
                2-> return "2nd"
                3-> return "3d"
                4-> return "4d"
                5-> return "5th"
                6-> return "6th"
            }
            return "${value}d"
        }
        fun calculateAge(birthDay: LocalDate): Int {
            return Period.between(birthDay, LocalDate.now()).years
        }
        fun calculateDays(date: LocalDate, currentDate: LocalDate): Int {
            return Period.between(date, currentDate).days
        }

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