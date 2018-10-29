package com.github.openweather.library.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.github.openweather.library.services.intent.PeriodicActionService

internal class PeriodicActionReceiver : BroadcastReceiver() {
    private val tag: String = PeriodicActionReceiver::class.java.simpleName

    companion object {
        const val requestCode: Int = 211990
        const val action: String = "com.github.openweather.library.receiver.reload"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.v(tag, "onReceive")
        context?.startService(Intent(context, PeriodicActionService::class.java))
    }
}