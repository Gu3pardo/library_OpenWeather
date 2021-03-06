package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey
import com.github.openweather.library.enums.ForecastListType
import com.github.openweather.library.enums.WeatherCondition
import com.github.openweather.library.extensions.empty
import com.github.openweather.library.extensions.integerFormat
import java.util.*

@JsonKey("list", "weather")
class WeatherForecastPart : JsonModel {
    private val tag: String = WeatherForecastPart::class.java.simpleName

    @JsonKey("weather", "main")
    var main: String = String.empty
    var weatherCondition: WeatherCondition = WeatherCondition.Null

    @JsonKey("weather", "description")
    var description: String = String.empty

    @JsonKey("weather", "icon")
    var weatherDefaultIcon: String = String.empty

    @JsonKey("main", "temp")
    var temperature: Double = 0.0

    @JsonKey("main", "temp_min")
    var temperatureMin: Double = 0.0

    @JsonKey("main", "temp_max")
    var temperatureMax: Double = 0.0

    @JsonKey("main", "temp_kf")
    var temperatureKf: Double = 0.0

    @JsonKey("main", "pressure")
    var pressure: Double = 0.0

    @JsonKey("main", "sea_level")
    var pressureSeaLevel: Double = 0.0

    @JsonKey("main", "grnd_level")
    var pressureGroundLevel: Double = 0.0

    @JsonKey("main", "humidity")
    var humidity: Double = 0.0

    @JsonKey("clouds", "all")
    var cloudsAll: Int = 0

    @JsonKey("wind", "speed")
    var windSpeed: Double = 0.0

    @JsonKey("wind", "deg")
    var windDegree: Double = 0.0

    @JsonKey("", "dt")
    var dateTime: Calendar = Calendar.getInstance()

    var listType: ForecastListType = ForecastListType.Null

    override fun toString(): String = "{Class: $tag, " +
            "Main: $main, " +
            "Description: $description, " +
            "Temperature: $temperature, " +
            "TemperatureMin: $temperatureMin, " +
            "TemperatureMax: $temperatureMax, " +
            "TemperatureKf: $temperatureKf, " +
            "Humidity: $humidity, " +
            "Pressure: $pressure, " +
            "SeaLevel: $pressureSeaLevel, " +
            "GroundLevel: $pressureGroundLevel, " +
            "CloudsAll: $cloudsAll, " +
            "WindSpeed: $windSpeed, " +
            "WindDegree: $windDegree, " +
            "DateTime: $dateTime, " +
            "WeatherDefaultIcon: $weatherDefaultIcon, " +
            "WeatherCondition: $weatherCondition, " +
            "ListType: $listType, " +
            "Day: ${dateTime.get(Calendar.DAY_OF_MONTH)}, " +
            "Month: ${dateTime.get(Calendar.MONTH) + 1}," +
            "Year: ${dateTime.get(Calendar.YEAR)}, " +
            "Hour: ${dateTime.get(Calendar.HOUR_OF_DAY)}" +
            "Minute: ${dateTime.get(Calendar.MINUTE)}" +
            "Date:${dateTime.get(Calendar.DAY_OF_MONTH).integerFormat(2)}.${(dateTime.get(Calendar.MONTH) + 1).integerFormat(2)}.${dateTime.get(Calendar.YEAR).integerFormat(4)}" +
            "Time:${dateTime.get(Calendar.HOUR_OF_DAY).integerFormat(2)}:${dateTime.get(Calendar.MINUTE).integerFormat(2)}}"
}