package com.pilltracker.info

import android.net.Uri

data class ChildInfo(
    var id: Int,
    var name: String,
    var age: Int,
    var weight: Int,
    var birthDate: String, //year_month_day
    var photoInt: Int,
    var photoUri: Uri?,
    var healthy: Boolean,
    var sickDate: String,
    var sicknessId: Int?,
    var lastTemperature:Double,
    var state:String
)