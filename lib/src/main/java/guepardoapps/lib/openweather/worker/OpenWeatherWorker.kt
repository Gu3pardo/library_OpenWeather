package guepardoapps.lib.openweather.worker

import androidx.work.Worker
import guepardoapps.lib.openweather.services.openweather.OpenWeatherService

class OpenWeatherWorker : Worker() {
    override fun doWork(): Result {
        // TODO Switch back to AlarmManager
        // https://guides.codepath.com/android/Starting-Background-Services#using-with-alarmmanager-for-periodic-tasks
        OpenWeatherService.instance.loadWeatherCurrent()
        OpenWeatherService.instance.loadWeatherForecast()
        return Result.SUCCESS
    }
}