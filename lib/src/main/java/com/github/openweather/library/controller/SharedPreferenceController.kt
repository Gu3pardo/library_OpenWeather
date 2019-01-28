package com.github.openweather.library.controller

import android.content.Context
import android.util.Log
import com.andreacioccarelli.cryptoprefs.CryptoPrefs
import com.github.openweather.library.common.Constants

class SharedPreferenceController(context: Context) : ISharedPreferenceController {
    private val tag: String = SharedPreferenceController::class.java.simpleName

    private val cryptoPrefs: CryptoPrefs = CryptoPrefs(context, Constants.SharedPref.Name, Constants.SharedPref.CryptoKey)

    override fun <T : Any> save(key: String, value: T) {
        when (value::class) {
            Boolean::class -> cryptoPrefs.put(key, value as Boolean)
            Byte::class -> cryptoPrefs.put(key, value as Byte)
            Double::class -> cryptoPrefs.put(key, value as Double)
            Float::class -> cryptoPrefs.put(key, value as Float)
            Int::class -> cryptoPrefs.put(key, value as Int)
            Long::class -> cryptoPrefs.put(key, value as Long)
            Short::class -> cryptoPrefs.put(key, value as Short)
            String::class -> cryptoPrefs.put(key, value as String)
            else -> {
                Log.e(tag, "Invalid generic type of $value")
            }
        }
    }

    @Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
    override fun <T : Any> load(key: String, default: T): T = cryptoPrefs.get(key, default)

    override fun exists(key: String): Boolean = cryptoPrefs.allPrefsMap.any { x -> x.key == key }

    override fun remove(key: String) {
        cryptoPrefs.remove(key)
    }

    override fun erase() {
        cryptoPrefs.erase()
    }
}