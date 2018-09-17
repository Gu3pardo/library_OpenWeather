package guepardoapps.lib.openweather.services.api

import guepardoapps.lib.openweather.enums.DownloadResult
import guepardoapps.lib.openweather.models.City

internal interface IApiService {
    fun setOnApiServiceListener(onApiServiceListener: OnApiServiceListener)
    fun geoCodeForCity(cityName: String): DownloadResult;
    fun currentWeather(apiKey: String, city: City): DownloadResult
    fun forecastWeather(apiKey: String, city: City): DownloadResult
    fun uvIndex(apiKey: String, city: City): DownloadResult
}