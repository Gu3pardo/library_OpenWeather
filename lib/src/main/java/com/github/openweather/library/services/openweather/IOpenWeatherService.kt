package com.github.openweather.library.services.openweather

import android.content.Context
import com.github.openweather.library.models.*
import io.reactivex.subjects.PublishSubject

interface IOpenWeatherService {
    var apiKey: String

    var notificationEnabled: Boolean
    var receiverActivity: Class<*>?

    var wallpaperEnabled: Boolean

    var reloadEnabled: Boolean
    var reloadTimeout: Long

    val cityPublishSubject: PublishSubject<RxOptional<City>>
    val weatherCurrentPublishSubject: PublishSubject<RxOptional<WeatherCurrent>>
    val weatherForecastPublishSubject: PublishSubject<RxOptional<WeatherForecast>>
    val uvIndexPublishSubject: PublishSubject<RxOptional<UvIndex>>

    val carbonMonoxidePublishSubject: PublishSubject<RxOptional<CarbonMonoxide>>
    val nitrogenDioxidePublishSubject: PublishSubject<RxOptional<NitrogenDioxide>>
    val ozonePublishSubject: PublishSubject<RxOptional<Ozone>>
    val sulfurDioxidePublishSubject: PublishSubject<RxOptional<SulfurDioxide>>

    fun initialize(context: Context, cityName: String)
    fun start()
    fun dispose()

    fun loadCityData(cityName: String): Boolean
    fun loadWeatherCurrent(): Boolean
    fun loadWeatherForecast(): Boolean
    fun loadUvIndex(): Boolean

    fun loadCarbonMonoxide(dateTime: String, accuracy: Int): Boolean
    fun loadNitrogenDioxide(dateTime: String, accuracy: Int): Boolean
    fun loadOzone(dateTime: String, accuracy: Int): Boolean
    fun loadSulfurDioxide(dateTime: String, accuracy: Int): Boolean

    fun searchForecast(searchValue: String): WeatherForecast
}