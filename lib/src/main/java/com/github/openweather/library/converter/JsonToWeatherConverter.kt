package com.github.openweather.library.converter

import android.util.Log
import com.github.openweather.library.enums.ForecastListType
import com.github.openweather.library.enums.WeatherCondition
import com.github.openweather.library.extensions.empty
import com.github.openweather.library.extensions.getJsonKey
import com.github.openweather.library.extensions.getPropertyJsonKey
import com.github.openweather.library.extensions.toMillis
import com.github.openweather.library.models.*
import org.json.JSONObject
import java.util.*

internal class JsonToWeatherConverter : IJsonToWeatherConverter {

    private val tag: String = JsonToWeatherConverter::class.java.simpleName

    private val codeKey: String = "cod"

    private val successCode: Int = 200

    private val statusKey: String = "status"

    private val okCode: String = "OK"

    private val dateKey: String = "dt_txt"

    private val dateSplitter: String = " "

    private val messageKey: String = "message"

    private val errorMessage: String = "not found"

    override fun convertToCity(jsonString: String): City2? {
        val city2 = City2()
        try {
            val jsonObject = JSONObject(jsonString)
            if (jsonObject.getString(statusKey) != okCode) {
                Log.e(tag, "Error in parsing jsonObject in convertToCity: $jsonObject")
                return null
            }

            val resultsJsonObject = jsonObject.getJSONArray(city2.getJsonKey().key).getJSONObject(0)
            val addressComponentJsonArray = resultsJsonObject.getJSONArray(city2.getPropertyJsonKey(city2::addressComponents.name).key)

            val addressComponentCity = AddressComponent()
            val addressComponentCityJsonObject = addressComponentJsonArray.getJSONObject(0)
            addressComponentCity.shortName = addressComponentCityJsonObject.getString(addressComponentCity.getPropertyJsonKey(addressComponentCity::shortName.name).key)
            addressComponentCity.longName = addressComponentCityJsonObject.getString(addressComponentCity.getPropertyJsonKey(addressComponentCity::longName.name).key)
            val addressComponentCityTypesJsonArray = addressComponentCityJsonObject.getJSONArray(addressComponentCity.getPropertyJsonKey(addressComponentCity::types.name).key)
            for (index in 0 until addressComponentCityTypesJsonArray.length() step 1) {
                addressComponentCity.types = addressComponentCity.types.plus(addressComponentCityTypesJsonArray.getString(index))
            }
            city2.addressComponents = city2.addressComponents.plus(addressComponentCity)

            val addressComponentCountry = AddressComponent()
            val addressComponentCountryJsonObject = addressComponentJsonArray.getJSONObject(1)
            addressComponentCountry.shortName = addressComponentCountryJsonObject.getString(addressComponentCountry.getPropertyJsonKey(addressComponentCountry::shortName.name).key)
            addressComponentCountry.longName = addressComponentCountryJsonObject.getString(addressComponentCountry.getPropertyJsonKey(addressComponentCountry::longName.name).key)
            val addressComponentCountryTypesJsonArray = addressComponentCountryJsonObject.getJSONArray(addressComponentCountry.getPropertyJsonKey(addressComponentCountry::types.name).key)
            for (index in 0 until addressComponentCountryTypesJsonArray.length() step 1) {
                addressComponentCountry.types = addressComponentCountry.types.plus(addressComponentCountryTypesJsonArray.getString(index))
            }
            city2.addressComponents = city2.addressComponents.plus(addressComponentCountry)

            city2.geometry = Geometry()
            val geometryJsonObject = resultsJsonObject.getJSONObject(city2.geometry.getJsonKey().key)
            city2.geometry.locationType = geometryJsonObject.getString(city2.geometry.getPropertyJsonKey(city2.geometry::locationType.name).key)

            city2.geometry.viewport = Viewport()
            val viewportJsonObject = geometryJsonObject.getJSONObject(city2.geometry.viewport.getJsonKey().key)

            val northeast = Coordinates2()
            val northeastJsonObject = viewportJsonObject.getJSONObject(city2.geometry.viewport.getPropertyJsonKey(city2.geometry.viewport::northeast.name).key)
            northeast.lat = northeastJsonObject.getDouble(northeast.getPropertyJsonKey(northeast::lat.name).key)
            northeast.lng = northeastJsonObject.getDouble(northeast.getPropertyJsonKey(northeast::lng.name).key)
            city2.geometry.viewport.northeast = northeast

            val southwest = Coordinates2()
            val southwestJsonObject = viewportJsonObject.getJSONObject(city2.geometry.viewport.getPropertyJsonKey(city2.geometry.viewport::southwest.name).key)
            southwest.lat = southwestJsonObject.getDouble(southwest.getPropertyJsonKey(southwest::lat.name).key)
            southwest.lng = southwestJsonObject.getDouble(southwest.getPropertyJsonKey(southwest::lng.name).key)
            city2.geometry.viewport.southwest = southwest

            city2.geometry.location = Coordinates2()
            val locationJsonObject = geometryJsonObject.getJSONObject(city2.geometry.getPropertyJsonKey(city2.geometry::location.name).key)
            city2.geometry.location.lat = locationJsonObject.getDouble(city2.geometry.location.getPropertyJsonKey(city2.geometry.location::lat.name).key)
            city2.geometry.location.lng = locationJsonObject.getDouble(city2.geometry.location.getPropertyJsonKey(city2.geometry.location::lng.name).key)

            val typesJsonArray = resultsJsonObject.getJSONArray(city2.getPropertyJsonKey(city2::types.name).key)
            for (index in 0 until typesJsonArray.length() step 1) {
                city2.types = city2.types.plus(typesJsonArray.getString(index))
            }

            return city2

        } catch (exception: Exception) {
            Log.e(tag, exception.message)
            return city2
        }
    }

    override fun convertToWeatherCurrent(jsonString: String): WeatherCurrent? {
        val weatherCurrent = WeatherCurrent()
        try {
            val jsonObject = JSONObject(jsonString)
            if (jsonObject.getInt(codeKey) != successCode) {
                Log.e(tag, "Error in parsing jsonObject in convertToWeatherCurrent: $jsonObject")
                return null
            }

            val sysJsonObject = jsonObject.getJSONObject(weatherCurrent.getJsonKey().key)

            weatherCurrent.city = City()
            weatherCurrent.city.id = jsonObject.getInt(weatherCurrent.city.getPropertyJsonKey(weatherCurrent.city::id.name).key)
            weatherCurrent.city.name = jsonObject.getString(weatherCurrent.city.getPropertyJsonKey(weatherCurrent.city::name.name).key)
            weatherCurrent.city.country = sysJsonObject.getString(weatherCurrent.city.getPropertyJsonKey(weatherCurrent.city::country.name).key)

            weatherCurrent.city.coordinates = Coordinates()
            val coordinationJsonObject = jsonObject.getJSONObject(weatherCurrent.city.coordinates.getJsonKey().key)
            weatherCurrent.city.coordinates.lat = coordinationJsonObject.getDouble(weatherCurrent.city.coordinates.getPropertyJsonKey(weatherCurrent.city.coordinates::lat.name).key)
            weatherCurrent.city.coordinates.lon = coordinationJsonObject.getDouble(weatherCurrent.city.coordinates.getPropertyJsonKey(weatherCurrent.city.coordinates::lon.name).key)

            weatherCurrent.sunriseTime.timeInMillis = sysJsonObject.getLong(weatherCurrent.getPropertyJsonKey(weatherCurrent::sunriseTime.name).key).toMillis()
            weatherCurrent.sunsetTime.timeInMillis = sysJsonObject.getLong(weatherCurrent.getPropertyJsonKey(weatherCurrent::sunsetTime.name).key).toMillis()

            val details = jsonObject.getJSONArray(weatherCurrent.getPropertyJsonKey(weatherCurrent::description.name).parent).getJSONObject(0)
            weatherCurrent.icon = details.getString(weatherCurrent.getPropertyJsonKey(weatherCurrent::icon.name).key)
            weatherCurrent.description = details.getString(weatherCurrent.getPropertyJsonKey(weatherCurrent::description.name).key)
            val main = details.getString(weatherCurrent.getPropertyJsonKey(weatherCurrent::weatherCondition.name).key)
            weatherCurrent.weatherCondition = WeatherCondition.valueOf(main)

            val mainJSONObject = jsonObject.getJSONObject(weatherCurrent.getPropertyJsonKey(weatherCurrent::temperature.name).parent)
            weatherCurrent.temperature = mainJSONObject.getDouble(weatherCurrent.getPropertyJsonKey(weatherCurrent::temperature.name).key)
            weatherCurrent.temperatureMin = mainJSONObject.getDouble(weatherCurrent.getPropertyJsonKey(weatherCurrent::temperatureMin.name).key)
            weatherCurrent.temperatureMax = mainJSONObject.getDouble(weatherCurrent.getPropertyJsonKey(weatherCurrent::temperatureMax.name).key)
            weatherCurrent.humidity = mainJSONObject.getDouble(weatherCurrent.getPropertyJsonKey(weatherCurrent::humidity.name).key)
            weatherCurrent.pressure = mainJSONObject.getDouble(weatherCurrent.getPropertyJsonKey(weatherCurrent::pressure.name).key)

            weatherCurrent.visibility = jsonObject.getInt(weatherCurrent.getPropertyJsonKey(weatherCurrent::visibility.name).key)

            weatherCurrent.cloudsAll = jsonObject
                    .getJSONObject(weatherCurrent.getPropertyJsonKey(weatherCurrent::cloudsAll.name).parent)
                    .getInt(weatherCurrent.getPropertyJsonKey(weatherCurrent::cloudsAll.name).key)

            weatherCurrent.windSpeed = jsonObject
                    .getJSONObject(weatherCurrent.getPropertyJsonKey(weatherCurrent::windSpeed.name).parent)
                    .getDouble(weatherCurrent.getPropertyJsonKey(weatherCurrent::windSpeed.name).key)
            /*weatherCurrent.windDegree = jsonObject
                    .getJSONObject(weatherCurrent.getPropertyJsonKey(weatherCurrent::windDegree.name).parent)
                    .getDouble(weatherCurrent.getPropertyJsonKey(weatherCurrent::windDegree.name).key)*/

            weatherCurrent.dateTime.timeInMillis = jsonObject.getLong(weatherCurrent.getPropertyJsonKey(weatherCurrent::dateTime.name).key).toMillis()

            weatherCurrent.lastUpdate = Calendar.getInstance()

            return weatherCurrent
        } catch (exception: Exception) {
            Log.e(tag, exception.message)
            return weatherCurrent
        }
    }

    override fun convertToWeatherForecast(jsonString: String): WeatherForecast? {
        val weatherForecast = WeatherForecast()
        try {
            val jsonObject = JSONObject(jsonString)
            if (jsonObject.getInt(codeKey) != successCode) {
                Log.e(tag, "Error in parsing jsonObject in convertToWeatherForecast: $jsonObject")
                return null
            }

            weatherForecast.city = City()
            val cityJsonObject = jsonObject.getJSONObject(weatherForecast.city.getJsonKey().key)
            weatherForecast.city.id = cityJsonObject.getInt(weatherForecast.city.getPropertyJsonKey(weatherForecast.city::id.name).key)
            weatherForecast.city.name = cityJsonObject.getString(weatherForecast.city.getPropertyJsonKey(weatherForecast.city::name.name).key)
            weatherForecast.city.country = cityJsonObject.getString(weatherForecast.city.getPropertyJsonKey(weatherForecast.city::country.name).key)
            weatherForecast.city.population = cityJsonObject.getInt(weatherForecast.city.getPropertyJsonKey(weatherForecast.city::population.name).key)

            weatherForecast.city.coordinates = Coordinates()
            val coordinationJsonObject = cityJsonObject.getJSONObject(weatherForecast.city.coordinates.getJsonKey().key)
            weatherForecast.city.coordinates.lat = coordinationJsonObject.getDouble(weatherForecast.city.coordinates.getPropertyJsonKey(weatherForecast.city.coordinates::lat.name).key)
            weatherForecast.city.coordinates.lon = coordinationJsonObject.getDouble(weatherForecast.city.coordinates.getPropertyJsonKey(weatherForecast.city.coordinates::lon.name).key)

            val weatherForecastPartC = WeatherForecastPart()
            val dataListJsonArray = jsonObject.getJSONArray(weatherForecastPartC.getJsonKey().parent)
            var list = listOf<WeatherForecastPart>()

            var previousDateString = String.empty

            for (index in 0 until dataListJsonArray.length() step 1) {
                val dataJsonObject = dataListJsonArray.getJSONObject(index)

                val currentDateString = dataJsonObject.getString(dateKey).split(dateSplitter).first()

                if (index == 0 || !currentDateString.contains(previousDateString)) {
                    val weatherForecastPartDivider = WeatherForecastPart()
                    weatherForecastPartDivider.listType = ForecastListType.DateDivider

                    weatherForecastPartDivider.dateTime.timeInMillis = dataJsonObject.getLong(weatherForecastPartDivider.getPropertyJsonKey(weatherForecastPartDivider::dateTime.name).key).toMillis()

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
            Log.e(tag, exception.message)
            return weatherForecast
        }
    }

    private fun convertToWeatherForecastPart(jsonObject: JSONObject): WeatherForecastPart? {
        val weatherForecastPart = WeatherForecastPart()
        try {
            val jsonObjectWeather = jsonObject
                    .getJSONArray(weatherForecastPart.getJsonKey().key).getJSONObject(0)

            weatherForecastPart.main = jsonObjectWeather.getString(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::main.name).key)
            weatherForecastPart.weatherCondition = WeatherCondition.valueOf(weatherForecastPart.main)
            weatherForecastPart.description = jsonObjectWeather.getString(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::description.name).key)
            weatherForecastPart.weatherDefaultIcon = jsonObjectWeather.getString(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::weatherDefaultIcon.name).key)

            val jsonObjectMain = jsonObject.getJSONObject(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::temperature.name).parent)

            weatherForecastPart.temperature = jsonObjectMain.getDouble(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::temperature.name).key)
            weatherForecastPart.temperatureMin = jsonObjectMain.getDouble(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::temperatureMin.name).key)
            weatherForecastPart.temperatureMax = jsonObjectMain.getDouble(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::temperatureMax.name).key)
            weatherForecastPart.temperatureKf = jsonObjectMain.getDouble(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::temperatureKf.name).key)
            weatherForecastPart.pressure = jsonObjectMain.getDouble(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::pressure.name).key)
            weatherForecastPart.pressureSeaLevel = jsonObjectMain.getDouble(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::pressureSeaLevel.name).key)
            weatherForecastPart.pressureGroundLevel = jsonObjectMain.getDouble(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::pressureGroundLevel.name).key)
            weatherForecastPart.humidity = jsonObjectMain.getDouble(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::humidity.name).key)

            weatherForecastPart.cloudsAll = jsonObject
                    .getJSONObject(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::cloudsAll.name).parent)
                    .getInt(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::cloudsAll.name).key)

            weatherForecastPart.windSpeed = jsonObject
                    .getJSONObject(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::windSpeed.name).parent)
                    .getDouble(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::windSpeed.name).key)
            weatherForecastPart.windDegree = jsonObject
                    .getJSONObject(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::windDegree.name).parent)
                    .getDouble(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::windDegree.name).key)

            weatherForecastPart.dateTime.timeInMillis = jsonObject.getLong(weatherForecastPart.getPropertyJsonKey(weatherForecastPart::dateTime.name).key).toMillis()

            weatherForecastPart.listType = ForecastListType.Forecast

            return weatherForecastPart

        } catch (exception: Exception) {
            Log.e(tag, exception.message)
            return weatherForecastPart
        }
    }

    override fun convertToUvIndex(jsonString: String): UvIndex? {
        val uvIndex = UvIndex()
        return try {
            val jsonObject = JSONObject(jsonString)

            uvIndex.coordinates = Coordinates()
            uvIndex.coordinates.lat = jsonObject.getDouble(uvIndex.coordinates.getPropertyJsonKey(uvIndex.coordinates::lat.name).key)
            uvIndex.coordinates.lon = jsonObject.getDouble(uvIndex.coordinates.getPropertyJsonKey(uvIndex.coordinates::lon.name).key)

            uvIndex.dateTime.timeInMillis = jsonObject.getLong(uvIndex.getPropertyJsonKey(uvIndex::dateTime.name).key).toMillis()
            uvIndex.value = jsonObject.getDouble(uvIndex.getPropertyJsonKey(uvIndex::value.name).key)

            uvIndex
        } catch (exception: Exception) {
            Log.e(tag, exception.message)
            uvIndex
        }
    }

    override fun convertToCarbonMonoxide(jsonString: String): CarbonMonoxide? {
        try {
            val jsonObject = JSONObject(jsonString)
            if (jsonObject.getString(messageKey) == errorMessage) {
                Log.e(tag, "Error in parsing jsonObject in convertToCarbonMonoxide: $jsonObject")
                return null
            }

            val carbonMonoxide = CarbonMonoxide()

            carbonMonoxide.coordinates = Coordinates3()
            carbonMonoxide.coordinates.latitude = jsonObject.getDouble(carbonMonoxide.coordinates.getPropertyJsonKey(carbonMonoxide.coordinates::latitude.name).key)
            carbonMonoxide.coordinates.longitude = jsonObject.getDouble(carbonMonoxide.coordinates.getPropertyJsonKey(carbonMonoxide.coordinates::longitude.name).key)

            carbonMonoxide.dateTime.timeInMillis = jsonObject.getLong(carbonMonoxide.getPropertyJsonKey(carbonMonoxide::dateTime.name).key).toMillis()

            val carbonMonoxideDataC = CarbonMonoxideData()
            val dataListJsonArray = jsonObject.getJSONArray(carbonMonoxideDataC.getJsonKey().parent)
            var list = listOf<CarbonMonoxideData>()

            for (index in 0 until dataListJsonArray.length() step 1) {
                val dataJsonObject = dataListJsonArray.getJSONObject(index)

                val carbonMonoxideData = CarbonMonoxideData()
                carbonMonoxideData.precision = dataJsonObject.getDouble(carbonMonoxideData.getPropertyJsonKey(carbonMonoxideData::precision.name).key)
                carbonMonoxideData.pressure = dataJsonObject.getDouble(carbonMonoxideData.getPropertyJsonKey(carbonMonoxideData::pressure.name).key)
                carbonMonoxideData.value = dataJsonObject.getDouble(carbonMonoxideData.getPropertyJsonKey(carbonMonoxideData::value.name).key)

                list = list.plus(carbonMonoxideData)
            }

            carbonMonoxide.data = list

            return carbonMonoxide
        } catch (exception: Exception) {
            Log.e(tag, exception.message)
            return null
        }
    }

    override fun convertToNitrogenDioxide(jsonString: String): NitrogenDioxide? {
        try {
            val jsonObject = JSONObject(jsonString)
            if (jsonObject.getString(messageKey) == errorMessage) {
                Log.e(tag, "Error in parsing jsonObject in convertToNitrogenDioxide: $jsonObject")
                return null
            }

            val nitrogenDioxide = NitrogenDioxide()

            nitrogenDioxide.coordinates = Coordinates3()
            nitrogenDioxide.coordinates.latitude = jsonObject.getDouble(nitrogenDioxide.coordinates.getPropertyJsonKey(nitrogenDioxide.coordinates::latitude.name).key)
            nitrogenDioxide.coordinates.longitude = jsonObject.getDouble(nitrogenDioxide.coordinates.getPropertyJsonKey(nitrogenDioxide.coordinates::longitude.name).key)

            nitrogenDioxide.dateTime.timeInMillis = jsonObject.getLong(nitrogenDioxide.getPropertyJsonKey(nitrogenDioxide::dateTime.name).key).toMillis()

            nitrogenDioxide.data = NitrogenDioxideData()
            val jsonObjectData = jsonObject.getJSONObject(nitrogenDioxide.data.getPropertyJsonKey(nitrogenDioxide.data::no2.name).parent)

            nitrogenDioxide.data.no2 = NitrogenDioxideDataHolder()
            val jsonObjectDataNo2 = jsonObjectData.getJSONObject("no2")
            nitrogenDioxide.data.no2.precision = jsonObjectDataNo2.getDouble(nitrogenDioxide.data.no2.getPropertyJsonKey(nitrogenDioxide.data.no2::precision.name).key)
            nitrogenDioxide.data.no2.value = jsonObjectDataNo2.getDouble(nitrogenDioxide.data.no2.getPropertyJsonKey(nitrogenDioxide.data.no2::value.name).key)

            nitrogenDioxide.data.no2Strat = NitrogenDioxideDataHolder()
            val jsonObjectDataNo2Strat = jsonObjectData.getJSONObject("no2_strat")
            nitrogenDioxide.data.no2Strat.precision = jsonObjectDataNo2Strat.getDouble(nitrogenDioxide.data.no2Strat.getPropertyJsonKey(nitrogenDioxide.data.no2Strat::precision.name).key)
            nitrogenDioxide.data.no2Strat.value = jsonObjectDataNo2Strat.getDouble(nitrogenDioxide.data.no2Strat.getPropertyJsonKey(nitrogenDioxide.data.no2Strat::value.name).key)

            nitrogenDioxide.data.no2Trop = NitrogenDioxideDataHolder()
            val jsonObjectDataNo2Trop = jsonObjectData.getJSONObject("no2_trop")
            nitrogenDioxide.data.no2Trop.precision = jsonObjectDataNo2Trop.getDouble(nitrogenDioxide.data.no2Trop.getPropertyJsonKey(nitrogenDioxide.data.no2Trop::precision.name).key)
            nitrogenDioxide.data.no2Trop.value = jsonObjectDataNo2Trop.getDouble(nitrogenDioxide.data.no2Trop.getPropertyJsonKey(nitrogenDioxide.data.no2Trop::value.name).key)

            return nitrogenDioxide
        } catch (exception: Exception) {
            Log.e(tag, exception.message)
            return null
        }
    }

    override fun convertToOzone(jsonString: String): Ozone? {
        try {
            val jsonObject = JSONObject(jsonString)
            if (jsonObject.getString(messageKey) == errorMessage) {
                Log.e(tag, "Error in parsing jsonObject in convertToOzone: $jsonObject")
                return null
            }

            val ozone = Ozone()

            ozone.coordinates = Coordinates3()
            ozone.coordinates.latitude = jsonObject.getDouble(ozone.coordinates.getPropertyJsonKey(ozone.coordinates::latitude.name).key)
            ozone.coordinates.longitude = jsonObject.getDouble(ozone.coordinates.getPropertyJsonKey(ozone.coordinates::longitude.name).key)

            ozone.dateTime.timeInMillis = jsonObject.getLong(ozone.getPropertyJsonKey(ozone::dateTime.name).key).toMillis()

            ozone.data = jsonObject.getDouble(ozone.getPropertyJsonKey(ozone::data.name).key)

            return ozone
        } catch (exception: Exception) {
            Log.e(tag, exception.message)
            return null
        }
    }

    override fun convertToSulfurDioxide(jsonString: String): SulfurDioxide? {
        try {
            val jsonObject = JSONObject(jsonString)
            if (jsonObject.getString(messageKey) == errorMessage) {
                Log.e(tag, "Error in parsing jsonObject in convertToSulfurDioxide: $jsonObject")
                return null
            }

            val sulfurDioxide = SulfurDioxide()

            sulfurDioxide.coordinates = Coordinates3()
            sulfurDioxide.coordinates.latitude = jsonObject.getDouble(sulfurDioxide.coordinates.getPropertyJsonKey(sulfurDioxide.coordinates::latitude.name).key)
            sulfurDioxide.coordinates.longitude = jsonObject.getDouble(sulfurDioxide.coordinates.getPropertyJsonKey(sulfurDioxide.coordinates::longitude.name).key)

            sulfurDioxide.dateTime.timeInMillis = jsonObject.getLong(sulfurDioxide.getPropertyJsonKey(sulfurDioxide::dateTime.name).key).toMillis()

            val sulfurDioxideDataC = SulfurDioxideData()
            val dataListJsonArray = jsonObject.getJSONArray(sulfurDioxideDataC.getJsonKey().parent)
            var list = listOf<SulfurDioxideData>()

            for (index in 0 until dataListJsonArray.length() step 1) {
                val dataJsonObject = dataListJsonArray.getJSONObject(index)

                val sulfurDioxideData = SulfurDioxideData()
                sulfurDioxideData.precision = dataJsonObject.getDouble(sulfurDioxideData.getPropertyJsonKey(sulfurDioxideData::precision.name).key)
                sulfurDioxideData.pressure = dataJsonObject.getDouble(sulfurDioxideData.getPropertyJsonKey(sulfurDioxideData::pressure.name).key)
                sulfurDioxideData.value = dataJsonObject.getDouble(sulfurDioxideData.getPropertyJsonKey(sulfurDioxideData::value.name).key)

                list = list.plus(sulfurDioxideData)
            }

            sulfurDioxide.data = list

            return sulfurDioxide
        } catch (exception: Exception) {
            Log.e(tag, exception.message)
            return null
        }
    }
}