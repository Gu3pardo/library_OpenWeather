package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey
import com.github.openweather.library.extensions.empty

@JsonKey("results", "address_components")
internal class AddressComponent : JsonModel {
    private val tag: String = AddressComponent::class.java.simpleName

    @JsonKey("address_components", "short_name")
    var shortName: String = String.empty

    @JsonKey("address_components", "types")
    var types: Array<String> = arrayOf()

    @JsonKey("address_components", "long_name")
    var longName: String = String.empty

    override fun toString(): String = "{Class: $tag, ShortName: $shortName, Types: $types, LongName: $longName}"
}