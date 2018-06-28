package guepardoapps.lib.openweather.services.openweather

import guepardoapps.lib.openweather.models.WeatherCurrent
import guepardoapps.lib.openweather.models.WeatherForecast

interface OnWeatherServiceListener {
    fun onCurrentWeather(currentWeather: WeatherCurrent?, success: Boolean)
    fun onForecastWeather(forecastWeather: WeatherForecast?, success: Boolean)
}