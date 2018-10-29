package com.github.openweather.library.extensions

import java.util.*

fun Calendar.DDMMYYYY(): String {
    return "${this.get(Calendar.DAY_OF_MONTH).integerFormat(2)}.${(this.get(Calendar.MONTH) + 1).integerFormat(2)}.${this.get(Calendar.YEAR).integerFormat(4)}"
}

fun Calendar.hhmm(): String {
    return "${this.get(Calendar.HOUR_OF_DAY).integerFormat(2)}:${this.get(Calendar.MINUTE).integerFormat(2)}"
}

fun Calendar.hhmmss(): String {
    return "${this.get(Calendar.HOUR_OF_DAY).integerFormat(2)}:${this.get(Calendar.MINUTE).integerFormat(2)}:${this.get(Calendar.SECOND).integerFormat(2)}"
}

fun Calendar.airPollutionCurrentDateTime(): String {
    return "${this.get(Calendar.YEAR).integerFormat(4)}-" +
            "${this.get(Calendar.MONTH).integerFormat(2)}-" +
            "${this.get(Calendar.DAY_OF_MONTH).integerFormat(2)}T" +
            "${this.get(Calendar.HOUR_OF_DAY).integerFormat(2)}:" +
            "${this.get(Calendar.MINUTE).integerFormat(2)}:" +
            "${this.get(Calendar.SECOND).integerFormat(2)}Z"
}