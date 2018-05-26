package guepardoapps.lib.openweather.enums

import java.io.Serializable

enum class DownloadResult : Serializable {
    InvalidCity, InvalidApiKey, Performing
}