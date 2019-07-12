package com.github.openweather.library.converter

import android.util.Log
import org.json.JSONObject

internal class JsonToImageUrlConverter : IJsonToImageUrlConverter {
    private val tag: String = JsonToImageUrlConverter::class.java.simpleName

    override fun convert(jsonString: String): String? {
        return try {
            JSONObject(jsonString)
                    .getJSONArray("results")
                    .getJSONObject(0)
                    .getJSONObject("urls")
                    .getString("small")
        } catch (exception: Exception) {
            Log.e(tag, exception.message)
            null
        }
    }
}