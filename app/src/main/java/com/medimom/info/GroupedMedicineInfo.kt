package com.medimom.info

import android.net.Uri

sealed class GroupedMedicineInfo{
    data class Category(val name: String) : GroupedMedicineInfo()
    data class Medicine(
        val name: String,
        val photoInt: Int,
        val photoUri: Uri?,
        val description: String
    ) : GroupedMedicineInfo()
}
