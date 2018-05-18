package guepardoapps.lib.openweather.utils

import android.graphics.Bitmap
import android.graphics.Color
import android.support.test.runner.AndroidJUnit4
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BitmapHelperUnitTest {
    @Test
    fun getCircleBitmap_shouldReturnExpectedBitmap() {
        // Arrange
        val testBitmap: Bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888)
        val height = 25
        val width = 25
        val color = Color.RED

        val expectedBitmap: Bitmap = Bitmap.createBitmap(25, 25, Bitmap.Config.ARGB_8888)

        // Act
        val actualBitmap = BitmapHelper.getCircleBitmap(testBitmap, height, width, color)

        // Assert
        assertEquals(expectedBitmap, actualBitmap)
    }
}
