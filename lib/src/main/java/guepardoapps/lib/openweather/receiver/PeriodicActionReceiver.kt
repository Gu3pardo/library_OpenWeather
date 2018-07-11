package guepardoapps.lib.openweather.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import guepardoapps.lib.openweather.services.intent.PeriodicActionService
import guepardoapps.lib.openweather.utils.Logger

class PeriodicActionReceiver : BroadcastReceiver() {
    private val tag: String = PeriodicActionReceiver::class.java.simpleName

    companion object {
        val requestCode: Int = 211990
        val action: String = "guepardoapps.lib.openweather.receiver.load"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Logger.instance.verbose(tag, "onReceive")
        context?.startService(Intent(context, PeriodicActionService::class.java))
    }
}