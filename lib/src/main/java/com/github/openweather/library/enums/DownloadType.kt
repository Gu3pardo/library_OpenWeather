package com.github.openweather.library.enums

import java.io.Serializable

internal enum class DownloadType : Serializable {
    Null,
    CarbonMonoxide,
    CityData,
    CityImage,
    CurrentWeather,
    ForecastWeather,
    NitrogenDioxide,
    Ozone,
    SulfurDioxide,
    UvIndex
}