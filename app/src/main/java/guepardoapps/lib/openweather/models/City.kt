package guepardoapps.lib.openweather.models

class City(private val id: Int,
           private val name: String,
           private val country: String,
           private val geoLocation: IGeoLocation,
           private val population: Int = 0) : ICity {

    private val tag: String = City::class.java.canonicalName

    override fun getId(): Int {
        return id
    }

    override fun getName(): String {
        return name
    }

    override fun getCountry(): String {
        return country
    }

    override fun getGeoLocation(): IGeoLocation {
        return geoLocation
    }

    override fun getPopulation(): Int {
        return population
    }

    override fun toString(): String {
        return "{Class: $tag, Id: $id, Name: $name, Country: $country, GeoLocation: $geoLocation, Population: $population}"
    }
}