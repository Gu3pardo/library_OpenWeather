package guepardoapps.lib.openweather.services.api

import guepardoapps.lib.openweather.enums.DownloadResult
import guepardoapps.lib.openweather.models.City

internal interface IApiService {
    var city: City
    var apiKey: String

    fun setOnApiServiceListener(onApiServiceListener: OnApiServiceListener)
    fun currentWeather(): DownloadResult
    fun forecastWeather(): DownloadResult
    fun uvIndex(): DownloadResult
}