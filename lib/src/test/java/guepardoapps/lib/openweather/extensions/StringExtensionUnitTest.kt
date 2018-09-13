package guepardoapps.lib.openweather.extensions

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

import org.junit.Assert.*
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class StringExtensionUnitTest : Spek({

    describe("Unit tests for StringExtension") {

        beforeEachTest { }

        afterEachTest { }

        it("occurenceCount should be correct") {
            // Arrange
            val stringToTest = "Hello world says 'Hello world' and in german 'Hallo Welt'"
            val stringToFind = "Hello world"
            val expectedCount = 2

            // Act
            val actualCount = stringToTest.occurenceCount(stringToFind)

            // Assert
            assertEquals(expectedCount, actualCount)
        }

        it("charPositions should be correct") {
            // Arrange
            val stringToTest = "Aber Hallo sagte Achim"
            val charToFind: Char = "A".toCharArray()[0]
            val expectedPositions: List<Int> = listOf(0, 17)

            // Act
            val actualPositions = stringToTest.charPositions(charToFind)

            // Assert
            assertEquals(expectedPositions, actualPositions)
        }

        it("areEqual should be correct") {
            // Arrange
            val stringArrayToTest = arrayOf("Hello", "Hello", "Hello", "Hello")

            // Act
            val areEqual = stringArrayToTest.areEqual()

            // Assert
            assertTrue(areEqual)
        }

        it("areEqual should return false if not equal") {
            // Arrange
            val stringArrayToTest = arrayOf("Hello", "Hallo", "Hello", "Hello")

            // Act
            val areEqual = stringArrayToTest.areEqual()

            // Assert
            assertFalse(areEqual)
        }

        it("excludeAndFindString should return expected string") {
            // Arrange
            val stringArrayToTest = arrayOf("Hello World", "Hallo Welt", "Hola bonita")
            val stringToExclude = "World"
            val stringToFind = "bonita"
            val expectedString = "Hola bonita"

            // Act
            val actualString = stringArrayToTest.excludeAndFindString(stringToExclude, stringToFind)

            // Assert
            assertEquals(expectedString, actualString)
        }
    }
})
