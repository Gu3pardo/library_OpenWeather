package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey
import com.github.openweather.library.common.Constants

@JsonKey(Constants.String.Empty, "location")
class Coordinates3 : JsonModel {
    private val tag: String = Coordinates3::class.java.simpleName

    @JsonKey("location", "latitude")
    var latitude: Double = Constants.Defaults.Coordinates

    @JsonKey("location", "longitude")
    var longitude: Double = Constants.Defaults.Coordinates

    override fun toString(): String {
        return "{Class: $tag, Latitude: $latitude, Longitude: $longitude}"
    }
}