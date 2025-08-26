package com.medimom.info


sealed class GroupedFactInfo{
    data class Child(val name: String) : GroupedFactInfo()
    data class Fact(
        val childName: String,
        val isTemp:Boolean,
        val time: String,//LocalTime,
        val name: String,
    ) : GroupedFactInfo()
}
