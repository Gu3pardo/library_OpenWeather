package guepardoapps.lib.openweather.services

import guepardoapps.lib.openweather.models.IWeatherCurrent
import guepardoapps.lib.openweather.models.IWeatherForecast

interface OnWeatherUpdateListener {
    fun onCurrentWeather(currentWeather: IWeatherCurrent?, success: Boolean)

    fun onForecastWeather(forecastWeather: IWeatherForecast?, success: Boolean)
}