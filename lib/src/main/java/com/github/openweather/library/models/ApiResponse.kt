package com.github.openweather.library.models

import com.github.openweather.library.enums.DownloadType

internal data class ApiResponse(val downloadType: DownloadType, val jsonString: String, val success: Boolean)