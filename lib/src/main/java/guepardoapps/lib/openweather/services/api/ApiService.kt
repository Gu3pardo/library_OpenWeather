package guepardoapps.lib.openweather.services.api

import guepardoapps.lib.openweather.enums.DownloadResult
import guepardoapps.lib.openweather.enums.DownloadType
import guepardoapps.lib.openweather.tasks.ApiRestCallTask
import guepardoapps.lib.openweather.utils.Logger

class ApiService(override var city: String, override var apiKey: String) : IApiService {
    private val tag: String = ApiService::class.java.simpleName

    private val currentWeatherUrl: String = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&APPID=%s"
    private val forecastWeatherUrl: String = "http://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric&APPID=%s"

    private lateinit var onApiServiceListener: OnApiServiceListener

    constructor() : this("", "")

    override fun setOnApiServiceListener(onApiServiceListener: OnApiServiceListener) {
        this.onApiServiceListener = onApiServiceListener
    }

    override fun currentWeather(): DownloadResult {
        if (city.isEmpty()) {
            Logger.instance.warning(tag, "currentWeather: City needs to be set before call!")
            return DownloadResult.InvalidCity
        }

        if (apiKey.isEmpty()) {
            Logger.instance.warning(tag, "currentWeather: ApiKey needs to be set before call!")
            return DownloadResult.InvalidApiKey
        }

        val task = ApiRestCallTask()
        task.downloadType = DownloadType.Current
        task.onApiServiceListener = onApiServiceListener
        task.execute(String.format(currentWeatherUrl, city, apiKey))

        return DownloadResult.Performing
    }

    override fun forecastWeather(): DownloadResult {
        if (city.isEmpty()) {
            Logger.instance.warning(tag, "forecastWeather: City needs to be set before call!")
            return DownloadResult.InvalidCity
        }

        if (apiKey.isEmpty()) {
            Logger.instance.warning(tag, "forecastWeather: ApiKey needs to be set before call!")
            return DownloadResult.InvalidApiKey
        }

        val task = ApiRestCallTask()
        task.downloadType = DownloadType.Forecast
        task.onApiServiceListener = onApiServiceListener
        task.execute(String.format(forecastWeatherUrl, city, apiKey))

        return DownloadResult.Performing
    }
}