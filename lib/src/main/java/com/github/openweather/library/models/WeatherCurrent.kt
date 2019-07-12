package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey
import com.github.openweather.library.enums.WeatherCondition
import com.github.openweather.library.extensions.empty
import java.util.*

@JsonKey("", "sys")
class WeatherCurrent : JsonModel {
    private val tag: String = WeatherCurrent::class.java.simpleName

    @JsonKey("weather", "icon")
    var icon: String = String.empty

    @JsonKey("weather", "description")
    var description: String = String.empty

    @JsonKey("weather", "main")
    var weatherCondition: WeatherCondition = WeatherCondition.Null

    @JsonKey("main", "temp")
    var temperature: Double = 0.0

    @JsonKey("main", "temp_min")
    var temperatureMin: Double = 0.0

    @JsonKey("main", "temp_max")
    var temperatureMax: Double = 0.0

    @JsonKey("main", "humidity")
    var humidity: Double = 0.0

    @JsonKey("main", "pressure")
    var pressure: Double = 0.0

    @JsonKey("", "visibility")
    var visibility: Int = 0

    @JsonKey("clouds", "all")
    var cloudsAll: Int = 0

    @JsonKey("wind", "speed")
    var windSpeed: Double = 0.0

    //@JsonKey("wind", "deg")
    //var windDegree: Double = Constants.Defaults.Zero.toDouble()

    @JsonKey("", "dt")
    var dateTime: Calendar = Calendar.getInstance()

    @JsonKey("sys", "sunrise")
    var sunriseTime: Calendar = Calendar.getInstance()

    @JsonKey("sys", "sunset")
    var sunsetTime: Calendar = Calendar.getInstance()

    var city: City = City()
    var lastUpdate: Calendar = Calendar.getInstance()

    override fun toString(): String = "{Class: $tag, City: $city, Icon: $icon, Description: $description, " +
            "Temperature: $temperature, TemperatureMin: $temperatureMin, TemperatureMax: $temperatureMax, " +
            "Humidity: $humidity, Pressure: $pressure, Visibility: $visibility, " +
            "CloudsAll: $cloudsAll, WindSpeed: $windSpeed, " + //WindDegree: $windDegree, " +
            "SunriseTime: $sunriseTime, SunsetTime: $sunsetTime, LastUpdate: $lastUpdate, " +
            "WeatherCondition: $weatherCondition}"
}