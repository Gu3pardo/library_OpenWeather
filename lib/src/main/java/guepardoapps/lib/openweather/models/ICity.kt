package guepardoapps.lib.openweather.models

import java.io.Serializable

interface ICity : Serializable {
    fun getId(): Int

    fun getName(): String

    fun getCountry(): String

    fun getPopulation(): Int

    fun getGeoLocation(): IGeoLocation
}