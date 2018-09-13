package guepardoapps.lib.openweather.extensions

import guepardoapps.lib.openweather.models.*

import org.junit.Assert.*

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class ClassExtensionUnitTest : Spek({

    describe("Unit tests for ClassExtension") {

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

        it("getPropertyJsonKey for city should be correct") {
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

        it("isDefault for city should be true") {
            // Arrange
            val city = City()

            // Act
            val actual = city.isDefault()

            // Assert
            assertEquals(true, actual)
        }

        it("isDefault for city should be false") {
            // Arrange
            val city = City()
            city.id = 420

            // Act
            val actual = city.isDefault()

            // Assert
            assertEquals(false, actual)
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

        it("getPropertyJsonKey for geoLocation should be correct") {
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

        it("isDefault for geoLocation should be true") {
            // Arrange
            val geoLocation = GeoLocation()

            // Act
            val actual = geoLocation.isDefault()

            // Assert
            assertEquals(true, actual)
        }

        it("isDefault for geoLocation should be false") {
            // Arrange
            val geoLocation = GeoLocation()
            geoLocation.longitude = 45.0

            // Act
            val actual = geoLocation.isDefault()

            // Assert
            assertEquals(false, actual)
        }

        it("getJsonKey for uv index should be correct") {
            // Arrange
            val uvIndex = UvIndex()
            val expectedParent = ""
            val expectedKey = ""

            // Act
            val actualJsonKey = uvIndex.getJsonKey()

            // Assert
            assertEquals(expectedParent, actualJsonKey.parent)
            assertEquals(expectedKey, actualJsonKey.key)
        }

        it("getPropertyJsonKey for uv index should be correct") {
            // Arrange
            val uvIndex = UvIndex()
            val expectedParent = ""
            val expectedKey = "value"

            // Act
            val name = uvIndex::value.name
            val actualJsonKey = uvIndex.getPropertyJsonKey(name)

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

        it("getPropertyJsonKey for weather current should be correct") {
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
