package guepardoapps.lib.openweather.extensions

import guepardoapps.lib.openweather.models.WeatherForecastPart
import org.junit.Test

import org.junit.Assert.*

class ClassExtensionUnitTest {
    @Test
    fun getJsonKey_isCorrect() {
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

    @Test
    fun getPropertyJsonKey_isCorrect() {
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
