package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey
import com.github.openweather.library.common.Constants
import java.util.*

@JsonKey(Constants.String.Empty, Constants.String.Empty)
class UvIndex : JsonModel {
    private val tag: String = UvIndex::class.java.simpleName

    var coordinates: Coordinates = Coordinates()

    @JsonKey(Constants.String.Empty, "date")
    var dateTime: Calendar = Calendar.getInstance()

    @JsonKey(Constants.String.Empty, "value")
    var value: Double = Constants.Defaults.Zero.toDouble()

    override fun toString(): String {
        return "{Class: $tag, Coordinates: $coordinates, DateTime: $dateTime, Value: $value}"
    }
}