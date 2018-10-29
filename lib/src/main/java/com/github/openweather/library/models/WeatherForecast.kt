package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey
import com.github.openweather.library.common.Constants

@JsonKey(Constants.String.Empty, Constants.String.Empty)
class WeatherForecast : JsonModel {
    private val tag: String = WeatherForecast::class.java.simpleName

    var city: City = City()

    var list: List<WeatherForecastPart> = listOf()

    override fun toString(): String {
        return "{Class: $tag, City: $city, List: $list}"
    }
}