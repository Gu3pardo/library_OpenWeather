package guepardoapps.lib.openweather.services.openweather

import guepardoapps.lib.openweather.models.IWeatherCurrent
import guepardoapps.lib.openweather.models.IWeatherForecast

interface OnWeatherServiceListener {
    fun onCurrentWeather(currentWeather: IWeatherCurrent?, success: Boolean)
    fun onForecastWeather(forecastWeather: IWeatherForecast?, success: Boolean)
}