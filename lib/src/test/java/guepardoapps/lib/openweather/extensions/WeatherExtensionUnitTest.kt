package guepardoapps.lib.openweather.extensions

import guepardoapps.lib.openweather.enums.ForecastDayTime
import guepardoapps.lib.openweather.enums.ForecastListType
import guepardoapps.lib.openweather.enums.WeatherCondition
import guepardoapps.lib.openweather.models.WeatherForecast
import guepardoapps.lib.openweather.models.WeatherForecastPart
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

import org.junit.Assert.*
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.util.*

@RunWith(JUnitPlatform::class)
class WeatherExtensionUnitTest : Spek({

    describe("Unit tests for WeatherExtensionUnitTest") {

        beforeEachTest { }

        afterEachTest { }

        it("getMostWeatherCondition should be correct") {
            // Arrange
            val weatherForecastPart1 = WeatherForecastPart()
            weatherForecastPart1.listType = ForecastListType.Forecast
            weatherForecastPart1.weatherCondition = WeatherCondition.Clear
            weatherForecastPart1.dateTime = Calendar.getInstance()

            val weatherForecastPart2 = WeatherForecastPart()
            weatherForecastPart2.listType = ForecastListType.Forecast
            weatherForecastPart2.weatherCondition = WeatherCondition.Clear
            weatherForecastPart2.dateTime = Calendar.getInstance()

            val weatherForecastPart3 = WeatherForecastPart()
            weatherForecastPart3.listType = ForecastListType.Forecast
            weatherForecastPart3.weatherCondition = WeatherCondition.Clouds
            weatherForecastPart3.dateTime = Calendar.getInstance()

            val weatherForecastPart4 = WeatherForecastPart()
            weatherForecastPart4.listType = ForecastListType.DateDivider
            weatherForecastPart4.weatherCondition = WeatherCondition.Null
            weatherForecastPart4.dateTime = Calendar.getInstance()

            val weatherForecast = WeatherForecast()
            weatherForecast.list = listOf(weatherForecastPart1, weatherForecastPart2, weatherForecastPart3, weatherForecastPart4)

            // Act
            val actual = weatherForecast.getMostWeatherCondition()

            // Assert
            assertEquals(WeatherCondition.Clear.id, actual.id)
            assertEquals(2, actual.count)
        }

        it("getMinTemperature should be correct") {
            // Arrange
            val weatherForecastPart1 = WeatherForecastPart()
            weatherForecastPart1.listType = ForecastListType.Forecast
            weatherForecastPart1.temperatureMin = 15.0

            val weatherForecastPart2 = WeatherForecastPart()
            weatherForecastPart2.listType = ForecastListType.Forecast
            weatherForecastPart2.temperatureMin = 15.5

            val weatherForecastPart3 = WeatherForecastPart()
            weatherForecastPart3.listType = ForecastListType.DateDivider
            weatherForecastPart3.temperatureMin = 0.0

            val weatherForecast = WeatherForecast()
            weatherForecast.list = listOf(weatherForecastPart1, weatherForecastPart2, weatherForecastPart3)

            // Act
            val actual = weatherForecast.getMinTemperature()

            // Assert
            assert(actual == 15.0)
        }

        it("getMaxTemperature should be correct") {
            // Arrange
            val weatherForecastPart1 = WeatherForecastPart()
            weatherForecastPart1.listType = ForecastListType.Forecast
            weatherForecastPart1.temperatureMax = 15.0

            val weatherForecastPart2 = WeatherForecastPart()
            weatherForecastPart2.listType = ForecastListType.Forecast
            weatherForecastPart2.temperatureMax = 15.5

            val weatherForecastPart3 = WeatherForecastPart()
            weatherForecastPart3.listType = ForecastListType.DateDivider
            weatherForecastPart3.temperatureMax = 0.0

            val weatherForecast = WeatherForecast()
            weatherForecast.list = listOf(weatherForecastPart1, weatherForecastPart2, weatherForecastPart3)

            // Act
            val actual = weatherForecast.getMaxTemperature()

            // Assert
            assert(actual == 15.5)
        }

        it("getMinPressure should be correct") {
            // Arrange
            val weatherForecastPart1 = WeatherForecastPart()
            weatherForecastPart1.listType = ForecastListType.Forecast
            weatherForecastPart1.pressure = 973.5

            val weatherForecastPart2 = WeatherForecastPart()
            weatherForecastPart2.listType = ForecastListType.Forecast
            weatherForecastPart2.pressure = 981.0

            val weatherForecastPart3 = WeatherForecastPart()
            weatherForecastPart3.listType = ForecastListType.DateDivider
            weatherForecastPart3.pressure = 0.0

            val weatherForecast = WeatherForecast()
            weatherForecast.list = listOf(weatherForecastPart1, weatherForecastPart2, weatherForecastPart3)

            // Act
            val actual = weatherForecast.getMinPressure()

            // Assert
            assert(actual == 973.5)
        }

        it("getMaxPressure should be correct") {
            // Arrange
            val weatherForecastPart1 = WeatherForecastPart()
            weatherForecastPart1.listType = ForecastListType.Forecast
            weatherForecastPart1.pressure = 973.5

            val weatherForecastPart2 = WeatherForecastPart()
            weatherForecastPart2.listType = ForecastListType.Forecast
            weatherForecastPart2.pressure = 981.0

            val weatherForecastPart3 = WeatherForecastPart()
            weatherForecastPart3.listType = ForecastListType.DateDivider
            weatherForecastPart3.pressure = 0.0

            val weatherForecast = WeatherForecast()
            weatherForecast.list = listOf(weatherForecastPart1, weatherForecastPart2, weatherForecastPart3)

            // Act
            val actual = weatherForecast.getMaxPressure()

            // Assert
            assert(actual == 973.5)
        }

        it("getMinHumidity should be correct") {
            // Arrange
            val weatherForecastPart1 = WeatherForecastPart()
            weatherForecastPart1.listType = ForecastListType.Forecast
            weatherForecastPart1.humidity = 43.5

            val weatherForecastPart2 = WeatherForecastPart()
            weatherForecastPart2.listType = ForecastListType.Forecast
            weatherForecastPart2.humidity = 35.5

            val weatherForecastPart3 = WeatherForecastPart()
            weatherForecastPart3.listType = ForecastListType.DateDivider
            weatherForecastPart3.humidity = 0.0

            val weatherForecast = WeatherForecast()
            weatherForecast.list = listOf(weatherForecastPart1, weatherForecastPart2, weatherForecastPart3)

            // Act
            val actual = weatherForecast.getMinHumidity()

            // Assert
            assert(actual == 35.5)
        }

        it("getMaxHumidity should be correct") {
            // Arrange
            val weatherForecastPart1 = WeatherForecastPart()
            weatherForecastPart1.listType = ForecastListType.Forecast
            weatherForecastPart1.humidity = 43.5

            val weatherForecastPart2 = WeatherForecastPart()
            weatherForecastPart2.listType = ForecastListType.Forecast
            weatherForecastPart2.humidity = 35.5

            val weatherForecastPart3 = WeatherForecastPart()
            weatherForecastPart3.listType = ForecastListType.DateDivider
            weatherForecastPart3.humidity = 0.0

            val weatherForecast = WeatherForecast()
            weatherForecast.list = listOf(weatherForecastPart1, weatherForecastPart2, weatherForecastPart3)

            // Act
            val actual = weatherForecast.getMaxHumidity()

            // Assert
            assert(actual == 43.5)
        }

        it("getForecastDayTime should be correct Night") {
            // Arrange
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 2)
            val weatherForecastPart = WeatherForecastPart()
            weatherForecastPart.dateTime = calendar

            // Act
            val actual = weatherForecastPart.getForecastDayTime()

            // Assert
            assertEquals(ForecastDayTime.Night, actual)
        }

        it("getForecastDayTime should be correct Afternoon") {
            // Arrange
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 16)
            val weatherForecastPart = WeatherForecastPart()
            weatherForecastPart.dateTime = calendar

            // Act
            val actual = weatherForecastPart.getForecastDayTime()

            // Assert
            assertEquals(ForecastDayTime.Afternoon, actual)
        }

        it("getForecastDayTime should be correct Evening") {
            // Arrange
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 19)
            val weatherForecastPart = WeatherForecastPart()
            weatherForecastPart.dateTime = calendar

            // Act
            val actual = weatherForecastPart.getForecastDayTime()

            // Assert
            assertEquals(ForecastDayTime.Evening, actual)
        }
    }
})
