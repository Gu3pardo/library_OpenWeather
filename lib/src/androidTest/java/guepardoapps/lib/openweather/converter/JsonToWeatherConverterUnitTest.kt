package guepardoapps.lib.openweather.converter

import android.support.test.runner.AndroidJUnit4
import guepardoapps.lib.openweather.enums.WeatherCondition
import guepardoapps.lib.openweather.models.*
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class JsonToWeatherConverterUnitTest {
    @Test
    fun convertToWeatherCurrent_isCorrect() {
        // Arrange
        val jsonToWeatherConverter = JsonToWeatherConverter()
        val jsonStringToTest = "{\"coord\":{\"lon\":11.08,\"lat\":49.45}," +
                "\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\"," +
                "\"icon\":\"03d\"}],\"base\":\"stations\",\"main\":{\"temp\":21.37," +
                "\"pressure\":1021,\"humidity\":56,\"temp_min\":20,\"temp_max\":23}," +
                "\"visibility\":10000,\"wind\":{\"speed\":3.1,\"deg\":90}," +
                "\"clouds\":{\"all\":40},\"dt\":1527326400," +
                "\"sys\":{\"type\":1,\"id\":4888,\"message\":0.0147," +
                "\"country\":\"DE\"," +
                "\"sunrise\":1527304731,\"sunset\":1527361642}," +
                "\"id\":2861650,\"name\":\"Nuremberg\",\"cod\":200}"

        val sunriseTime = Calendar.getInstance()
        sunriseTime.timeInMillis = 1527304731000
        val sunsetTime = Calendar.getInstance()
        sunsetTime.timeInMillis = 1527361642000

        val geoLocation = GeoLocation()
        geoLocation.longitude = 11.08
        geoLocation.latitude = 49.45

        val city = City()
        city.id = 2861650
        city.name = "Nuremberg"
        city.country = "DE"
        city.geoLocation = geoLocation

        val expectedWeatherCurrent = WeatherCurrent()
        expectedWeatherCurrent.description = "scattered clouds"
        expectedWeatherCurrent.weatherCondition = WeatherCondition.Clouds
        expectedWeatherCurrent.temperature = 21.37
        expectedWeatherCurrent.pressure = 1021.0
        expectedWeatherCurrent.humidity = 56.0
        expectedWeatherCurrent.sunsetTime = sunsetTime
        expectedWeatherCurrent.sunriseTime = sunriseTime
        expectedWeatherCurrent.city = city
        expectedWeatherCurrent.lastUpdate = Calendar.getInstance()

        // Act
        val actualWeatherCurrent = jsonToWeatherConverter.convertToWeatherCurrent(jsonStringToTest)

        // Assert
        assertEquals(expectedWeatherCurrent.description, actualWeatherCurrent!!.description)
        assertEquals(expectedWeatherCurrent.weatherCondition, actualWeatherCurrent.weatherCondition)
        assertEquals(expectedWeatherCurrent.temperature, actualWeatherCurrent.temperature, 0.0)
        assertEquals(expectedWeatherCurrent.pressure, actualWeatherCurrent.pressure, 0.0)
        assertEquals(expectedWeatherCurrent.humidity, actualWeatherCurrent.humidity, 0.0)
        assertEquals(expectedWeatherCurrent.sunsetTime, actualWeatherCurrent.sunsetTime)
        assertEquals(expectedWeatherCurrent.sunriseTime, actualWeatherCurrent.sunriseTime)
    }

    @Test
    fun convertToWeatherForecast_isCorrect() {
        // Arrange
        val jsonToWeatherConverter = JsonToWeatherConverter()
        val jsonStringToTest = "{\"cod\":\"200\",\"message\":0.0026,\"cnt\":40," +
                "\"list\":[" +
                "{\"dt\":1530219600,\"main\":{\"temp\":14.79,\"temp_min\":14.79,\"temp_max\":17.08,\"pressure\":980.7,\"sea_level\":1031.63,\"grnd_level\":980.7,\"humidity\":71,\"temp_kf\":-2.29},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10n\"}],\"clouds\":{\"all\":100},\"wind\":{\"speed\":3.36,\"deg\":33.0006},\"rain\":{\"3h\":0.0775},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2018-06-28 21:00:00\"}," +
                "{\"dt\":1530230400,\"main\":{\"temp\":15.21,\"temp_min\":15.21,\"temp_max\":16.93,\"pressure\":980.15,\"sea_level\":1031.31,\"grnd_level\":980.15,\"humidity\":71,\"temp_kf\":-1.72},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10n\"}],\"clouds\":{\"all\":92},\"wind\":{\"speed\":3.13,\"deg\":34.0042},\"rain\":{\"3h\":0.0575},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2018-06-29 00:00:00\"}," +
                "{\"dt\":1530241200,\"main\":{\"temp\":14.94,\"temp_min\":14.94,\"temp_max\":16.08,\"pressure\":979.77,\"sea_level\":1030.91,\"grnd_level\":979.77,\"humidity\":82,\"temp_kf\":-1.15},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10n\"}],\"clouds\":{\"all\":88},\"wind\":{\"speed\":2.52,\"deg\":22.001},\"rain\":{\"3h\":0.21},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2018-06-29 03:00:00\"}]," +
                "\"city\":{\"id\":2861650,\"name\":\"Nuremberg\",\"coord\":{\"lat\":49.4539,\"lon\":11.0773},\"country\":\"DE\",\"population\":499237}}"

        val geoLocation = GeoLocation()
        geoLocation.longitude = 11.0773
        geoLocation.latitude = 49.4539

        val city = City()
        city.id = 2861650
        city.name = "Nuremberg"
        city.country = "DE"
        city.population = 499237
        city.geoLocation = geoLocation

        val dateTime = Calendar.getInstance()
        dateTime.timeInMillis = 1530219600

        val weatherForecastPart = WeatherForecastPart()
        weatherForecastPart.main = "Rain"
        weatherForecastPart.weatherCondition = WeatherCondition.Rain
        weatherForecastPart.description = "light rain"
        weatherForecastPart.weatherDefaultIcon = "10n"
        weatherForecastPart.temperature = 14.79
        weatherForecastPart.temperatureMin = 14.79
        weatherForecastPart.temperatureMax = 17.08
        weatherForecastPart.temperatureKf = -2.29
        weatherForecastPart.pressure = 980.7
        weatherForecastPart.pressureSeaLevel = 1031.63
        weatherForecastPart.pressureGroundLevel = 980.7
        weatherForecastPart.humidity = 71.0
        weatherForecastPart.cloudsAll = 100
        weatherForecastPart.windSpeed = 3.36
        weatherForecastPart.windDegree = 33.0006
        weatherForecastPart.dateTime = dateTime

        val expectedWeatherForecast = WeatherForecast()
        expectedWeatherForecast.city = city
        expectedWeatherForecast.list = arrayOf(weatherForecastPart)

        // Act
        val actualWeatherForecast = jsonToWeatherConverter.convertToWeatherForecast(jsonStringToTest)

        // Assert
        assertEquals(expectedWeatherForecast.list[0].main, actualWeatherForecast!!.list[1].main)
        assertEquals(expectedWeatherForecast.list[0].weatherCondition, actualWeatherForecast.list[1].weatherCondition)
        assertEquals(expectedWeatherForecast.list[0].description, actualWeatherForecast.list[1].description)
        assertEquals(expectedWeatherForecast.list[0].weatherDefaultIcon, actualWeatherForecast.list[1].weatherDefaultIcon)
        assertEquals(expectedWeatherForecast.list[0].temperature, actualWeatherForecast.list[1].temperature, 0.0)
        assertEquals(expectedWeatherForecast.list[0].temperatureMin, actualWeatherForecast.list[1].temperatureMin, 0.0)
        assertEquals(expectedWeatherForecast.list[0].temperatureMax, actualWeatherForecast.list[1].temperatureMax, 0.0)
        assertEquals(expectedWeatherForecast.list[0].temperatureKf, actualWeatherForecast.list[1].temperatureKf, 0.0)
        assertEquals(expectedWeatherForecast.list[0].pressure, actualWeatherForecast.list[1].pressure, 0.0)
        assertEquals(expectedWeatherForecast.list[0].pressureSeaLevel, actualWeatherForecast.list[1].pressureSeaLevel, 0.0)
        assertEquals(expectedWeatherForecast.list[0].pressureGroundLevel, actualWeatherForecast.list[1].pressureGroundLevel, 0.0)
        assertEquals(expectedWeatherForecast.list[0].humidity, actualWeatherForecast.list[1].humidity, 0.0)
        assertEquals(expectedWeatherForecast.list[0].cloudsAll, actualWeatherForecast.list[1].cloudsAll)
        assertEquals(expectedWeatherForecast.list[0].windSpeed, actualWeatherForecast.list[1].windSpeed, 0.0)
        assertEquals(expectedWeatherForecast.list[0].windDegree, actualWeatherForecast.list[1].windDegree, 0.0)
    }
}
