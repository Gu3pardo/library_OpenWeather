package guepardoapps.lib.openweather.models

import guepardoapps.lib.openweather.annotations.JsonKey

@JsonKey("city", "coord")
class Coordinates {
    private val tag: String = Coordinates::class.java.simpleName

    @JsonKey("coord", "lat")
    var lat: Double = 720.0

    @JsonKey("coord", "lon")
    var lon: Double = 720.0

    override fun toString(): String {
        return "{Class: $tag, Lat: $lat, Lon: $lon}"
    }
}