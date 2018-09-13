package guepardoapps.lib.openweather.services.api

import guepardoapps.lib.openweather.enums.DownloadResult
import guepardoapps.lib.openweather.enums.DownloadType
import guepardoapps.lib.openweather.extensions.isDefault
import guepardoapps.lib.openweather.logging.Logger
import guepardoapps.lib.openweather.models.City
import guepardoapps.lib.openweather.tasks.ApiRestCallTask

internal class ApiService(override var city: City, override var apiKey: String) : IApiService {
    private val tag: String = ApiService::class.java.simpleName

    private val currentWeatherUrl: String = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&APPID=%s"
    private val forecastWeatherUrl: String = "http://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric&APPID=%s"
    private val uvIndexUrl: String = "http://api.openweathermap.org/data/2.5/uvi?lat=%.2f&lon=%.2f&APPID=%s"

    private lateinit var onApiServiceListener: OnApiServiceListener

    constructor() : this(City(), "")

    override fun setOnApiServiceListener(onApiServiceListener: OnApiServiceListener) {
        this.onApiServiceListener = onApiServiceListener
    }

    override fun currentWeather(): DownloadResult {
        return this.doApiRestCall(DownloadType.Current, String.format(this.currentWeatherUrl, this.city.name, this.apiKey))
    }

    override fun forecastWeather(): DownloadResult {
        return this.doApiRestCall(DownloadType.Forecast, String.format(this.forecastWeatherUrl, this.city.name, this.apiKey))
    }

    override fun uvIndex(): DownloadResult {
        if (this.city.geoLocation.isDefault()) {
            Logger.instance.warning(tag, "uvIndex: City.geoLocation needs to be set before call!")
            return DownloadResult.InvalidCity
        }

        return this.doApiRestCall(DownloadType.UvIndex, String.format(this.uvIndexUrl, this.city.geoLocation.latitude, this.city.geoLocation.longitude, this.apiKey))
    }

    private fun doApiRestCall(downloadType: DownloadType, url: String): DownloadResult {
        if (this.city.isDefault()) {
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
        task.execute(url)

        return DownloadResult.Performing
    }
}