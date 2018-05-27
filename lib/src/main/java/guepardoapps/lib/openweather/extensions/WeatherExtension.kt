package guepardoapps.lib.openweather.extensions

import guepardoapps.lib.openweather.enums.ForecastDayTime
import guepardoapps.lib.openweather.enums.WeatherCondition
import guepardoapps.lib.openweather.models.IWeatherForecast
import guepardoapps.lib.openweather.models.IWeatherForecastPart
import java.util.*

fun IWeatherForecast.getMostWeatherCondition(days: Int = -1): WeatherCondition {
    var weatherCondition = WeatherCondition.Null

    val weatherConditionCountArray: Array<WeatherCondition> = arrayOf(
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

    for (weatherConditionEntry in weatherConditionCountArray) {
        weatherConditionEntry.count = 0
    }

    val futureCalendar: Calendar = Calendar.getInstance()
    futureCalendar.add(Calendar.DAY_OF_YEAR, days)

    for (forecastPart in this.getList()) {
        for (weatherConditionEntry in weatherConditionCountArray) {
            if (forecastPart.getWeatherCondition() == weatherConditionEntry
                    && (days == -1 || forecastPart.getDateTime().timeInMillis < futureCalendar.timeInMillis)) {
                weatherConditionEntry.count++
                break
            }
        }
    }

    var mostCount = 0
    for (weatherConditionEntry in weatherConditionCountArray) {
        if (weatherConditionEntry.count > mostCount) {
            mostCount = weatherConditionEntry.count
            weatherCondition = weatherConditionEntry
        }
    }

    return weatherCondition
}

fun IWeatherForecastPart.getForecastDayTime(): ForecastDayTime {
    return when (this.getDateTime().get(Calendar.HOUR_OF_DAY)) {
        in 0..4 -> ForecastDayTime.Night
        in 5..10 -> ForecastDayTime.Morning
        in 11..14 -> ForecastDayTime.Midday
        in 15..17 -> ForecastDayTime.Afternoon
        in 18..20 -> ForecastDayTime.Evening
        in 21..24 -> ForecastDayTime.Night
        else -> ForecastDayTime.Null
    }
}