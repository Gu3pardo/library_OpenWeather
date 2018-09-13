package guepardoapps.lib.openweather.models

import guepardoapps.lib.openweather.annotations.JsonKey
import java.util.*

@JsonKey("", "")
class UvIndex {
    private val tag: String = UvIndex::class.java.simpleName

    var geoLocation: GeoLocation = GeoLocation()

    @JsonKey("", "date")
    var dateTime: Calendar = Calendar.getInstance()

    @JsonKey("", "value")
    var value: Double = 0.0

    override fun toString(): String {
        return "{Class: $tag, GeoLocation: $geoLocation, DateTime: $dateTime, Value: $value}"
    }
}