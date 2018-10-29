package com.github.openweather.library.converter

import androidx.annotation.NonNull
import com.github.openweather.library.models.*
import java.io.Serializable

internal interface IJsonToWeatherConverter : Serializable {
    fun convertToCity(@NonNull jsonString: String): City2?

    fun convertToWeatherCurrent(@NonNull jsonString: String): WeatherCurrent?

    fun convertToWeatherForecast(@NonNull jsonString: String): WeatherForecast?

    fun convertToUvIndex(@NonNull jsonString: String): UvIndex?

    fun convertToCarbonMonoxide(@NonNull jsonString: String): CarbonMonoxide?

    fun convertToNitrogenDioxide(@NonNull jsonString: String): NitrogenDioxide?

    fun convertToOzone(@NonNull jsonString: String): Ozone?

    fun convertToSulfurDioxide(@NonNull jsonString: String): SulfurDioxide?
}