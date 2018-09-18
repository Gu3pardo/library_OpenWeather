package guepardoapps.lib.openweather.models

import guepardoapps.lib.openweather.annotations.JsonKey

@JsonKey("", "city")
class City {
    private val tag: String = City::class.java.simpleName

    @JsonKey("city", "id")
    var id: Int = 0

    @JsonKey("city", "name")
    var name: String = ""

    @JsonKey("city", "country")
    var country: String = ""

    @JsonKey("city", "population")
    var population: Int = 0

    var coordinates: Coordinates = Coordinates()

    override fun toString(): String {
        return "{Class: $tag, Id: $id, Name: $name, Country: $country, Coordinates: $coordinates, Population: $population}"
    }
}