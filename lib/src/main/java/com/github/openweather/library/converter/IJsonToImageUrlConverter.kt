package com.github.openweather.library.converter

import androidx.annotation.NonNull

internal interface IJsonToImageUrlConverter {
    fun convert(@NonNull jsonString: String): String?
}