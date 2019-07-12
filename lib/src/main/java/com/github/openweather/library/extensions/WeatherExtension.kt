package com.github.openweather.library.extensions

import com.github.openweather.library.enums.ForecastListType
import com.github.openweather.library.enums.WeatherCondition
import com.github.openweather.library.models.WeatherForecast
import java.util.*

fun WeatherForecast.getMostWeatherCondition(days: Int = -1): WeatherCondition {
    val weatherConditionCountList = arrayOf(
            WeatherCondition.Clear,
            WeatherCondition.Clouds,
            WeatherCondition.Drizzle,
            WeatherCondition.Fog,
            WeatherCondition.Haze,
            WeatherCondition.Mist,
            WeatherCondition.Rain,
            WeatherCondition.Sleet,
            WeatherCondition.Snow,
            WeatherCondition.Squalls,
            WeatherCondition.Sun,
            WeatherCondition.Thunderstorm)
    weatherConditionCountList.forEach { x -> x.count = 0 }

    val futureCalendar: Calendar = Calendar.getInstance()
    futureCalendar.add(Calendar.DAY_OF_YEAR, days)

    this.list.forEach { forecastPart ->
        if (forecastPart.listType == ForecastListType.Forecast) {
            weatherConditionCountList.forEach { weatherCondition ->
                if (forecastPart.weatherCondition == weatherCondition
                        && (days == -1 || forecastPart.dateTime.timeInMillis < futureCalendar.timeInMillis)) {
                    weatherCondition.count++
                }
            }
        }
    }

    return weatherConditionCountList.maxBy { x -> x.count }!!
}

fun WeatherForecast.getMinTemperature(): Double =
        this.list
                .filter { x -> x.listType == ForecastListType.Forecast }
                .minBy { x -> x.temperatureMin }!!
                .temperatureMin

fun WeatherForecast.getMaxTemperature(): Double =
        this.list
                .filter { x -> x.listType == ForecastListType.Forecast }
                .maxBy { x -> x.temperatureMax }!!
                .temperatureMax

fun WeatherForecast.getMinPressure(): Double =
        this.list
                .filter { x -> x.listType == ForecastListType.Forecast }
                .minBy { x -> x.pressure }!!
                .pressure

fun WeatherForecast.getMaxPressure(): Double =
        this.list
                .filter { x -> x.listType == ForecastListType.Forecast }
                .maxBy { x -> x.pressure }!!
                .pressure

fun WeatherForecast.getMinHumidity(): Double =
        this.list
                .filter { x -> x.listType == ForecastListType.Forecast }
                .minBy { x -> x.humidity }!!
                .humidity

fun WeatherForecast.getMaxHumidity(): Double =
        this.list
                .filter { x -> x.listType == ForecastListType.Forecast }
                .maxBy { x -> x.humidity }!!
                .humidity
