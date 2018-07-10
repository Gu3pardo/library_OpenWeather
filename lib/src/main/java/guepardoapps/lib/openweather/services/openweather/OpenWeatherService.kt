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
import io.reactivex.subjects.PublishSubject
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

class OpenWeatherService private constructor() : IOpenWeatherService {
    private val tag: String = OpenWeatherService::class.java.simpleName

    private var weatherCurrentCall = 0
    private var weatherForecastCall = 0

    private val currentWeatherNotificationId: Int = 260520181
    private val forecastWeatherNotificationId: Int = 260520182

    private val minTimeoutMs: Long = 15 * 60 * 1000
    private val maxTimeoutMs: Long = 24 * 60 * 60 * 1000

    private var mayLoad: Boolean = false

    private var converter: JsonToWeatherConverter = JsonToWeatherConverter()
    private var apiService: ApiService = ApiService()

    private lateinit var context: Context
    private lateinit var networkController: NetworkController
    private lateinit var notificationController: NotificationController

    private var reloadWork: PeriodicWorkRequest? = null

    var weatherCurrent: WeatherCurrent? = null
        private set(value) {
            field = value
        }
    override val weatherCurrentPublishSubject = PublishSubject.create<RxOptional<WeatherCurrent>>()!!

    var weatherForecast: WeatherForecast? = null
        private set(value) {
            field = value
        }
    override val weatherForecastPublishSubject = PublishSubject.create<RxOptional<WeatherForecast>>()!!

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
            if (field != null && notificationEnabled) {
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
    override var reloadTimeout: Long = minTimeoutMs
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

                        weatherCurrent = null
                        weatherForecast = null

                        weatherCurrentPublishSubject.onNext(RxOptional(weatherCurrent))
                        weatherForecastPublishSubject.onNext(RxOptional(weatherForecast))
                    }
                }
            }
        })
    }

    override fun start() {
        mayLoad = true
        loadWeatherCurrent()
        loadWeatherForecast()
    }

    override fun dispose() {
        if (reloadWork != null) {
            WorkManager.getInstance()?.cancelWorkById(reloadWork!!.id)
        }
    }

    override fun loadWeatherCurrent() {
        weatherCurrentCall++
        Logger.instance.info(tag, "weatherCurrentCall: $weatherCurrentCall")

        if (!mayLoad) {
            return
        }

        val result = apiService.currentWeather()
        if (result != DownloadResult.Performing) {
            Logger.instance.error(tag, "Failure in loadCurrentWeather: $result")
            onWeatherServiceListener?.onCurrentWeather(null, false)
            weatherCurrent = null
            weatherCurrentPublishSubject.onNext(RxOptional(weatherCurrent))
        }
    }

    override fun loadWeatherForecast() {
        weatherForecastCall++
        Logger.instance.info(tag, "weatherForecastCall: $weatherForecastCall")

        if (!mayLoad) {
            return
        }

        val result = apiService.forecastWeather()
        if (result != DownloadResult.Performing) {
            Logger.instance.error(tag, "Failure in loadForecastWeather: $result")
            onWeatherServiceListener?.onForecastWeather(null, false)
            weatherForecast = null
            weatherForecastPublishSubject.onNext(RxOptional(weatherForecast))
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

    private fun restartHandler() {
        if (!mayLoad) {
            return
        }

        if (reloadWork != null) {
            WorkManager.getInstance()?.cancelWorkById(reloadWork!!.id)
        }

        if (reloadEnabled && networkController.isInternetConnected().second) {
            reloadWork = PeriodicWorkRequestBuilder<OpenWeatherWorker>(reloadTimeout, TimeUnit.MILLISECONDS).build()
            WorkManager.getInstance()?.enqueue(reloadWork)

            Logger.instance.info(tag, "restartHandler with reloadTimeout: $reloadTimeout and id ${reloadWork!!.id}")
        }
    }

    private fun handleCurrentWeatherUpdate(success: Boolean, jsonString: String) {
        if (!success) {
            weatherCurrent = null
        } else {
            val convertedCurrentWeather = converter.convertToWeatherCurrent(jsonString)
            if (convertedCurrentWeather == null) {
                weatherCurrent = null
            } else {
                weatherCurrent = convertedCurrentWeather
                if (wallpaperEnabled) {
                    changeWallpaper()
                }
                if (notificationEnabled) {
                    displayNotification()
                }
            }
        }

        onWeatherServiceListener?.onCurrentWeather(weatherCurrent, success && weatherCurrent != null)
        weatherCurrentPublishSubject.onNext(RxOptional(weatherCurrent))
    }

    private fun handleForecastWeatherUpdate(success: Boolean, jsonString: String) {
        if (!success) {
            weatherForecast = null
        } else {
            val convertedForecastWeather = converter.convertToWeatherForecast(jsonString)
            if (convertedForecastWeather == null) {
                weatherForecast = null
            } else {
                weatherForecast = convertedForecastWeather
                if (notificationEnabled) {
                    displayNotification()
                }
            }
        }

        onWeatherServiceListener?.onForecastWeather(weatherForecast, success && weatherForecast != null)
        weatherForecastPublishSubject.onNext(RxOptional(weatherForecast))
    }

    private fun displayNotification() {
        if (weatherCurrent != null) {
            val currentWeatherNotificationContent = NotificationContent(
                    currentWeatherNotificationId,
                    "Current Weather: " + weatherCurrent!!.description,
                    weatherCurrent!!.temperature.doubleFormat(1) + "${0x00B0.toChar()}C, "
                            + weatherCurrent!!.pressure.doubleFormat(1) + "mBar, "
                            + weatherCurrent!!.humidity.doubleFormat(1) + "%",
                    weatherCurrent!!.weatherCondition.iconId,
                    weatherCurrent!!.weatherCondition.wallpaperId,
                    receiverActivity!!,
                    true)
            notificationController.create(currentWeatherNotificationContent)
        }

        if (weatherForecast != null) {
            val forecastWeatherNotificationContent = NotificationContent(
                    forecastWeatherNotificationId,
                    "Forecast: " + weatherForecast!!.getMostWeatherCondition().description,
                    weatherForecast!!.getMinTemperature().doubleFormat(1) + "${0x00B0.toChar()}C - "
                            + weatherForecast!!.getMaxTemperature().doubleFormat(1) + "${0x00B0.toChar()}C, "
                            + weatherForecast!!.getMinPressure().doubleFormat(1) + "mBar - "
                            + weatherForecast!!.getMaxPressure().doubleFormat(1) + "mBar, "
                            + weatherForecast!!.getMinHumidity().doubleFormat(1) + "% - "
                            + weatherForecast!!.getMaxHumidity().doubleFormat(1) + "%",
                    weatherForecast!!.getMostWeatherCondition().iconId,
                    weatherForecast!!.getMostWeatherCondition().wallpaperId,
                    receiverActivity!!,
                    true)
            notificationController.create(forecastWeatherNotificationContent)
        }
    }

    private fun changeWallpaper() {
        if (weatherCurrent != null) {
            try {
                val wallpaperManager = WallpaperManager.getInstance(context.applicationContext)
                wallpaperManager.setResource(weatherCurrent!!.weatherCondition.wallpaperId)
            } catch (exception: IOException) {
                Logger.instance.error(tag, exception)
            }
        }
    }
}