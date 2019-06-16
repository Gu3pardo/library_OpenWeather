package com.github.openweather.library.common

import com.github.guepardoapps.timext.kotlin.extensions.minutes
import java.io.Serializable

internal object Constants {
    object Defaults {
        const val Coordinates = 720.0
        val MinReloadDifference = 5.minutes
        const val Thousand = 1000
        const val Zero = 0
    }

    object OpenWeatherIntentService {
        const val IntentActionKey = "Action"

        enum class IntentActionEnum : Serializable { Boot, PeriodicReload }
    }

    object SharedPref {
        const val Name = "OpenWeatherLib"
        const val CryptoKey = "Tc56di8="

        const val KeyCity = "City"
    }
}