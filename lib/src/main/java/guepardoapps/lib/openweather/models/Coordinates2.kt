package guepardoapps.lib.openweather.models

import guepardoapps.lib.openweather.annotations.JsonKey

@JsonKey("geometry", "location")
class Coordinates2 {
    private val tag: String = Coordinates2::class.java.simpleName

    @JsonKey("location", "lat")
    var lat: Double = 720.0

    @JsonKey("location", "lng")
    var lng: Double = 720.0

    override fun toString(): String {
        return "{Class: $tag, Lat: $lat, Lng: $lng}"
    }
}