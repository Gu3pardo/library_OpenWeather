package guepardoapps.lib.openweather.extensions

import java.util.*

internal fun Calendar.DDMMYYYY(): String {
    return "${this.get(Calendar.DAY_OF_MONTH).integerFormat(2)}.${(this.get(Calendar.MONTH) + 1).integerFormat(2)}.${this.get(Calendar.YEAR).integerFormat(4)}"
}

internal fun Calendar.hhmm(): String {
    return "${this.get(Calendar.HOUR_OF_DAY).integerFormat(2)}:${this.get(Calendar.MINUTE).integerFormat(2)}"
}

internal fun Calendar.hhmmss(): String {
    return "${this.get(Calendar.HOUR_OF_DAY).integerFormat(2)}:${this.get(Calendar.MINUTE).integerFormat(2)}:${this.get(Calendar.SECOND).integerFormat(2)}"
}