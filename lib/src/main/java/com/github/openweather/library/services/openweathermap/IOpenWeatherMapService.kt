package com.github.openweather.library.services.openweathermap

import android.content.Context
import com.github.openweather.library.models.*
import io.reactivex.subjects.BehaviorSubject

interface IOpenWeatherMapService {
    var apiKey: String

    var notificationEnabledWeatherCurrent: Boolean

    var notificationEnabledWeatherForecast: Boolean

    var notificationEnabledUvIndex: Boolean

    var receiverActivity: Class<*>?

    var wallpaperEnabled: Boolean

    val cityPublishSubject: BehaviorSubject<RxOptional<City>>

    val weatherCurrentPublishSubject: BehaviorSubject<RxOptional<WeatherCurrent>>

    val weatherForecastPublishSubject: BehaviorSubject<RxOptional<WeatherForecast>>

    val uvIndexPublishSubject: BehaviorSubject<RxOptional<UvIndex>>

    val carbonMonoxidePublishSubject: BehaviorSubject<RxOptional<CarbonMonoxide>>

    val nitrogenDioxidePublishSubject: BehaviorSubject<RxOptional<NitrogenDioxide>>

    val ozonePublishSubject: BehaviorSubject<RxOptional<Ozone>>

    val sulfurDioxidePublishSubject: BehaviorSubject<RxOptional<SulfurDioxide>>

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