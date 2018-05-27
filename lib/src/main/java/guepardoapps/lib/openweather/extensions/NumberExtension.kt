package guepardoapps.lib.openweather.extensions

import java.util.*

fun <T> T.format(digits: Int): String {
    return String.format(Locale.getDefault(), "%0${digits}d", this)
}

fun <T> T.decimalFormat(digits: Int): String {
    return String.format(Locale.getDefault(), "%.${digits}f", this)
}