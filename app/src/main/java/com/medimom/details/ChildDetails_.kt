package com.pilltracker.details

import java.time.LocalDate

data class ChildDetails_(
    val id: Int,
    val name: String,
    var age: Int,
    val weight: Int,
    val birthDate: LocalDate,
    val photo: Int
)