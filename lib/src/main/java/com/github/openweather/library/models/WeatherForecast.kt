package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey

@JsonKey("", "")
class WeatherForecast : JsonModel {
    private val tag: String = WeatherForecast::class.java.simpleName

    var city: City = City()

    var list: List<WeatherForecastPart> = listOf()

    override fun toString(): String = "{Class: $tag, City: $city, List: $list}"
}