package guepardoapps.lib.openweather.services.intent

import android.app.IntentService
import android.content.Intent
import guepardoapps.lib.openweather.services.openweather.OpenWeatherService
import guepardoapps.lib.openweather.utils.Logger

class PeriodicActionService : IntentService("PeriodicActionService") {
    private val tag: String = PeriodicActionService::class.java.simpleName

    override fun onHandleIntent(intent: Intent?) {
        Logger.instance.verbose(tag, "onHandleIntent")

        OpenWeatherService.instance.loadWeatherCurrent()
        OpenWeatherService.instance.loadWeatherForecast()
    }
}