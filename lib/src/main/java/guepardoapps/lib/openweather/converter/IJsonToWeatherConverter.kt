package guepardoapps.lib.openweather.converter

import androidx.annotation.NonNull
import guepardoapps.lib.openweather.models.City2
import guepardoapps.lib.openweather.models.UvIndex
import guepardoapps.lib.openweather.models.WeatherCurrent
import guepardoapps.lib.openweather.models.WeatherForecast
import java.io.Serializable

internal interface IJsonToWeatherConverter : Serializable {
    fun convertToCity(@NonNull jsonString: String): City2?
    fun convertToWeatherCurrent(@NonNull jsonString: String): WeatherCurrent?
    fun convertToWeatherForecast(@NonNull jsonString: String): WeatherForecast?
    fun convertToUvIndex(@NonNull jsonString: String): UvIndex?
}