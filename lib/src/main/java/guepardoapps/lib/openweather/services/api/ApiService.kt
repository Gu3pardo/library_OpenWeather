package guepardoapps.lib.openweather.services.api

import guepardoapps.lib.openweather.enums.DownloadResult
import guepardoapps.lib.openweather.enums.DownloadType
import guepardoapps.lib.openweather.extensions.isDefault
import guepardoapps.lib.openweather.logging.Logger
import guepardoapps.lib.openweather.models.City
import guepardoapps.lib.openweather.tasks.ApiRestCallTask

internal class ApiService : IApiService {
    private val tag: String = ApiService::class.java.simpleName

    private val geoCodeForCityUrl: String = "http://www.datasciencetoolkit.org/maps/api/geocode/json?address=%s"
    private val currentWeatherUrl: String = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&APPID=%s"
    private val forecastWeatherUrl: String = "http://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric&APPID=%s"
    private val uvIndexUrl: String = "http://api.openweathermap.org/data/2.5/uvi?lat=%.2f&lon=%.2f&APPID=%s"

    private lateinit var onApiServiceListener: OnApiServiceListener

    override fun setOnApiServiceListener(onApiServiceListener: OnApiServiceListener) {
        this.onApiServiceListener = onApiServiceListener
    }

    override fun geoCodeForCity(cityName: String): DownloadResult {
        return this.doApiRestCall(DownloadType.City, String.format(this.geoCodeForCityUrl, cityName))
    }

    override fun currentWeather(apiKey: String, city: City): DownloadResult {
        return this.doApiRestCall(DownloadType.Current, String.format(this.currentWeatherUrl, city.name, apiKey))
    }

    override fun forecastWeather(apiKey: String, city: City): DownloadResult {
        return this.doApiRestCall(DownloadType.Forecast, String.format(this.forecastWeatherUrl, city.name, apiKey))
    }

    override fun uvIndex(apiKey: String, city: City): DownloadResult {
        if (city.geoLocation.isDefault()) {
            Logger.instance.warning(tag, "uvIndex: City.geoLocation needs to be set before call!")
            return DownloadResult.InvalidCity
        }

        return this.doApiRestCall(DownloadType.UvIndex, String.format(this.uvIndexUrl, city.geoLocation.latitude, city.geoLocation.longitude, apiKey))
    }

    private fun doApiRestCall(downloadType: DownloadType, url: String): DownloadResult {
        val task = ApiRestCallTask()
        task.downloadType = downloadType
        task.onApiServiceListener = this.onApiServiceListener
        task.execute(url)
        return DownloadResult.Performing
    }
}