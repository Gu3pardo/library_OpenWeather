package guepardoapps.lib.openweather.models

class GeoLocation(private val latitude: Double,
                  private val longitude: Double) : IGeoLocation {

    private val tag: String = GeoLocation::class.java.simpleName

    override fun getLatitude(): Double {
        return latitude
    }

    override fun getLongitude(): Double {
        return longitude
    }

    override fun toString(): String {
        return "{Class: $tag, Latitude: $latitude, Longitude: $longitude}"
    }
}