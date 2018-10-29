package com.github.openweather.library.enums

import java.io.Serializable

enum class UnsplashImageOrientation(val value: String) :Serializable {
    Landscape("landscape"),
    Portrait("portrait"),
    Squarish("squarish")
}