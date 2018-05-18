package guepardoapps.lib.openweather.models

import java.io.Serializable

interface IGeoLocation : Serializable {
    fun getLatitude(): Double

    fun getLongitude(): Double
}