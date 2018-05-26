package guepardoapps.lib.openweather.models

import guepardoapps.lib.openweather.enums.ForecastDayTime
import guepardoapps.lib.openweather.enums.ForecastListType
import guepardoapps.lib.openweather.enums.WeatherCondition
import java.io.Serializable
import java.util.*

interface IWeatherForecastPart : Serializable {
    fun getMain(): String

    fun getDescription(): String

    fun getTemperature(): Double

    fun getTemperatureMin(): Double

    fun getTemperatureMax(): Double

    fun getTemperatureKf(): Double

    fun getHumidity(): Double

    fun getPressure(): Double

    fun getPressureSeaLevel(): Double

    fun getPressureGroundLevel(): Double

    fun getCloudsAll(): Int

    fun getWindSpeed(): Double

    fun getWindDegree(): Double

    fun getDateTime(): Calendar

    fun getWeatherDefaultIcon(): String

    fun getWeatherCondition(): WeatherCondition

    fun getListType(): ForecastListType
}