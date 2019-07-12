package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey

@JsonKey("geometry", "location")
internal class Coordinates2 : JsonModel {
    private val tag: String = Coordinates2::class.java.simpleName

    @JsonKey("location", "lat")
    var lat: Double = 720.0

    @JsonKey("location", "lng")
    var lng: Double = 720.0

    override fun toString(): String = "{Class: $tag, Lat: $lat, Lng: $lng}"
}