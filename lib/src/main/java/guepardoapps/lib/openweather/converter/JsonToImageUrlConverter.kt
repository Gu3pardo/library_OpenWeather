package guepardoapps.lib.openweather.converter

import guepardoapps.lib.openweather.common.Constants
import guepardoapps.lib.openweather.logging.Logger
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
            Logger.instance.error(tag, exception)
            null
        }
    }
}