package guepardoapps.lib.openweather.worker

import androidx.work.Worker
import guepardoapps.lib.openweather.services.openweather.OpenWeatherService

class OpenWeatherWorker : Worker() {
    override fun doWork(): Result {
        OpenWeatherService.instance.loadWeatherCurrent()
        OpenWeatherService.instance.loadWeatherForecast()
        return Result.SUCCESS
    }
}