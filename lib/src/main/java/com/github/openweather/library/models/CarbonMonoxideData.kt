package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey
import com.github.openweather.library.common.Constants

@JsonKey("", "data")
class CarbonMonoxideData : JsonModel {
    private val tag: String = CarbonMonoxideData::class.java.simpleName

    @JsonKey("", "precision")
    var precision: Double = Constants.Defaults.Zero.toDouble()

    @JsonKey("", "pressure")
    var pressure: Double = Constants.Defaults.Zero.toDouble()

    @JsonKey("", "value")
    var value: Double = Constants.Defaults.Zero.toDouble()

    override fun toString(): String = "{Class: $tag, Precision: $precision, Pressure: $pressure, Value: $value}"
}