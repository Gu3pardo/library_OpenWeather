package guepardoapps.lib.openweather.models

import guepardoapps.lib.openweather.annotations.JsonKey
import guepardoapps.lib.openweather.common.Constants

@JsonKey("city", "coord")
class Coordinates {
    private val tag: String = Coordinates::class.java.simpleName

    @JsonKey("coord", "lat")
    var lat: Double = Constants.Defaults.Coordinates

    @JsonKey("coord", "lon")
    var lon: Double = Constants.Defaults.Coordinates

    override fun toString(): String {
        return "{Class: $tag, Lat: $lat, Lon: $lon}"
    }
}