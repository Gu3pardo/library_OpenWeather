package guepardoapps.lib.openweather.controller

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.NonNull
import guepardoapps.lib.openweather.utils.Logger
import java.io.Serializable

class BroadcastController(@NonNull private val context: Context) : IBroadcastController {

    private val tag: String = BroadcastController::class.java.canonicalName

    override fun simpleBroadcast(broadcast: String) {
        context.sendBroadcast(Intent(broadcast))
    }

    override fun boolBroadcast(broadcast: String, key: String, data: Boolean) {
        val bundle = Bundle()
        bundle.putBoolean(key, data)
        val intent = Intent(broadcast)
        intent.putExtras(bundle)
        context.sendBroadcast(intent)
    }

    override fun intBroadcast(broadcast: String, key: String, data: Int) {
        val bundle = Bundle()
        bundle.putInt(key, data)
        val intent = Intent(broadcast)
        intent.putExtras(bundle)
        context.sendBroadcast(intent)
    }

    override fun doubleBroadcast(broadcast: String, key: String, data: Double) {
        val bundle = Bundle()
        bundle.putDouble(key, data)
        val intent = Intent(broadcast)
        intent.putExtras(bundle)
        context.sendBroadcast(intent)
    }

    override fun stringBroadcast(broadcast: String, key: String, data: String) {
        val bundle = Bundle()
        bundle.putString(key, data)
        val intent = Intent(broadcast)
        intent.putExtras(bundle)
        context.sendBroadcast(intent)
    }

    override fun serializableBroadcast(broadcast: String, key: String, data: Serializable) {
        val bundle = Bundle()
        bundle.putSerializable(key, data)
        val intent = Intent(broadcast)
        intent.putExtras(bundle)
        context.sendBroadcast(intent)
    }

    override fun boolArrayBroadcast(broadcast: String, keyArray: Array<String>, dataArray: Array<Boolean>) {
        if (keyArray.size != dataArray.size) {
            Logger.instance.error(tag, "boolArrayBroadcast: Incompatible array size: keyArray: ${keyArray.size}, dataArray: ${dataArray.size}")
            return
        }

        val bundle = Bundle()
        for (index in 0 until keyArray.size step 1) {
            bundle.putBoolean(keyArray[index], dataArray[index])
        }

        val intent = Intent(broadcast)
        intent.putExtras(bundle)

        context.sendBroadcast(intent)
    }

    override fun intArrayBroadcast(broadcast: String, keyArray: Array<String>, dataArray: Array<Int>) {
        if (keyArray.size != dataArray.size) {
            Logger.instance.error(tag, "intArrayBroadcast: Incompatible array size: keyArray: ${keyArray.size}, dataArray: ${dataArray.size}")
            return
        }

        val bundle = Bundle()
        for (index in 0 until keyArray.size step 1) {
            bundle.putInt(keyArray[index], dataArray[index])
        }

        val intent = Intent(broadcast)
        intent.putExtras(bundle)

        context.sendBroadcast(intent)
    }

    override fun doubleArrayBroadcast(broadcast: String, keyArray: Array<String>, dataArray: Array<Double>) {
        if (keyArray.size != dataArray.size) {
            Logger.instance.error(tag, "doubleArrayBroadcast: Incompatible array size: keyArray: ${keyArray.size}, dataArray: ${dataArray.size}")
            return
        }

        val bundle = Bundle()
        for (index in 0 until keyArray.size step 1) {
            bundle.putDouble(keyArray[index], dataArray[index])
        }

        val intent = Intent(broadcast)
        intent.putExtras(bundle)

        context.sendBroadcast(intent)
    }

    override fun stringArrayBroadcast(broadcast: String, keyArray: Array<String>, dataArray: Array<String>) {
        if (keyArray.size != dataArray.size) {
            Logger.instance.error(tag, "stringArrayBroadcast: Incompatible array size: keyArray: ${keyArray.size}, dataArray: ${dataArray.size}")
            return
        }

        val bundle = Bundle()
        for (index in 0 until keyArray.size step 1) {
            bundle.putString(keyArray[index], dataArray[index])
        }

        val intent = Intent(broadcast)
        intent.putExtras(bundle)

        context.sendBroadcast(intent)
    }

    override fun serializableArrayBroadcast(broadcast: String, keyArray: Array<String>, dataArray: Array<Serializable>) {
        if (keyArray.size != dataArray.size) {
            Logger.instance.error(tag, "serializableArrayBroadcast: Incompatible array size: keyArray: ${keyArray.size}, dataArray: ${dataArray.size}")
            return
        }

        val bundle = Bundle()
        for (index in 0 until keyArray.size step 1) {
            bundle.putSerializable(keyArray[index], dataArray[index])
        }

        val intent = Intent(broadcast)
        intent.putExtras(bundle)

        context.sendBroadcast(intent)
    }
}