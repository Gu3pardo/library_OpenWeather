package guepardoapps.lib.openweather.converter

import android.support.annotation.NonNull

internal interface IJsonToImageUrlConverter {
    fun convert(@NonNull jsonString: String): String?
}