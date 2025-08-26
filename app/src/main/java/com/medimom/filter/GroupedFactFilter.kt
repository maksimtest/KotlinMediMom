package com.medimom.filter

data class GroupedFactFilter(private var isTemperatureShow: Boolean, private var isTakePillsShow:Boolean){
    fun turnTemperature(value:Boolean){
        isTemperatureShow = value
    }
    fun turnTakePill(value:Boolean){
        isTakePillsShow = value
    }
    fun isTemperatureShow():Boolean{
        return isTemperatureShow
    }
    fun isTakePillsShow():Boolean{
        return isTakePillsShow
    }
}