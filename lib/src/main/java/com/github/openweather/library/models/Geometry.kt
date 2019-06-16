package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey
import com.github.openweather.library.extensions.empty

@JsonKey("results", "geometry")
internal class Geometry : JsonModel {
    private val tag: String = Geometry::class.java.simpleName

    @JsonKey("geometry", "location_type")
    var locationType: String = String.empty

    @JsonKey("geometry", "viewport")
    var viewport: Viewport = Viewport()

    @JsonKey("geometry", "location")
    var location: Coordinates2 = Coordinates2()

    override fun toString(): String = "{Class: $tag, LocationType: $locationType, Viewport: $viewport, Location: $location}"
}