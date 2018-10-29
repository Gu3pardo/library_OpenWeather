package com.github.openweather.library.converter

import android.util.Log
import com.github.openweather.library.common.Constants
import org.json.JSONObject

internal class JsonToImageUrlConverter : IJsonToImageUrlConverter {
    private val tag: String = JsonToImageUrlConverter::class.java.simpleName

    override fun convert(jsonString: String): String? {
        return try {
            val jsonObject = JSONObject(jsonString)
            val firstResultJsonObject = jsonObject.getJSONArray("results").getJSONObject(Constants.Defaults.Zero)
            val urls = firstResultJsonObject.getJSONObject("urls")
            urls.getString("small")
        } catch (exception: Exception) {
            Log.e(tag, exception.message)
            null
        }
    }
}