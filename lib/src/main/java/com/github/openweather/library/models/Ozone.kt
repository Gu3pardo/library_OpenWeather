package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey
import com.github.openweather.library.common.Constants
import java.util.*

@JsonKey("", "")
class Ozone : JsonModel {
    private val tag: String = Ozone::class.java.simpleName

    @JsonKey("", "time")
    var dateTime: Calendar = Calendar.getInstance()

    var coordinates: Coordinates3 = Coordinates3()

    @JsonKey("", "data")
    var data: Double = Constants.Defaults.Zero.toDouble()

    override fun toString(): String = "{Class: $tag, DateTime: $dateTime, Coordinates: $coordinates, Data: $data}"
}