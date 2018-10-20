package guepardoapps.lib.openweather.models

import guepardoapps.lib.openweather.annotations.JsonKey
import guepardoapps.lib.openweather.common.Constants

@JsonKey("geometry", "location")
internal class Coordinates2 {
    private val tag: String = Coordinates2::class.java.simpleName

    @JsonKey("location", "lat")
    var lat: Double = Constants.Defaults.Coordinates

    @JsonKey("location", "lng")
    var lng: Double = Constants.Defaults.Coordinates

    override fun toString(): String {
        return "{Class: $tag, Lat: $lat, Lng: $lng}"
    }
}