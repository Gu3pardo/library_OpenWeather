package guepardoapps.lib.openweather.converter

import android.support.annotation.NonNull
import guepardoapps.lib.openweather.models.WeatherCurrent
import guepardoapps.lib.openweather.models.WeatherForecast
import java.io.Serializable

internal interface IJsonToWeatherConverter : Serializable {
    fun convertToWeatherCurrent(@NonNull jsonString: String): WeatherCurrent?
    fun convertToWeatherForecast(@NonNull jsonString: String): WeatherForecast?
}