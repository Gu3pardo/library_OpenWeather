package guepardoapps.lib.openweather.models

import guepardoapps.lib.openweather.annotations.JsonKey

@JsonKey("", "")
class WeatherForecast {
    private val tag: String = WeatherForecast::class.java.simpleName

    var city: City = City()
    var list: Array<WeatherForecastPart> = arrayOf()

    override fun toString(): String {
        return "{Class: $tag, City: $city, List: $list}"
    }
}