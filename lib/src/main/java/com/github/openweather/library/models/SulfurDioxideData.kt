package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey

@JsonKey("", "data")
class SulfurDioxideData : JsonModel {
    private val tag: String = SulfurDioxideData::class.java.simpleName

    @JsonKey("", "precision")
    var precision: Double = 0.0

    @JsonKey("", "pressure")
    var pressure: Double = 0.0

    @JsonKey("", "value")
    var value: Double = 0.0

    override fun toString(): String = "{Class: $tag, Precision: $precision, Pressure: $pressure, Value: $value}"
}