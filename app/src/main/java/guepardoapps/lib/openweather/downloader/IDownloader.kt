package guepardoapps.lib.openweather.downloader

import guepardoapps.lib.openweather.enums.DownloadResult

interface IDownloader {
    var city: String?

    var apiKey: String?

    fun setOnDownloadListener(onDownloadListener: OnDownloadListener)

    fun currentWeather(): DownloadResult

    fun forecastWeather(): DownloadResult
}