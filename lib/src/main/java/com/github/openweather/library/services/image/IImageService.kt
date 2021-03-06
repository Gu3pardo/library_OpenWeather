package com.github.openweather.library.services.image

import android.content.Context
import com.github.openweather.library.enums.UnsplashImageOrientation
import com.github.openweather.library.models.RxOptional
import io.reactivex.subjects.BehaviorSubject

interface IImageService {
    var accessKey: String

    val urlPublishSubject: BehaviorSubject<RxOptional<String>>

    fun initialize(context: Context)

    fun receiveImagePictureUrl(cityName: String, orientation: UnsplashImageOrientation): Boolean
}