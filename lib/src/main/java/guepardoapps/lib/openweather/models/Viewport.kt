package guepardoapps.lib.openweather.models

import guepardoapps.lib.openweather.annotations.JsonKey

@JsonKey("geometry", "viewport")
class Viewport {
    private val tag: String = Viewport::class.java.simpleName

    @JsonKey("viewport", "northeast")
    var northeast: Coordinates2 = Coordinates2()

    @JsonKey("viewport", "southwest")
    var southwest: Coordinates2 = Coordinates2()

    override fun toString(): String {
        return "{Class: $tag, Northeast: $northeast, Southwest: $southwest}"
    }
}