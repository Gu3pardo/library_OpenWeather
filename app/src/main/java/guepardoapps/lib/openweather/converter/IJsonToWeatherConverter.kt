package guepardoapps.lib.openweather.converter

import android.support.annotation.NonNull
import guepardoapps.lib.openweather.models.IWeatherCurrent
import guepardoapps.lib.openweather.models.IWeatherForecast
import java.io.Serializable

interface IJsonToWeatherConverter : Serializable {
    fun convertToWeatherCurrent(@NonNull jsonString: String): IWeatherCurrent

    fun convertToWeatherForecast(@NonNull jsonString: String): IWeatherForecast
}