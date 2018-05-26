package guepardoapps.lib.openweather.models

import guepardoapps.lib.openweather.enums.ForecastDayTime
import guepardoapps.lib.openweather.enums.ForecastListType
import guepardoapps.lib.openweather.enums.WeatherCondition
import java.util.*

class WeatherForecastPart(private val main: String,
                          private val description: String,
                          private val temperature: Double,
                          private val temperatureMin: Double,
                          private val temperatureMax: Double,
                          private val temperatureKf: Double,
                          private val humidity: Double,
                          private val pressure: Double,
                          private val pressureSeaLevel: Double,
                          private val pressureGroundLevel: Double,
                          private val cloudsAll: Int,
                          private val windSpeed: Double,
                          private val windDegree: Double,
                          private val dateTime: Calendar,
                          private val weatherDefaultIcon: String,
                          private val weatherCondition: WeatherCondition,
                          private val listType: ForecastListType = ForecastListType.Forecast,
                          val dayTime: ForecastDayTime = ForecastDayTime.Null)
    : IWeatherForecastPart {

    private val tag: String = WeatherForecastPart::class.java.canonicalName

    constructor(dateTime: Calendar)
            : this("", "",
            0.0, 0.0, 0.0, 0.0,
            0.0, 0.0, 0.0, 0.0,
            0, 0.0, 0.0,
            dateTime, "", WeatherCondition.Null, ForecastListType.DateDivider)

    override fun getMain(): String {
        return main
    }

    override fun getDescription(): String {
        return description
    }

    override fun getTemperature(): Double {
        return temperature
    }

    override fun getTemperatureMin(): Double {
        return temperatureMin
    }

    override fun getTemperatureMax(): Double {
        return temperatureMax
    }

    override fun getTemperatureKf(): Double {
        return temperatureKf
    }

    override fun getHumidity(): Double {
        return humidity
    }

    override fun getPressure(): Double {
        return pressure
    }

    override fun getPressureSeaLevel(): Double {
        return pressureSeaLevel
    }

    override fun getPressureGroundLevel(): Double {
        return pressureGroundLevel
    }

    override fun getCloudsAll(): Int {
        return cloudsAll
    }

    override fun getWindSpeed(): Double {
        return windSpeed
    }

    override fun getWindDegree(): Double {
        return windDegree
    }

    override fun getDateTime(): Calendar {
        return dateTime
    }

    override fun getWeatherDefaultIcon(): String {
        return weatherDefaultIcon
    }

    override fun getWeatherCondition(): WeatherCondition {
        return weatherCondition
    }

    override fun getListType(): ForecastListType {
        return listType
    }

    override fun toString(): String {
        return "{Class: $tag, Main: $main, Description: $description, " +
                "Temperature: $temperature, TemperatureMin: $temperatureMin, " +
                "TemperatureMax: $temperatureMax, TemperatureKf: $temperatureKf, " +
                "Humidity: $humidity, Pressure: $pressure, " +
                "SeaLevel: $pressureSeaLevel, GroundLevel: $pressureGroundLevel, " +
                "CloudsAll: $cloudsAll, " +
                "WindSpeed: $windSpeed, WindDegree: $windDegree, " +
                "DateTime: $dateTime, " +
                "WeatherDefaultIcon: $weatherDefaultIcon, WeatherCondition: $weatherCondition, " +
                "ListType: $listType, DayTime: $dayTime}"
    }
}