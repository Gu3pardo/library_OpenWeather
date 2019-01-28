package com.github.openweather.library.extensions

import androidx.annotation.NonNull

internal fun String.occurenceCount(@NonNull find: String): Int {
    var lastIndex = 0
    var count = 0

    while (lastIndex != -1) {
        lastIndex = this.indexOf(find, lastIndex)
        if (lastIndex != -1) {
            count++
            lastIndex += find.length
        }
    }

    return count
}

internal fun String.charPositions(char: Char): List<Int> = this.mapIndexed { index, c -> if (c == char) index else -1 }.filter { x -> x != -1 }

internal fun Array<String>.areEqual(): Boolean = this.all { x -> this.all { y -> x == y } }

internal fun Array<String>.excludeAndFindString(@NonNull exclude: String, @NonNull find: String): String? = this.find { x -> !x.contains(exclude) && x.contains(find) }
