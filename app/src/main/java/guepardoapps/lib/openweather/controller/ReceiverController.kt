package guepardoapps.lib.openweather.controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import guepardoapps.lib.openweather.utils.Logger

class ReceiverController(private val context: Context) : IReceiverController {
    private val tag: String = ReceiverController::class.java.canonicalName

    private val registeredReceiver: MutableList<BroadcastReceiver> = mutableListOf()

    override fun registerReceiver(broadcastReceiver: BroadcastReceiver, actions: Array<String>) {
        val intentFilter = IntentFilter()

        for (action in actions) {
            intentFilter.addAction(action)
        }

        unregisterReceiver(broadcastReceiver)

        context.registerReceiver(broadcastReceiver, intentFilter)
        registeredReceiver.add(broadcastReceiver)
    }

    override fun unregisterReceiver(broadcastReceiver: BroadcastReceiver) {
        for (receiver in registeredReceiver) {
            if (receiver === broadcastReceiver) {
                try {
                    context.unregisterReceiver(receiver)
                    registeredReceiver.remove(receiver)
                } catch (exception: Exception) {
                    Logger.instance.error(tag, exception)
                }
                break
            }
        }
    }

    override fun dispose() {
        for (receiver in registeredReceiver) {
            try {
                context.unregisterReceiver(receiver)
                registeredReceiver.remove(receiver)
            } catch (exception: Exception) {
                Logger.instance.error(tag, exception)
            }
        }
    }
}