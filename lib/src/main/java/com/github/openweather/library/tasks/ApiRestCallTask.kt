package com.github.openweather.library.tasks

import android.os.AsyncTask
import android.util.Log
import com.github.openweather.library.common.Constants
import com.github.openweather.library.enums.DownloadType
import com.github.openweather.library.services.api.OnApiServiceListener
import okhttp3.OkHttpClient
import okhttp3.Request

internal class ApiRestCallTask : AsyncTask<String, Void, String>() {
    private val tag: String = ApiRestCallTask::class.java.simpleName

    lateinit var downloadType: DownloadType
    lateinit var onApiServiceListener: OnApiServiceListener

    override fun doInBackground(vararg requestUrls: String?): String {
        var result = Constants.String.Empty

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
                    Log.e(tag, "ResponseBody is null!")
                }
            } catch (exception: Exception) {
                Log.e(tag, exception.message)
            }
        }

        return result
    }

    override fun onPostExecute(result: String?) {
        if (result.isNullOrEmpty()) {
            onApiServiceListener.onFinished(downloadType, Constants.String.Empty, false)
            return
        }
        onApiServiceListener.onFinished(downloadType, result!!, true)
    }
}