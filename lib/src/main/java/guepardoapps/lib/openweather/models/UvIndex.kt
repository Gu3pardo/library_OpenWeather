package guepardoapps.lib.openweather.models

import guepardoapps.lib.openweather.annotations.JsonKey
import guepardoapps.lib.openweather.common.Constants
import java.util.*

@JsonKey(Constants.String.Empty, Constants.String.Empty)
class UvIndex {
    private val tag: String = UvIndex::class.java.simpleName

    var coordinates: Coordinates = Coordinates()

    @JsonKey(Constants.String.Empty, "date")
    var dateTime: Calendar = Calendar.getInstance()

    @JsonKey(Constants.String.Empty, "value")
    var value: Double = Constants.Defaults.Zero.toDouble()

    override fun toString(): String {
        return "{Class: $tag, Coordinates: $coordinates, DateTime: $dateTime, Value: $value}"
    }
}