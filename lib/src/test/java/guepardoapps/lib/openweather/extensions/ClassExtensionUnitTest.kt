package guepardoapps.lib.openweather.extensions

import guepardoapps.lib.openweather.models.City
import guepardoapps.lib.openweather.models.GeoLocation
import guepardoapps.lib.openweather.models.WeatherCurrent
import guepardoapps.lib.openweather.models.WeatherForecastPart

import org.junit.Assert.*

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class ClassExtensionUnitTest : Spek({

    describe("Unit tests for ClassExtensionUnitTest") {

        beforeEachTest { }

        afterEachTest { }

        it("getJsonKey for city should be correct") {
            // Arrange
            val city = City()
            val expectedParent = ""
            val expectedKey = "city"

            // Act
            val actualJsonKey = city.getJsonKey()

            // Assert
            assertEquals(expectedParent, actualJsonKey.parent)
            assertEquals(expectedKey, actualJsonKey.key)
        }

        it("getPropertyJsonKey for city  should be correct") {
            // Arrange
            val city = City()
            val expectedParent = "city"
            val expectedKey = "name"

            // Act
            val name = city::name.name
            val actualJsonKey = city.getPropertyJsonKey(name)

            // Assert
            assertEquals(expectedParent, actualJsonKey.parent)
            assertEquals(expectedKey, actualJsonKey.key)
        }

        it("getJsonKey for geoLocation should be correct") {
            // Arrange
            val geoLocation = GeoLocation()
            val expectedParent = "city"
            val expectedKey = "coord"

            // Act
            val actualJsonKey = geoLocation.getJsonKey()

            // Assert
            assertEquals(expectedParent, actualJsonKey.parent)
            assertEquals(expectedKey, actualJsonKey.key)
        }

        it("getPropertyJsonKey for geoLocation  should be correct") {
            // Arrange
            val geoLocation = GeoLocation()
            val expectedParent = "coord"
            val expectedKey = "lat"

            // Act
            val name = geoLocation::latitude.name
            val actualJsonKey = geoLocation.getPropertyJsonKey(name)

            // Assert
            assertEquals(expectedParent, actualJsonKey.parent)
            assertEquals(expectedKey, actualJsonKey.key)
        }

        it("getJsonKey for weather current should be correct") {
            // Arrange
            val weatherCurrent = WeatherCurrent()
            val expectedParent = ""
            val expectedKey = "sys"

            // Act
            val actualJsonKey = weatherCurrent.getJsonKey()

            // Assert
            assertEquals(expectedParent, actualJsonKey.parent)
            assertEquals(expectedKey, actualJsonKey.key)
        }

        it("getPropertyJsonKey for weather current  should be correct") {
            // Arrange
            val weatherCurrent = WeatherCurrent()
            val expectedParent = "main"
            val expectedKey = "humidity"

            // Act
            val name = weatherCurrent::humidity.name
            val actualJsonKey = weatherCurrent.getPropertyJsonKey(name)

            // Assert
            assertEquals(expectedParent, actualJsonKey.parent)
            assertEquals(expectedKey, actualJsonKey.key)
        }

        it("getJsonKey for weather forecast part should be correct") {
            // Arrange
            val weatherForecastPart = WeatherForecastPart()
            val expectedParent = "list"
            val expectedKey = "weather"

            // Act
            val actualJsonKey = weatherForecastPart.getJsonKey()

            // Assert
            assertEquals(expectedParent, actualJsonKey.parent)
            assertEquals(expectedKey, actualJsonKey.key)
        }

        it("getPropertyJsonKey for weather forecast part  should be correct") {
            // Arrange
            val weatherForecastPart = WeatherForecastPart()
            val expectedParent = "clouds"
            val expectedKey = "all"

            // Act
            val name = weatherForecastPart::cloudsAll.name
            val actualJsonKey = weatherForecastPart.getPropertyJsonKey(name)

            // Assert
            assertEquals(expectedParent, actualJsonKey.parent)
            assertEquals(expectedKey, actualJsonKey.key)
        }
    }
})
