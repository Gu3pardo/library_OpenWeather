package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey
import java.util.*

@JsonKey("", "")
class SulfurDioxide : JsonModel {
    private val tag: String = SulfurDioxide::class.java.simpleName

    @JsonKey("", "time")
    var dateTime: Calendar = Calendar.getInstance()

    var coordinates: Coordinates3 = Coordinates3()

    var data: List<SulfurDioxideData> = listOf()

    override fun toString(): String = "{Class: $tag, DateTime: $dateTime, Coordinates: $coordinates, Data: $data}"
}