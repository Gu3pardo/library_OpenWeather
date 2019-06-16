package com.github.openweather.library.extensions

val String.Companion.degreeSign: String
    get() = 0x00B0.toString()

val String.Companion.empty: String
    get() = ""

val String.Companion.newLine: String
    get() = "\n"
