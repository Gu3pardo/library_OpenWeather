package guepardoapps.lib.openweather.extensions

import guepardoapps.lib.openweather.enums.ForecastDayTime
import guepardoapps.lib.openweather.enums.ForecastListType
import guepardoapps.lib.openweather.enums.WeatherCondition
import guepardoapps.lib.openweather.models.WeatherForecast
import guepardoapps.lib.openweather.models.WeatherForecastPart
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

    weatherConditionCountList.sortByDescending { x -> x.count }
    return weatherConditionCountList.first()
}

fun WeatherForecast.getMinTemperature(): Double {
    val dataList = this.list.filter { x -> x.listType == ForecastListType.Forecast }
    dataList.sortedBy { x -> x.temperatureMin }
    return dataList.first().temperatureMin
}

fun WeatherForecast.getMaxTemperature(): Double {
    val dataList = this.list.filter { x -> x.listType == ForecastListType.Forecast }
    dataList.sortedByDescending { x -> x.temperatureMax }
    return dataList.first().temperatureMax
}

fun WeatherForecast.getMinPressure(): Double {
    val dataList = this.list.filter { x -> x.listType == ForecastListType.Forecast }
    dataList.sortedBy { x -> x.pressure }
    return dataList.first().pressure
}

fun WeatherForecast.getMaxPressure(): Double {
    val dataList = this.list.filter { x -> x.listType == ForecastListType.Forecast }
    dataList.sortedByDescending { x -> x.pressure }
    return dataList.first().pressure
}

fun WeatherForecast.getMinHumidity(): Double {
    val dataList = this.list.filter { x -> x.listType == ForecastListType.Forecast }
    dataList.sortedBy { x -> x.humidity }
    return dataList.first().humidity
}

fun WeatherForecast.getMaxHumidity(): Double {
    val dataList = this.list.filter { x -> x.listType == ForecastListType.Forecast }
    dataList.sortedByDescending { x -> x.humidity }
    return dataList.first().humidity
}

fun WeatherForecastPart.getForecastDayTime(): ForecastDayTime {
    return when (this.dateTime.get(Calendar.HOUR_OF_DAY)) {
        in 0..4 -> ForecastDayTime.Night
        in 5..10 -> ForecastDayTime.Morning
        in 11..14 -> ForecastDayTime.Midday
        in 15..17 -> ForecastDayTime.Afternoon
        in 18..20 -> ForecastDayTime.Evening
        in 21..24 -> ForecastDayTime.Night
        else -> ForecastDayTime.Null
    }
}
