package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey
import com.github.openweather.library.common.Constants

@JsonKey(Constants.String.Empty, "results")
internal class City2 : JsonModel {
    private val tag: String = City2::class.java.simpleName

    @JsonKey("results", "address_components")
    var addressComponents: Array<AddressComponent> = arrayOf()

    @JsonKey("results", "geometry")
    var geometry: Geometry = Geometry()

    @JsonKey("results", "types")
    var types: Array<String> = arrayOf()

    override fun toString(): String {
        return "{Class: $tag, AddressComponents: $addressComponents, Geometry: $geometry, tTypes: $types}"
    }
}