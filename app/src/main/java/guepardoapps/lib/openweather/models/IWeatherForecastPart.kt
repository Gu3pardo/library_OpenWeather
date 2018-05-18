package guepardoapps.lib.openweather.models

import guepardoapps.lib.openweather.enums.ForecastDayTime
import guepardoapps.lib.openweather.enums.ForecastListType
import guepardoapps.lib.openweather.enums.WeatherCondition
import java.io.Serializable
import java.util.*

interface IWeatherForecastPart : Serializable {
    fun getMain(): String

    fun getDescription(): String

    fun getTemperatureMin(): Double

    fun getTemperatureMax(): Double

    fun getHumidity(): Double

    fun getPressure(): Double

    fun getDateTime(): Calendar

    fun getWeatherCondition(): WeatherCondition

    fun getListType(): ForecastListType

    fun getDayTime(): ForecastDayTime
}