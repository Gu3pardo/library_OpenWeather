package guepardoapps.lib.openweather.services.openweather

import android.content.Context
import guepardoapps.lib.openweather.models.*
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

    fun initialize(context: Context, cityName: String)
    fun start()
    fun dispose()

    fun loadCityData(cityName: String): Boolean
    fun loadWeatherCurrent(): Boolean
    fun loadWeatherForecast(): Boolean
    fun loadUvIndex(): Boolean

    fun searchForecast(searchValue: String): WeatherForecast
}