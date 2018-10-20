package guepardoapps.lib.openweather.models

import guepardoapps.lib.openweather.annotations.JsonKey
import guepardoapps.lib.openweather.common.Constants

@JsonKey(Constants.String.Empty, Constants.String.Empty)
class WeatherForecast {
    private val tag: String = WeatherForecast::class.java.simpleName

    var city: City = City()
    var list: List<WeatherForecastPart> = listOf()

    override fun toString(): String {
        return "{Class: $tag, City: $city, List: $list}"
    }
}