package guepardoapps.lib.openweather.services

import guepardoapps.lib.openweather.models.IWeatherForecast

interface IOpenWeatherService {

    fun setCity(city: String)

    fun getCity(): String?

    fun setApiKey(apiKey: String)

    fun getApiKey(): String?

    fun setNotificationEnabled(notificationEnabled: Boolean)

    fun getNotificationEnabled(): Boolean

    fun setReceiverActivity(receiverActivity: Class<*>?)

    fun getReceiverActivity(): Class<*>?

    fun setWallpaperEnabled(wallpaperEnabled: Boolean)

    fun getWallpaperEnabled(): Boolean

    fun setReloadEnabled(reloadEnabled: Boolean)

    fun getReloadEnabled(): Boolean

    fun setReloadTimeout(reloadTimeout: Long)

    fun getReloadTimeout(): Long

    fun setOnWeatherUpdateListener(onWeatherUpdateListener: OnWeatherUpdateListener)

    fun loadCurrentWeather()

    fun loadForecastWeather()

    fun searchForecast(forecast: IWeatherForecast, searchValue: String): IWeatherForecast

    fun dispose()
}