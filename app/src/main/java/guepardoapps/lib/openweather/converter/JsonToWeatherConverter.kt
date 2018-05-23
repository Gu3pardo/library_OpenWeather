package guepardoapps.lib.openweather.converter

import guepardoapps.lib.openweather.R
import guepardoapps.lib.openweather.enums.WeatherCondition
import guepardoapps.lib.openweather.models.*
import java.util.*

class JsonToWeatherConverter : IJsonToWeatherConverter {
    override fun convertToWeatherCurrent(jsonString: String): WeatherCurrent {
        var weatherCurrent: WeatherCurrent = WeatherCurrent(
                "", "", "",
                -273.15, 0.0, 0.0,
                Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(),
                WeatherCondition.Null)

        try {

        } catch (exception: Exception) {

        } finally {
            return weatherCurrent
        }
    }

    override fun convertToWeatherForecast(jsonString: String): WeatherForecast {
        var geoLocation = GeoLocation(0.0, 0.0)
        var city = City(0, "", "", 0, geoLocation)
        var weatherForecastPartList: Array<IWeatherForecastPart> = arrayOf()
        var weatherForecast = WeatherForecast(
                city,
                R.drawable.weather_wallpaper_dummy,
                weatherForecastPartList)

        try {

        } catch (exception: Exception) {

        } finally {
            return weatherForecast
        }
    }
}