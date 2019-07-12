package com.github.openweather.library.controller

import android.content.Context
import androidx.annotation.NonNull
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.util.Log

@Suppress("DEPRECATION")
internal class NetworkController(@NonNull private val context: Context) : INetworkController {

    private val tag: String = NetworkController::class.java.simpleName

    private val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    override fun isInternetConnected(): Pair<NetworkInfo?, Boolean> {
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return Pair(activeNetwork, activeNetwork?.isConnectedOrConnecting == true)
    }

    override fun isWifiConnected(): Pair<NetworkInfo?, Boolean> {
        val networkPair = isInternetConnected()
        val isWiFi: Boolean = networkPair.first?.type == ConnectivityManager.TYPE_WIFI
        return Pair(networkPair.first, networkPair.second && isWiFi)
    }

    override fun isHomeWifiConnected(ssid: String): Boolean {
        val networkPair = isWifiConnected()

        if (networkPair.second && networkPair.first?.type == ConnectivityManager.TYPE_WIFI) {
            return try {
                wifiManager.connectionInfo.ssid.contains(ssid)
            } catch (exception: Exception) {
                val errorString = if (exception.message == null) "HomeSSID failed" else exception.message
                Log.e(tag, errorString)
                false
            }
        }

        return false
    }

    override fun isMobileConnected(): Pair<NetworkInfo?, Boolean> {
        val networkPair = isInternetConnected()
        val isMobile: Boolean = networkPair.first?.type == ConnectivityManager.TYPE_MOBILE
        return Pair(networkPair.first, networkPair.second && isMobile)
    }
}