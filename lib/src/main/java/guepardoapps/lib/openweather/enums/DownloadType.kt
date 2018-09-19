package guepardoapps.lib.openweather.enums

import java.io.Serializable

internal enum class DownloadType : Serializable {
    Null,
    City,
    Current,
    Forecast,
    UvIndex
}