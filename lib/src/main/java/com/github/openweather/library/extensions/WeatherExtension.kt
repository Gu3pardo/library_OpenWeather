package com.github.openweather.library.extensions

import com.github.openweather.library.common.Constants
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
    weatherConditionCountList.forEach { x -> x.count = Constants.Defaults.Zero }

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

    return weatherConditionCountList
            .sortedByDescending { x -> x.count }
            .first()
}

fun WeatherForecast.getMinTemperature(): Double =
        this.list
                .filter { x -> x.listType == ForecastListType.Forecast }
                .sortedBy { x -> x.temperatureMin }
                .first()
                .temperatureMin

fun WeatherForecast.getMaxTemperature(): Double =
        this.list
                .filter { x -> x.listType == ForecastListType.Forecast }
                .sortedByDescending { x -> x.temperatureMax }
                .first()
                .temperatureMax

fun WeatherForecast.getMinPressure(): Double =
        this.list
                .filter { x -> x.listType == ForecastListType.Forecast }
                .sortedBy { x -> x.pressure }
                .first()
                .pressure

fun WeatherForecast.getMaxPressure(): Double =
        this.list
                .filter { x -> x.listType == ForecastListType.Forecast }
                .sortedByDescending { x -> x.pressure }
                .first()
                .pressure

fun WeatherForecast.getMinHumidity(): Double =
        this.list
                .filter { x -> x.listType == ForecastListType.Forecast }
                .sortedBy { x -> x.humidity }
                .first()
                .humidity

fun WeatherForecast.getMaxHumidity(): Double =
        this.list
                .filter { x -> x.listType == ForecastListType.Forecast }
                .sortedByDescending { x -> x.humidity }
                .first()
                .humidity
