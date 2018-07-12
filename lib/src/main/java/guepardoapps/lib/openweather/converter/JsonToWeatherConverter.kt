package guepardoapps.lib.openweather.converter

import guepardoapps.lib.openweather.enums.ForecastListType
import guepardoapps.lib.openweather.enums.WeatherCondition
import guepardoapps.lib.openweather.extensions.getJsonKey
import guepardoapps.lib.openweather.extensions.getPropertyJsonKey
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
            val weatherCurrent = WeatherCurrent()

            val sys = jsonObject
                    .getJSONObject(weatherCurrent.getJsonKey().key)

            val geoLocation = GeoLocation()
            val coordinationJsonObject = jsonObject
                    .getJSONObject(geoLocation.getJsonKey().key)
            geoLocation.latitude = coordinationJsonObject
                    .getDouble(geoLocation.getPropertyJsonKey(geoLocation::latitude.name).key)
            geoLocation.longitude = coordinationJsonObject
                    .getDouble(geoLocation.getPropertyJsonKey(geoLocation::longitude.name).key)

            val city = City()
            city.id = jsonObject
                    .getInt(city.getPropertyJsonKey(city::id.name).key)
            city.name = jsonObject
                    .getString(city.getPropertyJsonKey(city::name.name).key)
            city.country = sys
                    .getString(city.getPropertyJsonKey(city::country.name).key)
            city.geoLocation = geoLocation

            weatherCurrent.city = city

            weatherCurrent.sunriseTime.timeInMillis = sys
                    .getLong(weatherCurrent.getPropertyJsonKey(weatherCurrent::sunriseTime.name).key) * 1000

            weatherCurrent.sunsetTime.timeInMillis = sys
                    .getLong(weatherCurrent.getPropertyJsonKey(weatherCurrent::sunsetTime.name).key) * 1000

            val details = jsonObject
                    .getJSONArray(weatherCurrent.getPropertyJsonKey(weatherCurrent::description.name).parent).getJSONObject(0)
            weatherCurrent.description = details
                    .getString(weatherCurrent.getPropertyJsonKey(weatherCurrent::description.name).key)
            val main = details
                    .getString(weatherCurrent.getPropertyJsonKey(weatherCurrent::weatherCondition.name).key)
            weatherCurrent.weatherCondition = WeatherCondition.valueOf(main)

            val mainJSONObject = jsonObject
                    .getJSONObject(weatherCurrent.getPropertyJsonKey(weatherCurrent::temperature.name).parent)
            weatherCurrent.temperature = mainJSONObject
                    .getDouble(weatherCurrent.getPropertyJsonKey(weatherCurrent::temperature.name).key)
            weatherCurrent.humidity = mainJSONObject
                    .getDouble(weatherCurrent.getPropertyJsonKey(weatherCurrent::humidity.name).key)
            weatherCurrent.pressure = mainJSONObject
                    .getDouble(weatherCurrent.getPropertyJsonKey(weatherCurrent::pressure.name).key)

            weatherCurrent.lastUpdate = Calendar.getInstance()

            return weatherCurrent
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
            val weatherForecast = WeatherForecast()

            val city = City()
            val cityJsonObject = jsonObject
                    .getJSONObject(city.getJsonKey().key)
            city.id = cityJsonObject
                    .getInt(city.getPropertyJsonKey(city::id.name).key)
            city.name = cityJsonObject
                    .getString(city.getPropertyJsonKey(city::name.name).key)
            city.country = cityJsonObject
                    .getString(city.getPropertyJsonKey(city::country.name).key)
            city.population = cityJsonObject
                    .getInt(city.getPropertyJsonKey(city::population.name).key)

            val geoLocation = GeoLocation()
            val coordinationJsonObject = cityJsonObject
                    .getJSONObject(geoLocation.getJsonKey().key)
            geoLocation.latitude = coordinationJsonObject
                    .getDouble(geoLocation.getPropertyJsonKey(geoLocation::latitude.name).key)
            geoLocation.longitude = coordinationJsonObject
                    .getDouble(geoLocation.getPropertyJsonKey(geoLocation::longitude.name).key)

            city.geoLocation = geoLocation

            weatherForecast.city = city

            val weatherForecastPartC = WeatherForecastPart()
            val dataListJsonArray = jsonObject.getJSONArray(weatherForecastPartC.getJsonKey().parent)
            var list = listOf<WeatherForecastPart>()

            var previousDateString = ""

            for (index in 0 until dataListJsonArray.length() step 1) {
                val dataJsonObject = dataListJsonArray.getJSONObject(index)

                val currentDateString = dataJsonObject.getString("dt_txt").split(" ")[0]

                if (index == 0 || !currentDateString.contains(previousDateString)) {
                    val weatherForecastPartDivider = WeatherForecastPart()
                    weatherForecastPartDivider.listType = ForecastListType.DateDivider

                    val dateTimeLong = dataJsonObject
                            .getLong(weatherForecastPartDivider.getPropertyJsonKey(weatherForecastPartDivider::dateTime.name).key)
                    weatherForecastPartDivider.dateTime.timeInMillis = dateTimeLong * 1000

                    list = list.plus(weatherForecastPartDivider)
                }

                val weatherForecastPart = convertToWeatherForecastPart(dataJsonObject)
                if (weatherForecastPart != null) {
                    list = list.plus(weatherForecastPart)
                }

                previousDateString = currentDateString
            }

            weatherForecast.list = list

            return weatherForecast
        } catch (exception: Exception) {
            Logger.instance.error(tag, exception)
            return null
        }
    }

    private fun convertToWeatherForecastPart(jsonObject: JSONObject): WeatherForecastPart? {
        try {
            val weatherForecastPart = WeatherForecastPart()

            val jsonObjectWeather = jsonObject
                    .getJSONArray(weatherForecastPart.getJsonKey().key).getJSONObject(0)

            weatherForecastPart.main = jsonObjectWeather
                    .getString(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::main.name).key)
            weatherForecastPart.weatherCondition = WeatherCondition.valueOf(weatherForecastPart.main)
            weatherForecastPart.description = jsonObjectWeather
                    .getString(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::description.name).key)
            weatherForecastPart.weatherDefaultIcon = jsonObjectWeather
                    .getString(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::weatherDefaultIcon.name).key)

            val jsonObjectMain = jsonObject
                    .getJSONObject(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::temperature.name).parent)

            weatherForecastPart.temperature = jsonObjectMain
                    .getDouble(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::temperature.name).key)
            weatherForecastPart.temperatureMin = jsonObjectMain
                    .getDouble(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::temperatureMin.name).key)
            weatherForecastPart.temperatureMax = jsonObjectMain
                    .getDouble(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::temperatureMax.name).key)
            weatherForecastPart.temperatureKf = jsonObjectMain
                    .getDouble(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::temperatureKf.name).key)
            weatherForecastPart.pressure = jsonObjectMain
                    .getDouble(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::pressure.name).key)
            weatherForecastPart.pressureSeaLevel = jsonObjectMain
                    .getDouble(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::pressureSeaLevel.name).key)
            weatherForecastPart.pressureGroundLevel = jsonObjectMain
                    .getDouble(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::pressureGroundLevel.name).key)
            weatherForecastPart.humidity = jsonObjectMain
                    .getDouble(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::humidity.name).key)

            weatherForecastPart.cloudsAll = jsonObject
                    .getJSONObject(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::cloudsAll.name).parent)
                    .getInt(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::cloudsAll.name).key)

            weatherForecastPart.windSpeed = jsonObject
                    .getJSONObject(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::windSpeed.name).parent)
                    .getDouble(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::windSpeed.name).key)
            weatherForecastPart.windDegree = jsonObject
                    .getJSONObject(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::windDegree.name).parent)
                    .getDouble(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::windDegree.name).key)

            weatherForecastPart.dateTime.timeInMillis = jsonObject
                    .getLong(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::dateTime.name).key) * 1000

            weatherForecastPart.listType = ForecastListType.Forecast

            return weatherForecastPart

        } catch (exception: Exception) {
            Logger.instance.error(tag, exception)
            return null
        }
    }
}