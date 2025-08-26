package com.medimom.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "children")
class ChildEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val name: String,
    var age: Int,
    val weight: Int,
    val birthDate: LocalDate,
    val photoInt: Int,
    val photoUri: Uri?
)