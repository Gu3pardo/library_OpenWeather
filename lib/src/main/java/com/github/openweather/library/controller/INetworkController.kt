package com.github.openweather.library.controller

import android.net.NetworkInfo

internal interface INetworkController {
    fun isInternetConnected(): Pair<NetworkInfo?, Boolean>

    fun isWifiConnected(): Pair<NetworkInfo?, Boolean>

    fun isHomeWifiConnected(ssid: String): Boolean

    fun isMobileConnected(): Pair<NetworkInfo?, Boolean>
}