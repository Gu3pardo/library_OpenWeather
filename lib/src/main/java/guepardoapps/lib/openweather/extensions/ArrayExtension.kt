package guepardoapps.lib.openweather.extensions

internal fun <T> Array<out T>.second(): T {
    if (isEmpty() || this.size < 2) {
        throw NoSuchElementException("Array is empty or has only one entry.")
    }
    return this[1]
}