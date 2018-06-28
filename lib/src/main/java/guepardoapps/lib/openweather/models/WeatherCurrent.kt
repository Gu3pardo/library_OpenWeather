package guepardoapps.lib.openweather.models

import guepardoapps.lib.openweather.annotations.JsonKey
import guepardoapps.lib.openweather.enums.WeatherCondition
import java.util.*

@JsonKey("", "sys")
class WeatherCurrent {
    private val tag: String = WeatherCurrent::class.java.simpleName

    @JsonKey("weather", "description")
    var description: String = ""

    @JsonKey("weather", "main")
    var weatherCondition: WeatherCondition = WeatherCondition.Null

    @JsonKey("main", "temp")
    var temperature: Double = 0.0

    @JsonKey("main", "humidity")
    var humidity: Double = 0.0

    @JsonKey("main", "pressure")
    var pressure: Double = 0.0

    @JsonKey("sys", "sunrise")
    var sunriseTime: Calendar = Calendar.getInstance()

    @JsonKey("sys", "sunset")
    var sunsetTime: Calendar = Calendar.getInstance()
    
    var city: City = City()
    var lastUpdate: Calendar = Calendar.getInstance()

    override fun toString(): String {
        return "{Class: $tag, City: $city, Description: $description, " +
                "Temperature: $temperature, Humidity: $humidity, Pressure: $pressure, " +
                "SunriseTime: $sunriseTime, SunsetTime: $sunsetTime, LastUpdate: $lastUpdate, " +
                "WeatherCondition: $weatherCondition}"
    }
}