package guepardoapps.lib.openweather.utils

import org.junit.Test

import org.junit.Assert.*

class StringHelperUnitTest {
    @Test
    fun getStringCount_isCorrect() {
        // Arrange
        val stringToTest = "Hello world says 'Hello world' and in german 'Hallo Welt'"
        val stringToFind = "Hello world"
        val expectedCount = 2

        // Act
        val actualCount = StringHelper.getStringCount(stringToTest, stringToFind)

        // Assert
        assertEquals(expectedCount, actualCount)
    }

    @Test
    fun getStringPositions_isCorrect() {
        // Arrange
        val stringToTest = "Aber Hallo sagte Achim"
        val charToFind: Char = "A".toCharArray()[0]
        val expectedPositions: IntArray = intArrayOf(0, 17)

        // Act
        val actualPositions = StringHelper.getCharPositions(stringToTest, charToFind)

        // Assert
        assertArrayEquals(expectedPositions, actualPositions)
    }

    @Test
    fun stringsAreEqual_shouldReturnTrue_IfEqual() {
        // Arrange
        val stringArrayToTest = Array<String>

        // Act
        val actualPositions = StringHelper.getCharPositions(stringToTest, charToFind)

        // Assert
        assertArrayEquals(expectedPositions, actualPositions)
    }
}
