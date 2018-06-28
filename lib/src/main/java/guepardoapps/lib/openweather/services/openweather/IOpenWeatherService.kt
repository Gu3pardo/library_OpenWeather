package guepardoapps.lib.openweather.services.openweather

import android.content.Context
import guepardoapps.lib.openweather.models.WeatherForecast

interface IOpenWeatherService {
    var city: String
    var apiKey: String

    var notificationEnabled: Boolean
    var receiverActivity: Class<*>?

    var wallpaperEnabled: Boolean

    var reloadEnabled: Boolean
    var reloadTimeout: Long

    var onWeatherServiceListener: OnWeatherServiceListener?

    fun initialize(context: Context)
    fun loadCurrentWeather()
    fun loadForecastWeather()
    fun searchForecast(forecast: WeatherForecast, searchValue: String): WeatherForecast
    fun dispose()
}