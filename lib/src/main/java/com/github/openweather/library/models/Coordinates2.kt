package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey
import com.github.openweather.library.common.Constants

@JsonKey("geometry", "location")
internal class Coordinates2 : JsonModel {
    private val tag: String = Coordinates2::class.java.simpleName

    @JsonKey("location", "lat")
    var lat: Double = Constants.Defaults.Coordinates

    @JsonKey("location", "lng")
    var lng: Double = Constants.Defaults.Coordinates

    override fun toString(): String = "{Class: $tag, Lat: $lat, Lng: $lng}"
}