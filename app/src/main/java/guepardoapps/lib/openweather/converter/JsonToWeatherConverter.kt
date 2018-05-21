package guepardoapps.lib.openweather.converter

import guepardoapps.lib.openweather.enums.WeatherCondition
import guepardoapps.lib.openweather.models.WeatherCurrent
import guepardoapps.lib.openweather.models.WeatherForecast
import java.util.*

class JsonToWeatherConverter : IJsonToWeatherConverter {
    override fun ConvertToWeatherCurrent(jsonString: String): WeatherCurrent {
        var weatherCurrent: WeatherCurrent = WeatherCurrent(
                "", "", "",
                -273.15, 0.0 , 0.0,
                Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(),
                WeatherCondition.Null)

        try {

        } catch (exception: Exception) {

        } finally {
            return weatherCurrent
        }
    }

    override fun ConvertToWeatherForecast(jsonString: String): WeatherForecast {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}