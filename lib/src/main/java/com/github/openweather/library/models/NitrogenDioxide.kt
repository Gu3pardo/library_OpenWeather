package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey
import java.util.*

@JsonKey("", "")
class NitrogenDioxide : JsonModel {
    private val tag: String = NitrogenDioxide::class.java.simpleName

    @JsonKey("", "time")
    var dateTime: Calendar = Calendar.getInstance()

    var coordinates: Coordinates3 = Coordinates3()

    var data: NitrogenDioxideData = NitrogenDioxideData()

    override fun toString(): String = "{Class: $tag, DateTime: $dateTime, Coordinates: $coordinates, Data: $data}"
}