package com.medimom.data

import android.net.Uri

data class MedicineWithCategory(
    val name:String,
    val photoInt: Int,
    val photoUri: Uri?,
    val description: String,
    val categoryName:String
)