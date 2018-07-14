package guepardoapps.lib.openweather.services.api

import guepardoapps.lib.openweather.enums.DownloadType

internal interface OnApiServiceListener {
    fun onFinished(downloadType: DownloadType, jsonString: String, success: Boolean)
}