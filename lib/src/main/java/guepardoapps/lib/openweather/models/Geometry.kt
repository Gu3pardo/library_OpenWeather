package guepardoapps.lib.openweather.models

import guepardoapps.lib.openweather.annotations.JsonKey

@JsonKey("results", "geometry")
internal class Geometry {
    private val tag: String = Geometry::class.java.simpleName

    @JsonKey("geometry", "location_type")
    var locationType: String = ""

    @JsonKey("geometry", "viewport")
    var viewport: Viewport = Viewport()

    @JsonKey("geometry", "location")
    var location: Coordinates2 = Coordinates2()

    override fun toString(): String {
        return "{Class: $tag, LocationType: $locationType, Viewport: $viewport, Location: $location}"
    }
}