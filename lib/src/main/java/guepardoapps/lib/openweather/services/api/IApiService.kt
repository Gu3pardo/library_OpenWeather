package guepardoapps.lib.openweather.services.api

import guepardoapps.lib.openweather.enums.DownloadResult

internal interface IApiService {
    var city: String
    var apiKey: String

    fun setOnApiServiceListener(onApiServiceListener: OnApiServiceListener)
    fun currentWeather(): DownloadResult
    fun forecastWeather(): DownloadResult
}