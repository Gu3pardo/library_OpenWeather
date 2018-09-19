package guepardoapps.lib.openweather.controller

import android.content.Context
import com.andreacioccarelli.cryptoprefs.CryptoPrefs
import guepardoapps.lib.openweather.common.Constants
import guepardoapps.lib.openweather.logging.Logger

class SharedPreferenceController(context: Context) : ISharedPreferenceController {
    private val tag: String = SharedPreferenceController::class.java.simpleName

    private val cryptoPrefs: CryptoPrefs = CryptoPrefs(context, Constants.sharedPrefName, Constants.sharedPrefCryptoKey)

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
                Logger.instance.error(tag, "Invalid generic type of $value")
            }
        }
    }

    @Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
    override fun <T : Any> load(key: String, default: T): T {
        return when (default::class) {
            Boolean::class -> cryptoPrefs.getBoolean(key, default as Boolean)
            Byte::class -> cryptoPrefs.getByte(key, default as Byte)
            Double::class -> cryptoPrefs.getDouble(key, default as Double)
            Float::class -> cryptoPrefs.getFloat(key, default as Float)
            Int::class -> cryptoPrefs.getInt(key, default as Int)
            Long::class -> cryptoPrefs.getLong(key, default as Long)
            Short::class -> cryptoPrefs.getShort(key, default as Short)
            String::class -> cryptoPrefs.getString(key, default)
            else -> {
                Logger.instance.error(tag, "Invalid generic type of $default")
                return default
            }
        } as T
    }

    override fun exists(key: String): Boolean {
        return cryptoPrefs.allPrefsMap.any { x -> x.key == key }
    }

    override fun remove(key: String) {
        cryptoPrefs.remove(key)
    }

    override fun erase() {
        cryptoPrefs.erase()
    }
}