package com.github.openweather.library.services.image

import android.content.Context
import com.github.openweather.library.enums.UnsplashImageOrientation
import com.github.openweather.library.models.RxOptional
import io.reactivex.subjects.PublishSubject

interface IImageService {
    var accessKey: String

    val urlPublishSubject: PublishSubject<RxOptional<String>>

    fun initialize(context: Context)

    fun receiveImagePictureUrl(cityName: String, orientation: UnsplashImageOrientation): Boolean
}