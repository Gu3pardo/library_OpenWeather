package guepardoapps.lib.openweather.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import guepardoapps.lib.openweather.logging.Logger
import guepardoapps.lib.openweather.services.intent.PeriodicActionService

internal class PeriodicActionReceiver : BroadcastReceiver() {
    private val tag: String = PeriodicActionReceiver::class.java.simpleName

    companion object {
        const val requestCode: Int = 211990
        const val action: String = "guepardoapps.lib.openweather.receiver.reload"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Logger.instance.verbose(tag, "onReceive")
        context?.startService(Intent(context, PeriodicActionService::class.java))
    }
}