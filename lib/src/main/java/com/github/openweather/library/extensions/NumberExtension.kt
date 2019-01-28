package com.github.openweather.library.extensions

import com.github.openweather.library.common.Constants
import java.util.*

fun Int.integerFormat(digits: Int): String = String.format(Locale.getDefault(), "%0${digits}d", this)

fun Double.doubleFormat(digits: Int): String = String.format(Locale.getDefault(), "%.${digits}f", this)

fun Float.floatFormat(digits: Int): String = String.format(Locale.getDefault(), "%.${digits}f", this)

fun Number.isZero(): Boolean = this == Constants.Defaults.Zero

fun Long.toMillis(): Long = (this * Constants.Defaults.Thousand)
