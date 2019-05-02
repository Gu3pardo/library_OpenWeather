package com.github.openweather.library.services.image

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.github.guepardoapps.timext.kotlin.extensions.minus
import com.github.openweather.library.common.Constants
import com.github.openweather.library.controller.NetworkController
import com.github.openweather.library.converter.JsonToImageUrlConverter
import com.github.openweather.library.enums.DownloadType
import com.github.openweather.library.enums.UnsplashImageOrientation
import com.github.openweather.library.models.RxOptional
import com.github.openweather.library.services.api.OnApiServiceListener
import com.github.openweather.library.tasks.ApiRestCallTask
import io.reactivex.subjects.BehaviorSubject
import java.util.*

class ImageService private constructor() : IImageService {
    private val tag: String = ImageService::class.java.simpleName

    private val imageApiUrl: String = "https://api.unsplash.com/search/photos?client_id=%s&orientation=%s&query=%s"

    private var converter: JsonToImageUrlConverter = JsonToImageUrlConverter()

    private lateinit var context: Context
    private lateinit var networkController: NetworkController

    private var url: String? = null
        private set(value) {
            field = value
            urlPublishSubject.onNext(RxOptional(value))
        }
    override val urlPublishSubject = BehaviorSubject.create<RxOptional<String>>()
    private var lastCallDate: Date = Date(0)

    private val onApiServiceListener = object : OnApiServiceListener {
        override fun onFinished(downloadType: DownloadType, jsonString: String, success: Boolean) {
            Log.v(tag, "Received onFinished")
            when (downloadType) {
                DownloadType.CityImage -> handleCityImageUpdate(success, jsonString)
                DownloadType.Null -> Log.e(tag, "Received download update with downloadType Null and jsonString: $jsonString")
                else -> {
                    // Nothing to do here
                }
            }
        }
    }

    override lateinit var accessKey: String

    private object Holder {
        @SuppressLint("StaticFieldLeak")
        val instance: ImageService = ImageService()
    }

    companion object {
        val instance: ImageService by lazy { Holder.instance }
    }

    override fun initialize(context: Context) {
        this.context = context
        this.networkController = NetworkController(this.context)
    }

    override fun receiveImagePictureUrl(cityName: String, orientation: UnsplashImageOrientation): Boolean {
        if (cityName.isEmpty()) {
            return false
        }

        if (Date() - lastCallDate < Constants.Defaults.MinReloadDifference) {
            // Set it to itself again to trigger subscriptions
            url = url
        } else {
            doApiRestCall(DownloadType.CityImage, String.format(imageApiUrl, accessKey, orientation.value, cityName))
        }

        return true
    }

    private fun handleCityImageUpdate(success: Boolean, jsonString: String) {
        url = if (!success) {
            null
        } else {
            lastCallDate = Date()
            converter.convert(jsonString)
        }
    }

    private fun doApiRestCall(downloadType: DownloadType, url: String) {
        val task = ApiRestCallTask()
        task.onApiServiceListener = onApiServiceListener
        task.downloadType = downloadType
        task.execute(url)
    }
}