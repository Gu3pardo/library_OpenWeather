package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey
import com.github.openweather.library.common.Constants

@JsonKey("city", "coord")
class Coordinates : JsonModel {
    private val tag: String = Coordinates::class.java.simpleName

    @JsonKey("coord", "lat")
    var lat: Double = Constants.Defaults.Coordinates

    @JsonKey("coord", "lon")
    var lon: Double = Constants.Defaults.Coordinates

    override fun toString(): String = "{Class: $tag, Lat: $lat, Lon: $lon}"
}