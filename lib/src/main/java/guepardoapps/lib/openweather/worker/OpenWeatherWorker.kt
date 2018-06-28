package guepardoapps.lib.openweather.worker

import androidx.work.Worker
import guepardoapps.lib.openweather.services.openweather.OpenWeatherService

class OpenWeatherWorker : Worker() {
    override fun doWork(): Result {
        OpenWeatherService.instance.loadCurrentWeather()
        OpenWeatherService.instance.loadForecastWeather()
        return Result.SUCCESS
    }
}