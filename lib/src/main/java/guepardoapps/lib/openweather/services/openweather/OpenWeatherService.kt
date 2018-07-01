package guepardoapps.lib.openweather.services.openweather

import android.annotation.SuppressLint
import android.content.Context
import guepardoapps.lib.openweather.controller.*
import guepardoapps.lib.openweather.converter.*
import guepardoapps.lib.openweather.enums.*
import guepardoapps.lib.openweather.extensions.*
import guepardoapps.lib.openweather.models.*
import guepardoapps.lib.openweather.utils.Logger
import android.app.WallpaperManager
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import guepardoapps.lib.openweather.services.api.ApiService
import guepardoapps.lib.openweather.services.api.OnApiServiceListener
import guepardoapps.lib.openweather.worker.OpenWeatherWorker
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

class OpenWeatherService private constructor() : IOpenWeatherService {
    private val tag: String = OpenWeatherService::class.java.simpleName

    private val currentWeatherNotificationId: Int = 260520181
    private val forecastWeatherNotificationId: Int = 260520182

    private val minTimeoutMs: Long = 15 * 60 * 1000
    private val maxTimeoutMs: Long = 24 * 60 * 60 * 1000

    private var converter: JsonToWeatherConverter = JsonToWeatherConverter()
    private var apiService: ApiService = ApiService()

    private lateinit var context: Context
    private lateinit var networkController: NetworkController
    private lateinit var notificationController: NotificationController

    private lateinit var reloadWork: PeriodicWorkRequest
    private var reloadWorkId: UUID = UUID.randomUUID()

    private var currentWeather: WeatherCurrent? = null
    private var forecastWeather: WeatherForecast? = null

    override var city: String
        get() = apiService.city
        set(value) {
            apiService.city = value
        }
    override var apiKey: String
        get() = apiService.apiKey
        set(value) {
            apiService.apiKey = value
        }

    override var notificationEnabled: Boolean = true
        set(value) {
            field = value
            if (field) {
                displayNotification()
            } else {
                notificationController.close(currentWeatherNotificationId)
                notificationController.close(forecastWeatherNotificationId)
            }
        }
    override var receiverActivity: Class<*>? = null
        set(value) {
            field = value
            if (field != null && this.notificationEnabled) {
                displayNotification()
            } else {
                notificationController.close(currentWeatherNotificationId)
                notificationController.close(forecastWeatherNotificationId)
            }
        }

    override var wallpaperEnabled: Boolean = false
        set(value) {
            field = value
            if (field) {
                changeWallpaper()
            }
        }

    override var reloadEnabled: Boolean = true
        set(value) {
            field = value
            restartHandler()
        }
    override var reloadTimeout: Long = this.minTimeoutMs
        set(value) {
            field = when {
                value < minTimeoutMs -> minTimeoutMs
                value > maxTimeoutMs -> maxTimeoutMs
                else -> value
            }
            restartHandler()
        }

    override var onWeatherServiceListener: OnWeatherServiceListener? = null

    init {
    }

    private object Holder {
        @SuppressLint("StaticFieldLeak")
        val instance: OpenWeatherService = OpenWeatherService()
    }

    companion object {
        val instance: OpenWeatherService by lazy { Holder.instance }
    }

    override fun initialize(context: Context) {
        this.context = context

        this.networkController = NetworkController(this.context)
        this.notificationController = NotificationController(this.context)

        Logger.instance.initialize(context)

        apiService.setOnApiServiceListener(object : OnApiServiceListener {
            override fun onFinished(downloadType: DownloadType, jsonString: String, success: Boolean) {
                Logger.instance.verbose(tag, "Received onDownloadListener onFinished")

                when (downloadType) {
                    DownloadType.Current -> {
                        handleCurrentWeatherUpdate(success, jsonString)
                    }
                    DownloadType.Forecast -> {
                        handleForecastWeatherUpdate(success, jsonString)
                    }
                    DownloadType.Null -> {
                        Logger.instance.error(tag, "Received download update with downloadType Null and jsonString: $jsonString")
                        onWeatherServiceListener?.onCurrentWeather(null, false)
                        onWeatherServiceListener?.onForecastWeather(null, false)
                    }
                }
            }
        })
    }

    override fun loadCurrentWeather() {
        val result = apiService.currentWeather()
        if (result != DownloadResult.Performing) {
            Logger.instance.error(tag, "Failure in loadCurrentWeather: $result")
            onWeatherServiceListener?.onCurrentWeather(null, false)
        }
    }

    override fun loadForecastWeather() {
        val result = apiService.forecastWeather()
        if (result != DownloadResult.Performing) {
            Logger.instance.error(tag, "Failure in loadForecastWeather: $result")
            onWeatherServiceListener?.onForecastWeather(null, false)
        }
    }

    override fun searchForecast(forecast: WeatherForecast, searchValue: String): WeatherForecast {
        var foundEntries: Array<WeatherForecastPart> = arrayOf()

        for (forecastPart in forecast.list) {
            when (searchValue) {
                "Today", "Heute", "Hoy", "Inru" -> {
                    val todayCalendar = Calendar.getInstance()
                    val dateTime = forecastPart.dateTime
                    if (dateTime.get(Calendar.DAY_OF_MONTH) == todayCalendar.get(Calendar.DAY_OF_MONTH)
                            && dateTime.get(Calendar.MONTH) == todayCalendar.get(Calendar.MONTH)
                            && dateTime.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR)) {
                        foundEntries = foundEntries.plus(forecastPart)
                    }
                }
                "Tomorrow", "Morgen", "Manana", "Nalai" -> {
                    val tomorrowCalendar = Calendar.getInstance()
                    tomorrowCalendar.add(Calendar.DAY_OF_MONTH, 1)
                    val dateTime = forecastPart.dateTime
                    if (dateTime.get(Calendar.DAY_OF_MONTH) == tomorrowCalendar.get(Calendar.DAY_OF_MONTH)
                            && dateTime.get(Calendar.MONTH) == tomorrowCalendar.get(Calendar.MONTH)
                            && dateTime.get(Calendar.YEAR) == tomorrowCalendar.get(Calendar.YEAR)) {
                        foundEntries = foundEntries.plus(forecastPart)
                    }
                }
                else -> {
                    if (forecastPart.toString().contains(searchValue)) {
                        foundEntries = foundEntries.plus(forecastPart)
                    }
                }
            }
        }

        val weatherForecast = WeatherForecast()
        weatherForecast.city = forecast.city
        weatherForecast.list = foundEntries
        return weatherForecast
    }

    override fun dispose() {
        WorkManager.getInstance().cancelWorkById(this.reloadWorkId)
    }

    private fun restartHandler() {
        WorkManager.getInstance().cancelWorkById(this.reloadWorkId)
        if (reloadEnabled && networkController.networkAvailable()) {
            this.reloadWork = PeriodicWorkRequestBuilder<OpenWeatherWorker>(this.reloadTimeout, TimeUnit.MILLISECONDS).build()
            this.reloadWorkId = this.reloadWork.id
            WorkManager.getInstance().enqueue(this.reloadWork)
        }
    }

    private fun handleCurrentWeatherUpdate(success: Boolean, jsonString: String) {
        if (!success) {
            onWeatherServiceListener?.onCurrentWeather(null, false)
        } else {
            val convertedCurrentWeather = converter.convertToWeatherCurrent(jsonString)
            if (convertedCurrentWeather == null) {
                onWeatherServiceListener?.onCurrentWeather(null, false)
            } else {
                currentWeather = convertedCurrentWeather
                onWeatherServiceListener?.onCurrentWeather(currentWeather, true)
                if (wallpaperEnabled) {
                    changeWallpaper()
                }
                if (notificationEnabled) {
                    displayNotification()
                }
            }
        }
    }

    private fun handleForecastWeatherUpdate(success: Boolean, jsonString: String) {
        if (!success) {
            onWeatherServiceListener?.onForecastWeather(null, false)
        } else {
            val convertedForecastWeather = converter.convertToWeatherForecast(jsonString)
            if (convertedForecastWeather == null) {
                onWeatherServiceListener?.onForecastWeather(null, false)
            } else {
                forecastWeather = convertedForecastWeather
                onWeatherServiceListener?.onForecastWeather(forecastWeather, true)
                if (notificationEnabled) {
                    displayNotification()
                }
            }
        }
    }

    private fun displayNotification() {
        if (currentWeather != null) {
            val currentWeatherNotificationContent = NotificationContent(
                    currentWeatherNotificationId,
                    "Current Weather: " + currentWeather!!.description,
                    currentWeather!!.temperature.doubleFormat(1) + "${0x00B0.toChar()}C, "
                            + currentWeather!!.pressure.doubleFormat(1) + "mBar, "
                            + currentWeather!!.humidity.doubleFormat(1) + "%",
                    currentWeather!!.weatherCondition.iconId,
                    currentWeather!!.weatherCondition.wallpaperId,
                    receiverActivity!!,
                    true)
            notificationController.create(currentWeatherNotificationContent)
        }

        if (forecastWeather != null) {
            val forecastWeatherNotificationContent = NotificationContent(
                    forecastWeatherNotificationId,
                    "Forecast: " + forecastWeather!!.getMostWeatherCondition().description,
                    forecastWeather!!.getMinTemperature().doubleFormat(1) + "${0x00B0.toChar()}C - "
                            + forecastWeather!!.getMaxTemperature().doubleFormat(1) + "${0x00B0.toChar()}C, "
                            + forecastWeather!!.getMinPressure().doubleFormat(1) + "mBar - "
                            + forecastWeather!!.getMaxPressure().doubleFormat(1) + "mBar, "
                            + forecastWeather!!.getMinHumidity().doubleFormat(1) + "% - "
                            + forecastWeather!!.getMaxHumidity().doubleFormat(1) + "%",
                    forecastWeather!!.getMostWeatherCondition().iconId,
                    forecastWeather!!.getMostWeatherCondition().wallpaperId,
                    receiverActivity!!,
                    true)
            notificationController.create(forecastWeatherNotificationContent)
        }
    }

    private fun changeWallpaper() {
        if (currentWeather != null) {
            try {
                val wallpaperManager = WallpaperManager.getInstance(context.applicationContext)
                wallpaperManager.setResource(currentWeather!!.weatherCondition.wallpaperId)
            } catch (exception: IOException) {
                Logger.instance.error(tag, exception)
            }
        }
    }
}