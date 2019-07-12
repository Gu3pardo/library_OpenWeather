package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey

@JsonKey("", "location")
class Coordinates3 : JsonModel {
    private val tag: String = Coordinates3::class.java.simpleName

    @JsonKey("location", "latitude")
    var latitude: Double = 720.0

    @JsonKey("location", "longitude")
    var longitude: Double = 720.0

    override fun toString(): String = "{Class: $tag, Latitude: $latitude, Longitude: $longitude}"
}