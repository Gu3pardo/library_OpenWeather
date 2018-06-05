package guepardoapps.lib.openweather.extensions

import org.junit.Test

import org.junit.Assert.*

class NumberExtensionUnitTest {
    @Test
    fun integerFormat_isCorrect() {
        // Arrange
        val integerToTest = 5
        val charCount = 2
        val expectedString = "05"

        // Act
        val actualString = integerToTest.integerFormat(charCount)

        // Assert
        assertEquals(expectedString, actualString)
    }

    @Test
    fun doubleFormat_isCorrect() {
        // Arrange
        val doubleToTest = 5.4
        val decimalCount = 2
        val expectedString = "5.40"

        // Act
        val actualString = doubleToTest.doubleFormat(decimalCount)

        // Assert
        assertEquals(expectedString, actualString)
    }
}
