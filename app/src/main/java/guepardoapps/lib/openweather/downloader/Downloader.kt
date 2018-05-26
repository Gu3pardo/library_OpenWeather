package guepardoapps.lib.openweather.downloader

import android.content.Context
import android.os.AsyncTask
import guepardoapps.lib.openweather.controller.BroadcastController
import guepardoapps.lib.openweather.enums.DownloadResult
import guepardoapps.lib.openweather.enums.DownloadType
import guepardoapps.lib.openweather.utils.Logger
import okhttp3.OkHttpClient
import okhttp3.Request

class Downloader(context: Context,
                 override var city: String,
                 override var apiKey: String) : IDownloader {

    private val tag: String = Downloader::class.java.canonicalName

    private val currentWeatherUrl: String = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&APPID=%s"
    private val forecastWeatherUrl: String = "http://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric&APPID=%s"

    private var broadcastController: BroadcastController? = null
    private var onDownloadListener: OnDownloadListener? = null

    init {
        broadcastController = BroadcastController(context)
    }

    override fun setOnDownloadListener(onDownloadListener: OnDownloadListener) {
        this.onDownloadListener = onDownloadListener
    }

    override fun currentWeather(): DownloadResult {
        if (city.isEmpty()) {
            Logger.instance.warning(tag, "currentWeather: City needs to be set before call!")
            return DownloadResult.InvalidCity
        }

        if (apiKey.isEmpty()) {
            Logger.instance.warning(tag, "currentWeather: ApiKey needs to be set before call!")
            return DownloadResult.InvalidApiKey
        }

        return DownloadResult.Performing
    }

    override fun forecastWeather(): DownloadResult {
        if (city.isEmpty()) {
            Logger.instance.warning(tag, "forecastWeather: City needs to be set before call!")
            return DownloadResult.InvalidCity
        }

        if (apiKey.isEmpty()) {
            Logger.instance.warning(tag, "forecastWeather: ApiKey needs to be set before call!")
            return DownloadResult.InvalidApiKey
        }

        return DownloadResult.Performing
    }

    private class DownloadWeatherTask : AsyncTask<String, Void, String>() {
        private val tag: String = DownloadWeatherTask::class.java.canonicalName

        var downloadType: DownloadType = DownloadType.Null
        var onDownloadListener: OnDownloadListener? = null

        override fun doInBackground(vararg requestUrls: String?): String {
            if (downloadType == DownloadType.Null) {
                onDownloadListener?.onFinished(downloadType, "", false)
                return ""
            }

            for (requestUrl in requestUrls) {
                var result = ""
                var success = false

                try {
                    val okHttpClient = OkHttpClient()
                    val request = Request.Builder().url(requestUrl).build()
                    val response = okHttpClient.newCall(request).execute()
                    val responseBody = response.body()

                    if (responseBody != null) {
                        result = responseBody.string()
                        success = true
                        Logger.instance.debug(tag, result)
                    } else {
                        Logger.instance.error(tag, "ResponseBody is null!")
                    }
                } catch (exception: Exception) {
                    Logger.instance.error(tag, exception)
                } finally {
                    onDownloadListener?.onFinished(downloadType, result, success)
                }
            }

            return ""
        }
    }
}