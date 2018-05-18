package guepardoapps.lib.openweather.models

class GeoLocation(private val latitude: Double,
                  private val longitude: Double) : IGeoLocation {

    override fun getLatitude(): Double {
        return latitude
    }

    override fun getLongitude(): Double {
        return longitude
    }
}