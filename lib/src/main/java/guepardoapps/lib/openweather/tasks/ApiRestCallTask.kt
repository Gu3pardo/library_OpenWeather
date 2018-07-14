package guepardoapps.lib.openweather.tasks

import android.os.AsyncTask
import guepardoapps.lib.openweather.services.api.OnApiServiceListener
import guepardoapps.lib.openweather.enums.DownloadType
import guepardoapps.lib.openweather.utils.Logger
import okhttp3.OkHttpClient
import okhttp3.Request

internal class ApiRestCallTask : AsyncTask<String, Void, String>() {
    private val tag: String = ApiRestCallTask::class.java.simpleName

    lateinit var downloadType: DownloadType
    lateinit var onApiServiceListener: OnApiServiceListener

    override fun doInBackground(vararg requestUrls: String?): String {
        var result = ""

        if (downloadType == DownloadType.Null) {
            return result
        }

        val okHttpClient = OkHttpClient()
        requestUrls.forEach { requestUrl ->
            try {
                val request = Request.Builder().url(requestUrl!!).build()
                val response = okHttpClient.newCall(request).execute()
                val responseBody = response.body()

                if (responseBody != null) {
                    result = responseBody.string()
                } else {
                    Logger.instance.error(tag, "ResponseBody is null!")
                }
            } catch (exception: Exception) {
                Logger.instance.error(tag, exception)
            }
        }

        return result
    }

    override fun onPostExecute(result: String?) {
        if (result.isNullOrEmpty()) {
            onApiServiceListener.onFinished(downloadType, "", false)
            return
        }
        onApiServiceListener.onFinished(downloadType, result!!, true)
    }
}