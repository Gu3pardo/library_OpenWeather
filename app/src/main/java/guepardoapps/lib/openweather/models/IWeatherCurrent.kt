package guepardoapps.lib.openweather.models

import guepardoapps.lib.openweather.enums.WeatherCondition
import java.io.Serializable
import java.util.*

interface IWeatherCurrent : Serializable {
    fun getCity(): ICity

    fun getDescription(): String

    fun getTemperature(): Double

    fun getHumidity(): Double

    fun getPressure(): Double

    fun getSunriseTime(): Calendar

    fun getSunsetTime(): Calendar

    fun getLastUpdate(): Calendar

    fun getWeatherCondition(): WeatherCondition
}