package guepardoapps.lib.openweather.converter

import android.support.annotation.NonNull
import guepardoapps.lib.openweather.models.IWeatherCurrent
import guepardoapps.lib.openweather.models.IWeatherForecast
import java.io.Serializable

interface IJsonToWeatherConverter : Serializable {
    fun ConvertToWeatherCurrent(@NonNull jsonString: String): IWeatherCurrent

    fun ConvertToWeatherForecast(@NonNull jsonString: String): IWeatherForecast
}