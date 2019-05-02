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

    weatherConditionCountList.sortByDescending { x -> x.count }
    return weatherConditionCountList.first()
}

fun WeatherForecast.getMinTemperature(): Double {
    var dataList = this.list.filter { x -> x.listType == ForecastListType.Forecast }
    dataList = dataList.sortedBy { x -> x.temperatureMin }
    return dataList.first().temperatureMin
}

fun WeatherForecast.getMaxTemperature(): Double {
    var dataList = this.list.filter { x -> x.listType == ForecastListType.Forecast }
    dataList = dataList.sortedByDescending { x -> x.temperatureMax }
    return dataList.first().temperatureMax
}

fun WeatherForecast.getMinPressure(): Double {
    var dataList = this.list.filter { x -> x.listType == ForecastListType.Forecast }
    dataList = dataList.sortedBy { x -> x.pressure }
    return dataList.first().pressure
}

fun WeatherForecast.getMaxPressure(): Double {
    var dataList = this.list.filter { x -> x.listType == ForecastListType.Forecast }
    dataList = dataList.sortedByDescending { x -> x.pressure }
    return dataList.first().pressure
}

fun WeatherForecast.getMinHumidity(): Double {
    var dataList = this.list.filter { x -> x.listType == ForecastListType.Forecast }
    dataList = dataList.sortedBy { x -> x.humidity }
    return dataList.first().humidity
}

fun WeatherForecast.getMaxHumidity(): Double {
    var dataList = this.list.filter { x -> x.listType == ForecastListType.Forecast }
    dataList = dataList.sortedByDescending { x -> x.humidity }
    return dataList.first().humidity
}
