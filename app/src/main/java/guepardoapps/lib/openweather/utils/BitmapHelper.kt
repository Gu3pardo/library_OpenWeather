package guepardoapps.lib.openweather.utils

import android.graphics.*
import android.support.annotation.NonNull

class BitmapHelper {
    companion object {
        fun getCircleBitmap(@NonNull bitmap: Bitmap,
                            height: Int,
                            width: Int,
                            color: Int): Bitmap {
            val output: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

            val rect = Rect(0, 0, width, height)
            val rectF = RectF(rect)

            val paint = Paint()
            paint.isAntiAlias = true
            paint.color = color
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

            val canvas = Canvas(output)
            canvas.drawARGB(0, 0, 0, 0)
            canvas.drawOval(rectF, paint)
            canvas.drawBitmap(bitmap, rect, rect, paint)

            bitmap.recycle()

            return output
        }
    }
}