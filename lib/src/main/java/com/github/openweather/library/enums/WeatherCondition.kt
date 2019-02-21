package com.github.openweather.library.enums

import com.github.openweather.library.R
import java.io.Serializable

enum class WeatherCondition(val id: Int, val description: String, val wallpaperId: Int, val iconId: Int, var count: Int = 0) : Serializable {
    Null(0, "Null", R.drawable.weather_wallpaper_dummy, R.drawable.weather_dummy),
    Clear(1, "Clear", R.drawable.weather_wallpaper_clear, R.drawable.weather_clear),
    Clouds(2, "Clouds", R.drawable.weather_wallpaper_cloud, R.drawable.weather_cloud),
    Drizzle(3, "Drizzle", R.drawable.weather_wallpaper_drizzle, R.drawable.weather_drizzle),
    Fog(4, "Fog", R.drawable.weather_wallpaper_fog, R.drawable.weather_fog),
    Haze(5, "Haze", R.drawable.weather_wallpaper_haze, R.drawable.weather_haze),
    Mist(6, "Mist", R.drawable.weather_wallpaper_mist, R.drawable.weather_mist),
    Rain(7, "Rainy", R.drawable.weather_wallpaper_rain, R.drawable.weather_rain),
    Sleet(8, "Sleet", R.drawable.weather_wallpaper_sleet, R.drawable.weather_sleet),
    Snow(9, "Snow", R.drawable.weather_wallpaper_snow, R.drawable.weather_snow),
    Squalls(10, "Squalls", R.drawable.weather_wallpaper_squalls, R.drawable.weather_squalls),
    Sun(11, "Sunny", R.drawable.weather_wallpaper_sun, R.drawable.weather_sun),
    Thunderstorm(12, "Thunderstorm", R.drawable.weather_wallpaper_thunderstorm, R.drawable.weather_thunderstorm)
}