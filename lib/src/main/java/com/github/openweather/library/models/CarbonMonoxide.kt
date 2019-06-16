package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey
import java.util.*

@JsonKey("", "")
class CarbonMonoxide : JsonModel {
    private val tag: String = CarbonMonoxide::class.java.simpleName

    @JsonKey("", "time")
    var dateTime: Calendar = Calendar.getInstance()

    var coordinates: Coordinates3 = Coordinates3()

    var data: List<CarbonMonoxideData> = listOf()

    override fun toString(): String = "{Class: $tag, DateTime: $dateTime, Coordinates: $coordinates, Data: $data}"
}