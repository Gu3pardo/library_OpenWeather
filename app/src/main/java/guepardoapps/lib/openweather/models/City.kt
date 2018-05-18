package guepardoapps.lib.openweather.models

class City(private val id: Int,
           private val name: String,
           private val country: String,
           private val population: Int,
           private val geoLocation: IGeoLocation) : ICity {

    override fun getId(): Int {
        return id
    }

    override fun getName(): String {
        return name
    }

    override fun getCountry(): String {
        return country
    }

    override fun getPopulation(): Int {
        return population
    }

    override fun getGeoLocation(): IGeoLocation {
        return geoLocation
    }
}