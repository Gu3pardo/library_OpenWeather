package guepardoapps.lib.openweather.services.openweather

import android.content.Context
import guepardoapps.lib.openweather.models.RxOptional
import guepardoapps.lib.openweather.models.WeatherCurrent
import guepardoapps.lib.openweather.models.WeatherForecast
import io.reactivex.subjects.PublishSubject

internal interface IOpenWeatherService {
    var city: String
    var apiKey: String

    var notificationEnabled: Boolean
    var receiverActivity: Class<*>?

    var wallpaperEnabled: Boolean

    var reloadEnabled: Boolean
    var reloadTimeout: Long

    val weatherCurrentPublishSubject: PublishSubject<RxOptional<WeatherCurrent>>
    val weatherForecastPublishSubject: PublishSubject<RxOptional<WeatherForecast>>

    fun initialize(context: Context)
    fun start()
    fun dispose()

    fun loadWeatherCurrent()
    fun loadWeatherForecast()

    fun searchForecast(forecast: WeatherForecast, searchValue: String): WeatherForecast
}