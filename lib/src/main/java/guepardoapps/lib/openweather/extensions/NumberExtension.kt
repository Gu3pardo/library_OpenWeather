package guepardoapps.lib.openweather.extensions

import java.util.*

internal fun Int.integerFormat(digits: Int): String {
    return String.format(Locale.getDefault(), "%0${digits}d", this)
}

internal fun Double.doubleFormat(digits: Int): String {
    return String.format(Locale.getDefault(), "%.${digits}f", this)
}

internal fun Float.floatFormat(digits: Int): String {
    return String.format(Locale.getDefault(), "%.${digits}f", this)
}