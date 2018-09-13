package guepardoapps.lib.openweather.services.intent

import android.app.IntentService
import android.content.Intent
import guepardoapps.lib.openweather.logging.Logger
import guepardoapps.lib.openweather.services.openweather.OpenWeatherService

internal class PeriodicActionService : IntentService("PeriodicActionService") {
    private val tag: String = PeriodicActionService::class.java.simpleName

    override fun onHandleIntent(intent: Intent?) {
        Logger.instance.verbose(tag, "onHandleIntent")
        OpenWeatherService.instance.loadWeatherCurrent()
        OpenWeatherService.instance.loadWeatherForecast()
        OpenWeatherService.instance.loadUvIndex()
    }
}