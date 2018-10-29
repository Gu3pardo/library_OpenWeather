package com.github.openweather.library.enums

import java.io.Serializable

enum class ForecastDayTime(val hour: Int) : Serializable {
    Null(-1),
    Morning(5),
    Midday(11),
    Afternoon(15),
    Evening(18),
    Night(21)
}