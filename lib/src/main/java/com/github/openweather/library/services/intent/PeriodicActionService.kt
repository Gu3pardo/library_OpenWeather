package com.github.openweather.library.services.intent

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.github.openweather.library.extensions.airPollutionCurrentDateTime
import com.github.openweather.library.services.openweather.OpenWeatherService
import java.util.*

internal class PeriodicActionService : IntentService("PeriodicActionService") {
    private val tag: String = PeriodicActionService::class.java.simpleName

    override fun onHandleIntent(intent: Intent?) {
        Log.v(tag, "onHandleIntent")

        OpenWeatherService.instance.loadWeatherCurrent()
        OpenWeatherService.instance.loadWeatherForecast()
        OpenWeatherService.instance.loadUvIndex()

        OpenWeatherService.instance.loadCarbonMonoxide(Calendar.getInstance().airPollutionCurrentDateTime(), 1)
        OpenWeatherService.instance.loadNitrogenDioxide(Calendar.getInstance().airPollutionCurrentDateTime(), 1)
        OpenWeatherService.instance.loadOzone(Calendar.getInstance().airPollutionCurrentDateTime(), 1)
        OpenWeatherService.instance.loadSulfurDioxide(Calendar.getInstance().airPollutionCurrentDateTime(), 1)
    }
}