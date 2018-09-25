package guepardoapps.lib.openweather.services.image

import android.annotation.SuppressLint
import android.content.Context
import guepardoapps.lib.openweather.controller.NetworkController
import guepardoapps.lib.openweather.converter.JsonToImageUrlConverter
import guepardoapps.lib.openweather.enums.DownloadType
import guepardoapps.lib.openweather.enums.UnsplashImageOrientation
import guepardoapps.lib.openweather.logging.Logger
import guepardoapps.lib.openweather.models.RxOptional
import guepardoapps.lib.openweather.services.api.OnApiServiceListener
import guepardoapps.lib.openweather.tasks.ApiRestCallTask
import io.reactivex.subjects.PublishSubject

class ImageService private constructor() : IImageService {
    private val tag: String = ImageService::class.java.simpleName

    private val imageApiUrl: String = "https://api.unsplash.com/search/photos?client_id=%s&orientation=%s&query=%s"

    private var converter: JsonToImageUrlConverter = JsonToImageUrlConverter()

    private lateinit var context: Context
    private lateinit var networkController: NetworkController

    var url: String? = null
        private set(value) {
            field = value
            urlPublishSubject.onNext(RxOptional(value))
        }
    override val urlPublishSubject = PublishSubject.create<RxOptional<String>>()!!

    private val onApiServiceListener = object : OnApiServiceListener {
        override fun onFinished(downloadType: DownloadType, jsonString: String, success: Boolean) {
            Logger.instance.verbose(tag, "Received onFinished")
            when (downloadType) {
                DownloadType.CityData -> {
                    // Nothing to do here
                }
                DownloadType.CityImage -> {
                    handleCityImageUpdate(success, jsonString)
                }
                DownloadType.CurrentWeather -> {
                    // Nothing to do here
                }
                DownloadType.ForecastWeather -> {
                    // Nothing to do here
                }
                DownloadType.UvIndex -> {
                    // Nothing to do here
                }
                DownloadType.Null -> {
                    Logger.instance.error(tag, "Received download update with downloadType Null and jsonString: $jsonString")
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

        doApiRestCall(DownloadType.CityImage, String.format(imageApiUrl, accessKey, orientation.value, cityName))
        return true
    }

    private fun handleCityImageUpdate(success: Boolean, jsonString: String) {
        url = if (!success) {
            null
        } else {
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