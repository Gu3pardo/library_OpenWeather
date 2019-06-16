package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey
import com.github.openweather.library.common.Constants

@JsonKey("", "")
class NitrogenDioxideDataHolder : JsonModel {
    private val tag: String = NitrogenDioxideDataHolder::class.java.simpleName

    @JsonKey("", "precision")
    var precision: Double = Constants.Defaults.Zero.toDouble()

    @JsonKey("", "value")
    var value: Double = Constants.Defaults.Zero.toDouble()

    override fun toString(): String = "{Class: $tag, Precision: $precision, Value: $value}"
}