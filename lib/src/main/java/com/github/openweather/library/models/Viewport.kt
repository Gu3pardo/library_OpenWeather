package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey

@JsonKey("geometry", "viewport")
internal class Viewport : JsonModel {
    private val tag: String = Viewport::class.java.simpleName

    @JsonKey("viewport", "northeast")
    var northeast: Coordinates2 = Coordinates2()

    @JsonKey("viewport", "southwest")
    var southwest: Coordinates2 = Coordinates2()

    override fun toString(): String = "{Class: $tag, Northeast: $northeast, Southwest: $southwest}"
}