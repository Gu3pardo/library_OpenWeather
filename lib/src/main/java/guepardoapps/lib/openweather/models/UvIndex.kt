package guepardoapps.lib.openweather.models

import guepardoapps.lib.openweather.annotations.JsonKey
import java.util.*

@JsonKey("", "")
class UvIndex {
    private val tag: String = UvIndex::class.java.simpleName

    var coordinates: Coordinates = Coordinates()

    @JsonKey("", "date")
    var dateTime: Calendar = Calendar.getInstance()

    @JsonKey("", "value")
    var value: Double = 0.0

    override fun toString(): String {
        return "{Class: $tag, Coordinates: $coordinates, DateTime: $dateTime, Value: $value}"
    }
}