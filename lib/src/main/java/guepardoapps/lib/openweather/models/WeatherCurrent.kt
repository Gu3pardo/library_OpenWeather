package guepardoapps.lib.openweather.models

import guepardoapps.lib.openweather.annotations.JsonKey
import guepardoapps.lib.openweather.common.Constants
import guepardoapps.lib.openweather.enums.WeatherCondition
import java.util.*

@JsonKey(Constants.String.Empty, "sys")
class WeatherCurrent {
    private val tag: String = WeatherCurrent::class.java.simpleName

    @JsonKey("weather", "icon")
    var icon: String = Constants.String.Empty

    @JsonKey("weather", "description")
    var description: String = Constants.String.Empty

    @JsonKey("weather", "main")
    var weatherCondition: WeatherCondition = WeatherCondition.Null

    @JsonKey("main", "temp")
    var temperature: Double = Constants.Defaults.Zero.toDouble()

    @JsonKey("main", "temp_min")
    var temperatureMin: Double = Constants.Defaults.Zero.toDouble()

    @JsonKey("main", "temp_max")
    var temperatureMax: Double = Constants.Defaults.Zero.toDouble()

    @JsonKey("main", "humidity")
    var humidity: Double = Constants.Defaults.Zero.toDouble()

    @JsonKey("main", "pressure")
    var pressure: Double = Constants.Defaults.Zero.toDouble()

    @JsonKey(Constants.String.Empty, "visibility")
    var visibility: Int = Constants.Defaults.Zero

    @JsonKey("clouds", "all")
    var cloudsAll: Int = Constants.Defaults.Zero

    @JsonKey("wind", "speed")
    var windSpeed: Double = Constants.Defaults.Zero.toDouble()

    @JsonKey("wind", "deg")
    var windDegree: Double = Constants.Defaults.Zero.toDouble()

    @JsonKey(Constants.String.Empty, "dt")
    var dateTime: Calendar = Calendar.getInstance()

    @JsonKey("sys", "sunrise")
    var sunriseTime: Calendar = Calendar.getInstance()

    @JsonKey("sys", "sunset")
    var sunsetTime: Calendar = Calendar.getInstance()

    var city: City = City()
    var lastUpdate: Calendar = Calendar.getInstance()

    override fun toString(): String {
        return "{Class: $tag, City: $city, Icon: $icon, Description: $description, " +
                "Temperature: $temperature, TemperatureMin: $temperatureMin, TemperatureMax: $temperatureMax, " +
                "Humidity: $humidity, Pressure: $pressure, Visibility: $visibility, " +
                "CloudsAll: $cloudsAll, WindSpeed: $windSpeed, WindDegree: $windDegree, " +
                "SunriseTime: $sunriseTime, SunsetTime: $sunsetTime, LastUpdate: $lastUpdate, " +
                "WeatherCondition: $weatherCondition}"
    }
}