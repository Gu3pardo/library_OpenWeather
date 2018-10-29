package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey
import com.github.openweather.library.common.Constants

@JsonKey(Constants.String.Empty, Constants.String.Empty)
class NitrogenDioxideDataHolder : JsonModel {
    private val tag: String = NitrogenDioxideDataHolder::class.java.simpleName

    @JsonKey(Constants.String.Empty, "precision")
    var precision: Double = Constants.Defaults.Zero.toDouble()

    @JsonKey(Constants.String.Empty, "value")
    var value: Double = Constants.Defaults.Zero.toDouble()

    override fun toString(): String {
        return "{Class: $tag, Precision: $precision, Value: $value}"
    }
}