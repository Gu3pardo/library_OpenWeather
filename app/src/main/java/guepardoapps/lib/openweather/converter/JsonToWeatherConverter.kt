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
                Logger.instance.error(tag,
                        "Error in parsing jsonObject in convertToWeatherCurrent: $jsonObject")
                return null
            }

            val sys = jsonObject.getJSONObject("sys")

            val coordinationJsonObject = jsonObject.getJSONObject("coord")
            val latitude = coordinationJsonObject.getDouble("lat")
            val longitude = coordinationJsonObject.getDouble("lon")
            val geoLocation = GeoLocation(latitude, longitude)

            val cityId = jsonObject.getInt("id")
            val cityName = jsonObject.getString("name").toUpperCase(Locale.getDefault())
            val cityCountry = sys.getString("country")
            val city = City(cityId, cityName, cityCountry, geoLocation)

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

            return WeatherCurrent(city, description,
                    temperature, humidity, pressure,
                    sunriseCalendar, sunsetCalendar,
                    Calendar.getInstance(),
                    weatherCondition)
        } catch (exception: Exception) {
            Logger.instance.error(tag, exception)
            return null
        }
    }

    override fun convertToWeatherForecast(jsonString: String): WeatherForecast? {
        try {
            val jsonObject = JSONObject(jsonString)
            if (jsonObject.getInt("cod") != 200) {
                Logger.instance.error(tag,
                        "Error in parsing jsonObject in convertToWeatherForecast: $jsonObject")
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
            val city = City(cityId, cityName, cityCountry, geoLocation, cityPopulation)

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
                val weatherForecastPart = convertToWeatherForecastPart(dataJsonObject)
                if (weatherForecastPart != null) {
                    list.plus(weatherForecastPart)
                }

                previousDateString = currentDateString
            }

            return WeatherForecast(city, list)
        } catch (exception: Exception) {
            Logger.instance.error(tag, exception)
            return null
        }
    }

    private fun convertToWeatherForecastPart(jsonObject: JSONObject): WeatherForecastPart? {
        try {
            val jsonObjectWeather = jsonObject.getJSONArray("weather").getJSONObject(0)

            val main = jsonObjectWeather.getString("main")
            val weatherCondition = WeatherCondition.valueOf(main)
            val description = jsonObjectWeather.getString("description")
            val weatherDefaultIcon = jsonObjectWeather.getString("icon")

            val temp = jsonObject.getJSONObject("main").getDouble("temp")
            val tempMin = jsonObject.getJSONObject("main").getDouble("temp_min")
            val tempMax = jsonObject.getJSONObject("main").getDouble("temp_max")
            val tempKf = jsonObject.getJSONObject("main").getDouble("temp_kf")
            val pressure = jsonObject.getJSONObject("main").getDouble("pressure")
            val pressureSeaLevel = jsonObject.getJSONObject("main").getDouble("sea_level")
            val pressureGroundLevel = jsonObject.getJSONObject("main").getDouble("grnd_level")
            val humidity = jsonObject.getJSONObject("main").getDouble("humidity")

            val cloudsAll = jsonObject.getJSONObject("clouds").getInt("all")

            val windSpeed = jsonObject.getJSONObject("wind").getDouble("speed")
            val windDegree = jsonObject.getJSONObject("wind").getDouble("deg")

            val dateTimeLong = jsonObject.getLong("dt")
            val dateTime = Calendar.getInstance()
            dateTime.timeInMillis = dateTimeLong

            return WeatherForecastPart(main, description,
                    temp, tempMin, tempMax, tempKf,
                    humidity,
                    pressure, pressureSeaLevel, pressureGroundLevel,
                    cloudsAll, windSpeed, windDegree,
                    dateTime,
                    weatherDefaultIcon, weatherCondition)

        } catch (exception: Exception) {
            Logger.instance.error(tag, exception)
            return null
        }
    }
}