package guepardoapps.lib.openweather.downloader

import guepardoapps.lib.openweather.enums.DownloadType

interface OnDownloadListener {
    fun onFinished(downloadType: DownloadType, jsonString: String, success: Boolean)
}