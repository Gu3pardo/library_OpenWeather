package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey
import com.github.openweather.library.common.Constants
import com.github.openweather.library.extensions.empty

@JsonKey("", "city")
class City : JsonModel {
    private val tag: String = City::class.java.simpleName

    @JsonKey("city", "id")
    var id: Int = Constants.Defaults.Zero

    @JsonKey("city", "name")
    var name: String = String.empty

    @JsonKey("city", "country")
    var country: String = String.empty

    @JsonKey("city", "population")
    var population: Int = Constants.Defaults.Zero

    var coordinates: Coordinates = Coordinates()

    override fun toString(): String = "{Class: $tag, Id: $id, Name: $name, Country: $country, Coordinates: $coordinates, Population: $population}"
}