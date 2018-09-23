package guepardoapps.lib.openweather.models

import guepardoapps.lib.openweather.enums.DownloadType

internal data class ApiResponse(val downloadType: DownloadType, val jsonString: String, val success: Boolean)