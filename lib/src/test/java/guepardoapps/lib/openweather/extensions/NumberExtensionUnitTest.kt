package guepardoapps.lib.openweather.extensions

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

import org.junit.Assert.*
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class NumberExtensionUnitTest : Spek({

    describe("Unit tests for NumberExtension") {

        beforeEachTest { }

        afterEachTest { }

        it("integerFormat should be correct") {
            // Arrange
            val integerToTest = 5
            val charCount = 2
            val expectedString = "05"

            // Act
            val actualString = integerToTest.integerFormat(charCount)

            // Assert
            assertEquals(expectedString, actualString)
        }

        it("doubleFormat should be correct") {
            // Arrange
            val doubleToTest = 5.4
            val decimalCount = 2
            val expectedString = "5.40"

            // Act
            val actualString = doubleToTest.doubleFormat(decimalCount)

            // Assert
            assertEquals(expectedString, actualString)
        }

        it("doubleFormat should be correct") {
            // Arrange
            val floatToTest = 5.402f
            val decimalCount = 4
            val expectedString = "5.4020"

            // Act
            val actualString = floatToTest.floatFormat(decimalCount)

            // Assert
            assertEquals(expectedString, actualString)
        }
    }
})
