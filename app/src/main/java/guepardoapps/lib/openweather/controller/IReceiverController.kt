package guepardoapps.lib.openweather.controller

import android.content.BroadcastReceiver
import android.support.annotation.NonNull

interface IReceiverController {
    fun registerReceiver(@NonNull broadcastReceiver: BroadcastReceiver, @NonNull actions: Array<String>)

    fun unregisterReceiver(@NonNull broadcastReceiver: BroadcastReceiver)

    fun dispose()
}