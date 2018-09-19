package guepardoapps.lib.openweather.models

import guepardoapps.lib.openweather.annotations.JsonKey

@JsonKey("results", "address_components")
internal class AddressComponent {
    private val tag: String = AddressComponent::class.java.simpleName

    @JsonKey("address_components", "short_name")
    var shortName: String = ""

    @JsonKey("address_components", "types")
    var types: Array<String> = arrayOf()

    @JsonKey("address_components", "long_name")
    var longName: String = ""

    override fun toString(): String {
        return "{Class: $tag, ShortName: $shortName, Types: $types, LongName: $longName}"
    }
}