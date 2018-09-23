package guepardoapps.lib.openweather.services.image

import android.content.Context
import guepardoapps.lib.openweather.enums.UnsplashImageOrientation
import guepardoapps.lib.openweather.models.RxOptional
import io.reactivex.subjects.PublishSubject

interface IImageService {
    var accessKey: String

    val urlPublishSubject: PublishSubject<RxOptional<String>>

    fun initialize(context: Context)

    fun receiveImagePictureUrl(cityName: String, orientation: UnsplashImageOrientation): Boolean
}