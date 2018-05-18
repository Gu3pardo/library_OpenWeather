package guepardoapps.lib.openweather.models

import guepardoapps.lib.openweather.enums.ForecastDayTime
import guepardoapps.lib.openweather.enums.ForecastListType
import guepardoapps.lib.openweather.enums.WeatherCondition
import java.util.*

class WeatherForecastPart(private val main: String,
                          private val description: String,
                          private val temperatureMin: Double,
                          private val temperatureMax: Double,
                          private val humidity: Double,
                          private val pressure: Double,
                          private val dateTime: Calendar,
                          private val weatherCondition: WeatherCondition,
                          private val listType: ForecastListType,
                          private val dayTime: ForecastDayTime) : IWeatherForecastPart {

    override fun getMain(): String {
        return main
    }

    override fun getDescription(): String {
        return description
    }

    override fun getTemperatureMin(): Double {
        return temperatureMin
    }

    override fun getTemperatureMax(): Double {
        return temperatureMax
    }

    override fun getHumidity(): Double {
        return humidity
    }

    override fun getPressure(): Double {
        return pressure
    }

    override fun getDateTime(): Calendar {
        return dateTime
    }

    override fun getWeatherCondition(): WeatherCondition {
        return weatherCondition
    }

    override fun getListType(): ForecastListType {
        return listType
    }

    override fun getDayTime(): ForecastDayTime {
        return dayTime
    }
}