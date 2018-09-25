package guepardoapps.lib.openweather.converter

import androidx.annotation.NonNull

internal interface IJsonToImageUrlConverter {
    fun convert(@NonNull jsonString: String): String?
}