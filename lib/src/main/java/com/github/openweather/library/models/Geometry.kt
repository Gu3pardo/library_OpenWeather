package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey
import com.github.openweather.library.common.Constants

@JsonKey("results", "geometry")
internal class Geometry : JsonModel {
    private val tag: String = Geometry::class.java.simpleName

    @JsonKey("geometry", "location_type")
    var locationType: String = Constants.String.Empty

    @JsonKey("geometry", "viewport")
    var viewport: Viewport = Viewport()

    @JsonKey("geometry", "location")
    var location: Coordinates2 = Coordinates2()

    override fun toString(): String {
        return "{Class: $tag, LocationType: $locationType, Viewport: $viewport, Location: $location}"
    }
}