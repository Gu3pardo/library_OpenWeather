package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey
import com.github.openweather.library.common.Constants

@JsonKey(Constants.String.Empty, "city")
class City : JsonModel {
    private val tag: String = City::class.java.simpleName

    @JsonKey("city", "id")
    var id: Int = Constants.Defaults.Zero

    @JsonKey("city", "name")
    var name: String = Constants.String.Empty

    @JsonKey("city", "country")
    var country: String = Constants.String.Empty

    @JsonKey("city", "population")
    var population: Int = Constants.Defaults.Zero

    var coordinates: Coordinates = Coordinates()

    override fun toString(): String {
        return "{Class: $tag, Id: $id, Name: $name, Country: $country, Coordinates: $coordinates, Population: $population}"
    }
}