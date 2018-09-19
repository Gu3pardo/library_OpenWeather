package guepardoapps.lib.openweather.extensions

import java.util.*

fun Int.integerFormat(digits: Int): String {
    return String.format(Locale.getDefault(), "%0${digits}d", this)
}

fun Double.doubleFormat(digits: Int): String {
    return String.format(Locale.getDefault(), "%.${digits}f", this)
}

fun Float.floatFormat(digits: Int): String {
    return String.format(Locale.getDefault(), "%.${digits}f", this)
}