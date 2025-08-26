package com.medimom.data

import android.net.Uri

class MedicineInfo(
    val name:String,
    val photoInt:Int,
    val photoUri:Uri?,
    val unit: UnitMeasurement,
    val countPerDay: Int
    )