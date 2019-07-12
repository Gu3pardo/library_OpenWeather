package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey

@JsonKey("city", "coord")
class Coordinates : JsonModel {
    private val tag: String = Coordinates::class.java.simpleName

    @JsonKey("coord", "lat")
    var lat: Double = 720.0

    @JsonKey("coord", "lon")
    var lon: Double = 720.0

    override fun toString(): String = "{Class: $tag, Lat: $lat, Lon: $lon}"
}