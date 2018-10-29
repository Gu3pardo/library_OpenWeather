package com.github.openweather.library.services.api

import com.github.openweather.library.enums.DownloadType

internal interface OnApiServiceListener {
    fun onFinished(downloadType: DownloadType, jsonString: String, success: Boolean)
}