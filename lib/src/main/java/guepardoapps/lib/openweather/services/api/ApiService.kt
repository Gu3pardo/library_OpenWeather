package guepardoapps.lib.openweather.services.api

import guepardoapps.lib.openweather.enums.DownloadResult
import guepardoapps.lib.openweather.enums.DownloadType
import guepardoapps.lib.openweather.tasks.ApiRestCallTask
import guepardoapps.lib.openweather.utils.Logger

internal class ApiService(override var city: String, override var apiKey: String) : IApiService {
    private val tag: String = ApiService::class.java.simpleName

    private val currentWeatherUrl: String = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&APPID=%s"
    private val forecastWeatherUrl: String = "http://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric&APPID=%s"

    private lateinit var onApiServiceListener: OnApiServiceListener

    constructor() : this("", "")

    override fun setOnApiServiceListener(onApiServiceListener: OnApiServiceListener) {
        this.onApiServiceListener = onApiServiceListener
    }

    override fun currentWeather(): DownloadResult {
        return this.doApiRestCall(DownloadType.Current, this.currentWeatherUrl)
    }

    override fun forecastWeather(): DownloadResult {
        return this.doApiRestCall(DownloadType.Forecast, this.forecastWeatherUrl)
    }

    private fun doApiRestCall(
            downloadType: DownloadType,
            url: String)
            : DownloadResult {
        if (this.city.isEmpty()) {
            Logger.instance.warning(tag, "doApiRestCall: City needs to be set before call!")
            return DownloadResult.InvalidCity
        }

        if (this.apiKey.isEmpty()) {
            Logger.instance.warning(tag, "doApiRestCall: ApiKey needs to be set before call!")
            return DownloadResult.InvalidApiKey
        }

        val task = ApiRestCallTask()
        task.downloadType = downloadType
        task.onApiServiceListener = this.onApiServiceListener
        task.execute(String.format(url, this.city, this.apiKey))

        return DownloadResult.Performing
    }
}