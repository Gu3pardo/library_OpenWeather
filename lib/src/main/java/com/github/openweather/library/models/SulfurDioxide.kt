package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey
import com.github.openweather.library.common.Constants
import java.util.*

@JsonKey(Constants.String.Empty, Constants.String.Empty)
class SulfurDioxide : JsonModel {
    private val tag: String = SulfurDioxide::class.java.simpleName

    @JsonKey(Constants.String.Empty, "time")
    var dateTime: Calendar = Calendar.getInstance()

    var coordinates: Coordinates3 = Coordinates3()

    var data: List<SulfurDioxideData> = listOf()

    override fun toString(): String {
        return "{Class: $tag, DateTime: $dateTime, Coordinates: $coordinates, Data: $data}"
    }
}