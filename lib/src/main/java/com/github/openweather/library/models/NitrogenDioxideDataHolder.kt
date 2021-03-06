package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey

@JsonKey("", "")
class NitrogenDioxideDataHolder : JsonModel {
    private val tag: String = NitrogenDioxideDataHolder::class.java.simpleName

    @JsonKey("", "precision")
    var precision: Double = 0.0

    @JsonKey("", "value")
    var value: Double = 0.0

    override fun toString(): String = "{Class: $tag, Precision: $precision, Value: $value}"
}