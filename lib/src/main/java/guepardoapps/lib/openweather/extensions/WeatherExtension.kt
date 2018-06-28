package guepardoapps.lib.openweather.extensions

import guepardoapps.lib.openweather.enums.ForecastDayTime
import guepardoapps.lib.openweather.enums.ForecastListType
import guepardoapps.lib.openweather.enums.WeatherCondition
import guepardoapps.lib.openweather.models.WeatherForecast
import guepardoapps.lib.openweather.models.WeatherForecastPart
import java.util.*

fun WeatherForecast.getMostWeatherCondition(days: Int = -1): WeatherCondition {
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

    for (forecastPart in this.list) {
        if (forecastPart.listType != ForecastListType.Forecast) {
            continue
        }

        for (weatherConditionEntry in weatherConditionCountArray) {
            if (forecastPart.weatherCondition == weatherConditionEntry
                    && (days == -1 || forecastPart.dateTime.timeInMillis < futureCalendar.timeInMillis)) {
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

fun WeatherForecast.getMinTemperature(): Double {
    var value = 10000.0

    for (forecastPart in this.list) {
        if (forecastPart.listType != ForecastListType.Forecast) {
            continue
        }

        if (forecastPart.temperatureMin < value) {
            value = forecastPart.temperatureMin
        }
    }

    return value
}

fun WeatherForecast.getMaxTemperature(): Double {
    var value = -10000.0

    for (forecastPart in this.list) {
        if (forecastPart.listType != ForecastListType.Forecast) {
            continue
        }

        if (forecastPart.temperatureMax > value) {
            value = forecastPart.temperatureMax
        }
    }

    return value
}

fun WeatherForecast.getMinPressure(): Double {
    var value = 10000.0

    for (forecastPart in this.list) {
        if (forecastPart.listType != ForecastListType.Forecast) {
            continue
        }

        if (forecastPart.pressure < value) {
            value = forecastPart.pressure
        }
    }

    return value
}

fun WeatherForecast.getMaxPressure(): Double {
    var value = -10000.0

    for (forecastPart in this.list) {
        if (forecastPart.listType != ForecastListType.Forecast) {
            continue
        }

        if (forecastPart.pressure > value) {
            value = forecastPart.pressure
        }
    }

    return value
}

fun WeatherForecast.getMinHumidity(): Double {
    var value = 10000.0

    for (forecastPart in this.list) {
        if (forecastPart.listType != ForecastListType.Forecast) {
            continue
        }

        if (forecastPart.humidity < value) {
            value = forecastPart.humidity
        }
    }

    return value
}

fun WeatherForecast.getMaxHumidity(): Double {
    var value = -10000.0

    for (forecastPart in this.list) {
        if (forecastPart.listType != ForecastListType.Forecast) {
            continue
        }

        if (forecastPart.humidity > value) {
            value = forecastPart.humidity
        }
    }

    return value
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
