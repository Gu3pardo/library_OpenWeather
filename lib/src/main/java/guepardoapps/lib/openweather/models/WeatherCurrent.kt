package guepardoapps.lib.openweather.models

import guepardoapps.lib.openweather.enums.WeatherCondition
import java.util.*

class WeatherCurrent(private val city: ICity,
                     private val description: String,
                     private val temperature: Double,
                     private val humidity: Double,
                     private val pressure: Double,
                     private val sunriseTime: Calendar,
                     private val sunsetTime: Calendar,
                     private val lastUpdate: Calendar,
                     private val weatherCondition: WeatherCondition) : IWeatherCurrent {

    private val tag: String = WeatherCurrent::class.java.simpleName

    override fun getCity(): ICity {
        return city
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

    override fun toString(): String {
        return "{Class: $tag, City: $city, Description: $description, " +
                "Temperature: $temperature, Humidity: $humidity, Pressure: $pressure, " +
                "SunriseTime: $sunriseTime, SunsetTime: $sunsetTime, LastUpdate: $lastUpdate, " +
                "WeatherCondition: $weatherCondition}"
    }
}