package com.github.openweather.library.services.intent

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.github.openweather.library.R
import com.github.openweather.library.enums.IntentActionEnum
import com.github.openweather.library.extensions.airPollutionCurrentDateTime
import com.github.openweather.library.services.openweathermap.OpenWeatherMapService
import java.util.*

internal class OpenWeatherIntentService : IntentService("OpenWeatherIntentService") {
    override fun onHandleIntent(intent: Intent?) {
        when (intent?.extras?.getSerializable(resources.getString(R.string.openWeatherIntentServiceIntentActionKey))) {
            IntentActionEnum.Boot,
            IntentActionEnum.PeriodicReload -> loadAll()
            null -> Log.e(OpenWeatherIntentService::class.java.simpleName, "Received null intent extra")
        }
    }

    private fun loadAll() {
        OpenWeatherMapService.instance.loadWeatherCurrent()

        OpenWeatherMapService.instance.loadWeatherForecast()

        OpenWeatherMapService.instance.loadUvIndex()

        OpenWeatherMapService.instance.loadCarbonMonoxide(Calendar.getInstance().airPollutionCurrentDateTime(), 1)

        OpenWeatherMapService.instance.loadNitrogenDioxide(Calendar.getInstance().airPollutionCurrentDateTime(), 1)

        OpenWeatherMapService.instance.loadOzone(Calendar.getInstance().airPollutionCurrentDateTime(), 1)

        OpenWeatherMapService.instance.loadSulfurDioxide(Calendar.getInstance().airPollutionCurrentDateTime(), 1)
    }
}
