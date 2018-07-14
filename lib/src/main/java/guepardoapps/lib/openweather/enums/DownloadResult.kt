package guepardoapps.lib.openweather.enums

import java.io.Serializable

internal enum class DownloadResult : Serializable {
    InvalidCity, InvalidApiKey, Performing
}