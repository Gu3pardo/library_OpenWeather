package com.github.openweather.library.extensions

internal fun <T> Array<out T>.second(): T =
        if (isEmpty() || this.size < 2) {
            throw NoSuchElementException("Array is empty or has only one entry.")
        } else {
            this[1]
        }