package guepardoapps.lib.openweather.models

import guepardoapps.lib.openweather.enums.WeatherCondition
import java.util.*

class WeatherCurrent(private val city: String,
                     private val country: String,
                     private val description: String,
                     private val temperature: Double,
                     private val humidity: Double,
                     private val pressure: Double,
                     private val sunriseTime: Calendar,
                     private val sunsetTime: Calendar,
                     private val lastUpdate: Calendar,
                     private val weatherCondition: WeatherCondition) : IWeatherCurrent {

    override fun getCity(): String {
        return city
    }

    override fun getCountry(): String {
        return country
    }

    override fun getDescription(): String {
        return description
    }

    override fun getTemperature(): Double {
        return temperature
    }

    override fun getHumidity(): Double {
        return humidity
    }

    override fun getPressure(): Double {
        return pressure
    }

    override fun getSunriseTime(): Calendar {
        return sunriseTime
    }

    override fun getSunsetTime(): Calendar {
        return sunsetTime
    }

    override fun getLastUpdate(): Calendar {
        return lastUpdate
    }

    override fun getWeatherCondition(): WeatherCondition {
        return weatherCondition
    }
}