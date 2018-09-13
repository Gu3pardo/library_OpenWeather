package guepardoapps.lib.openweather.models

import guepardoapps.lib.openweather.annotations.JsonKey

@JsonKey("city", "coord")
class GeoLocation {
    private val tag: String = GeoLocation::class.java.simpleName

    @JsonKey("coord", "lat")
    var latitude: Double = 720.0

    @JsonKey("coord", "lon")
    var longitude: Double = 720.0

    override fun toString(): String {
        return "{Class: $tag, Latitude: $latitude, Longitude: $longitude}"
    }
}