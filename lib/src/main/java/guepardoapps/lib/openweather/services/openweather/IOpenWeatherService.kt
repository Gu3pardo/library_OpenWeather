package guepardoapps.lib.openweather.services.openweather

import guepardoapps.lib.openweather.models.IWeatherForecast

interface IOpenWeatherService {
    var city: String
    var apiKey: String

    var notificationEnabled: Boolean
    var receiverActivity: Class<*>?

    var wallpaperEnabled: Boolean

    var reloadEnabled: Boolean
    var reloadTimeout: Long

    var onWeatherServiceListener: OnWeatherServiceListener?

    fun loadCurrentWeather()
    fun loadForecastWeather()
    fun searchForecast(forecast: IWeatherForecast, searchValue: String): IWeatherForecast
    fun dispose()
}