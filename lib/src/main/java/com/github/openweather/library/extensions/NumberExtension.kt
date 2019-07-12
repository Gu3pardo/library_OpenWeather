package com.github.openweather.library.extensions

import java.util.*

fun Int.integerFormat(digits: Int): String = String.format(Locale.getDefault(), "%0${digits}d", this)

fun Double.doubleFormat(digits: Int): String = String.format(Locale.getDefault(), "%.${digits}f", this)

fun Number.isZero(): Boolean = this == 0

fun Long.toMillis(): Long = (this * 1000)
