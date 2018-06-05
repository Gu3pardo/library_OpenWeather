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

        val geoLocation = GeoLocation(49.45, 11.08)
        val city = City(2861650, "Nuremberg", "DE", geoLocation)

        val expectedWeatherCurrent = WeatherCurrent(
                city, "scattered clouds",
                21.37, 56.0, 1021.0,
                sunriseTime, sunsetTime, Calendar.getInstance(),
                WeatherCondition.Clouds)


        // Act
        val actualWeatherCurrent = jsonToWeatherConverter.convertToWeatherCurrent(jsonStringToTest)

        // Assert
        assertEquals(expectedWeatherCurrent, actualWeatherCurrent)
    }

    @Test
    fun convertToWeatherForecast_isCorrect() {
        // Arrange
        val jsonToWeatherConverter = JsonToWeatherConverter()
        val jsonStringToTest = "{\"cod\":\"200\",\"message\":0.0071,\"cnt\":40," +
                "\"list\":" +
                "[" +
                "{\"dt\":1527336000,\"main\":{" +
                "\"temp\":23.43,\"temp_min\":23.17,\"temp_max\":23.43," +
                "\"pressure\":983.37,\"sea_level\":1034.16,\"grnd_level\":983.37," +
                "\"humidity\":81,\"temp_kf\":0.26}," +
                "\"weather\":" +
                "[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"02d\"}]," +
                "\"clouds\":{\"all\":8}," +
                "\"wind\":{\"speed\":3.01,\"deg\":107.001}," +
                "\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2018-05-26 12:00:00\"}" +
                "]," +
                "\"city\":{\"id\":2861650,\"name\":\"Nuremberg\"," +
                "\"coord\":{\"lat\":49.4539,\"lon\":11.0773}," +
                "\"country\":\"DE\",\"population\":499237}}"

        val geoLocation = GeoLocation(49.4539, 11.0773)
        val city = City(2861650, "Nuremberg", "DE", geoLocation, 499237)

        val dateTime = Calendar.getInstance()
        dateTime.timeInMillis = 1527336000

        val forecastpart = WeatherForecastPart(
                "Clear", "clear sky",
                23.43, 23.17, 23.43, 0.26,
                81.0,
                983.37, 1034.16, 983.37,
                8,
                3.01, 107.001,
                dateTime,
                "02d", WeatherCondition.Clear)
        val list = arrayOf<IWeatherForecastPart>(forecastpart)

        val expectedWeatherForecastPart = WeatherForecast(city, list)

        // Act
        val actualWeatherForecast = jsonToWeatherConverter.convertToWeatherForecast(jsonStringToTest)

        // Assert
        assertEquals(expectedWeatherForecastPart, actualWeatherForecast)
    }
}
