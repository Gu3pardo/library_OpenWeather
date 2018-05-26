package guepardoapps.lib.openweather.controller

import android.support.annotation.NonNull
import java.io.Serializable

interface IBroadcastController {
    fun simpleBroadcast(@NonNull broadcast: String)

    fun boolBroadcast(@NonNull broadcast: String, @NonNull key: String, data: Boolean)

    fun intBroadcast(@NonNull broadcast: String, @NonNull key: String, data: Int)

    fun doubleBroadcast(@NonNull broadcast: String, @NonNull key: String, data: Double)

    fun stringBroadcast(@NonNull broadcast: String, @NonNull key: String, data: String)

    fun serializableBroadcast(@NonNull broadcast: String, @NonNull key: String, data: Serializable)

    fun boolArrayBroadcast(@NonNull broadcast: String, @NonNull keyArray: Array<String>, dataArray: Array<Boolean>)

    fun intArrayBroadcast(@NonNull broadcast: String, @NonNull keyArray: Array<String>, dataArray: Array<Int>)

    fun doubleArrayBroadcast(@NonNull broadcast: String, @NonNull keyArray: Array<String>, dataArray: Array<Double>)

    fun stringArrayBroadcast(@NonNull broadcast: String, @NonNull keyArray: Array<String>, dataArray: Array<String>)

    fun serializableArrayBroadcast(@NonNull broadcast: String, @NonNull keyArray: Array<String>, dataArray: Array<Serializable>)
}