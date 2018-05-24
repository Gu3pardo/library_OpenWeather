package guepardoapps.lib.openweather.converter

import guepardoapps.lib.openweather.enums.WeatherCondition
import guepardoapps.lib.openweather.models.*
import guepardoapps.lib.openweather.utils.Logger
import org.json.JSONObject
import java.util.*

class JsonToWeatherConverter : IJsonToWeatherConverter {
    private val tag: String = JsonToWeatherConverter::class.java.simpleName

    override fun convertToWeatherCurrent(jsonString: String): WeatherCurrent? {
        try {
            val jsonObject = JSONObject(jsonString)
            if (jsonObject.getInt("cod") != 200) {
                Logger.instance.error(tag, "Error in parsing jsonObject in convertToWeatherCurrent: $jsonObject")
                return null
            }

            val sys = jsonObject.getJSONObject("sys")

            val city = jsonObject.getString("name").toUpperCase(Locale.getDefault())
            val country = sys.getString("country")

            val sunriseLong = sys.getLong("sunrise") * 1000
            val sunriseCalendar = Calendar.getInstance()
            sunriseCalendar.timeInMillis = sunriseLong

            val sunsetLong = sys.getLong("sunset") * 1000
            val sunsetCalendar = Calendar.getInstance()
            sunsetCalendar.timeInMillis = sunsetLong

            val details = jsonObject.getJSONArray("weather").getJSONObject(0)
            val description = details.getString("description").toUpperCase(Locale.getDefault())
            val weatherCondition = WeatherCondition.valueOf(description)

            val main = jsonObject.getJSONObject("main")
            val temperature = main.getDouble("temp")
            val humidity = main.getDouble("humidity")
            val pressure = main.getDouble("pressure")

            return WeatherCurrent(city, country, description, temperature, humidity, pressure, sunriseCalendar, sunsetCalendar, Calendar.getInstance(), weatherCondition)
        } catch (exception: Exception) {
            Logger.instance.error(tag, exception)
            return null
        }
    }

    override fun convertToWeatherForecast(jsonString: String): WeatherForecast? {
        try {
            val jsonObject = JSONObject(jsonString)
            if (jsonObject.getInt("cod") != 200) {
                Logger.instance.error(tag, "Error in parsing jsonObject in convertToWeatherForecast: $jsonObject")
                return null
            }

            val coordinationJsonObject = jsonObject.getJSONObject("coord")
            val latitude = coordinationJsonObject.getDouble("lat")
            val longitude = coordinationJsonObject.getDouble("lon")
            val geoLocation = GeoLocation(latitude, longitude)

            val cityJsonObject = jsonObject.getJSONObject("city")
            val cityId = cityJsonObject.getInt("id")
            val cityName = cityJsonObject.getString("name")
            val cityCountry = cityJsonObject.getString("country")
            val cityPopulation = cityJsonObject.getInt("population")
            val city = City(cityId, cityName, cityCountry, cityPopulation, geoLocation)

            val dataListJsonArray = jsonObject.getJSONArray("list")
            val list = arrayOf<IWeatherForecastPart>()

            var previousDateString = ""

            for (index in 0 until dataListJsonArray.length() step 1) {
                val dataJsonObject = dataListJsonArray.getJSONObject(index)

                val dateTimeLong = dataJsonObject.getLong("dt")
                val dateTime = Calendar.getInstance()
                dateTime.timeInMillis = dateTimeLong

                val currentDateString = dataJsonObject.getString("dt_txt").split(" ")[0]

                if (index == 0) {
                    list.plus(WeatherForecastPart(dateTime))
                }

                if (!currentDateString.contains(previousDateString)) {
                    list.plus(WeatherForecastPart(dateTime))
                }
                list.plus(convertToWeatherForecastPart(dataJsonObject))

                previousDateString = currentDateString
            }

            return WeatherForecast(city, 0/*TODO*/, list)
        } catch (exception: Exception) {
            Logger.instance.error(tag, exception)
            return null
        }
    }

    private fun convertToWeatherForecastPart(jsonObject: JSONObject): WeatherForecastPart {
        /*TODO*/
        return WeatherForecastPart(Calendar.getInstance())
    }
}