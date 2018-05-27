package guepardoapps.lib.openweather.services

import android.content.Context
import android.os.Handler
import guepardoapps.lib.openweather.controller.*
import guepardoapps.lib.openweather.converter.*
import guepardoapps.lib.openweather.downloader.*
import guepardoapps.lib.openweather.enums.*
import guepardoapps.lib.openweather.extensions.*
import guepardoapps.lib.openweather.models.*
import guepardoapps.lib.openweather.utils.Logger
import android.app.WallpaperManager
import java.io.IOException
import java.util.*

class OpenWeatherService(
        private val context: Context,
        private var notificationEnabled: Boolean = false,
        private var receiverActivity: Class<*>? = null,
        private var wallpaperEnabled: Boolean = false,
        private var reloadEnabled: Boolean = false,
        private var reloadTimeout: Long = 15 * 60 * 1000) : IOpenWeatherService {

    private val tag: String = OpenWeatherService::class.java.canonicalName

    private val currentWeatherId: Int = 260520181
    private val forecastWeatherId: Int = 260520182

    private val minTimeoutMs: Long = 5 * 60 * 1000
    private val maxTimeoutMs: Long = 24 * 60 * 60 * 1000

    private var converter: JsonToWeatherConverter = JsonToWeatherConverter()
    private var downloader: Downloader = Downloader(context)
    private var networkController: NetworkController = NetworkController(context)
    private var notificationController: NotificationController = NotificationController(context)
    private var receiverController: ReceiverController = ReceiverController(context)
    private lateinit var onWeatherUpdateListener: OnWeatherUpdateListener

    private var reloadHandler: Handler = Handler()
    private var reloadRunnable: Runnable = Runnable {
        loadCurrentWeather()
        loadForecastWeather()
        restartHandler()
    }

    private var currentWeather: IWeatherCurrent? = null
    private var forecastWeather: IWeatherForecast? = null

    init {
        downloader.setOnDownloadListener(object : OnDownloadListener {
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
                        onWeatherUpdateListener.onCurrentWeather(null, false)
                        onWeatherUpdateListener.onForecastWeather(null, false)
                    }
                }
            }
        })
    }

    override fun setCity(city: String) {
        downloader.city = city
    }

    override fun getCity(): String? {
        return downloader.city
    }

    override fun setApiKey(apiKey: String) {
        downloader.apiKey = apiKey
    }

    override fun getApiKey(): String? {
        return downloader.apiKey
    }

    override fun setNotificationEnabled(notificationEnabled: Boolean) {
        this.notificationEnabled = notificationEnabled
        if (this.notificationEnabled) {
            displayNotification()
        } else {
            notificationController.close()
        }
    }

    override fun getNotificationEnabled(): Boolean {
        return notificationEnabled
    }

    override fun setReceiverActivity(receiverActivity: Class<*>?) {
        this.receiverActivity = receiverActivity
    }

    override fun getReceiverActivity(): Class<*>? {
        return receiverActivity
    }

    override fun setWallpaperEnabled(wallpaperEnabled: Boolean) {
        this.wallpaperEnabled = wallpaperEnabled
        if (this.wallpaperEnabled) {
            changeWallpaper()
        }
    }

    override fun getWallpaperEnabled(): Boolean {
        return wallpaperEnabled
    }

    override fun setReloadEnabled(reloadEnabled: Boolean) {
        this.reloadEnabled = reloadEnabled
        restartHandler()
    }

    override fun getReloadEnabled(): Boolean {
        return reloadEnabled
    }

    override fun setReloadTimeout(reloadTimeout: Long) {
        when {
            reloadTimeout < minTimeoutMs -> this.reloadTimeout = minTimeoutMs
            reloadTimeout > maxTimeoutMs -> this.reloadTimeout = maxTimeoutMs
            else -> this.reloadTimeout = reloadTimeout
        }
        restartHandler()
    }

    override fun getReloadTimeout(): Long {
        return reloadTimeout
    }

    override fun setOnWeatherUpdateListener(onWeatherUpdateListener: OnWeatherUpdateListener) {
        this.onWeatherUpdateListener = onWeatherUpdateListener
    }

    override fun loadCurrentWeather() {
        val result = downloader.currentWeather()
        if (result != DownloadResult.Performing) {
            Logger.instance.error(tag, "Failure in loadCurrentWeather: $result")
            onWeatherUpdateListener.onCurrentWeather(null, false)
        }
        restartHandler()
    }

    override fun loadForecastWeather() {
        val result = downloader.forecastWeather()
        if (result != DownloadResult.Performing) {
            Logger.instance.error(tag, "Failure in loadForecastWeather: $result")
            onWeatherUpdateListener.onForecastWeather(null, false)
        }
        restartHandler()
    }

    override fun searchForecast(forecast: IWeatherForecast, searchValue: String): IWeatherForecast {
        var foundEntries: Array<IWeatherForecastPart> = arrayOf()

        for (forecastPart in forecast.getList()) {
            when (searchValue) {
                "Today", "Heute", "Hoy", "Inru" -> {
                    val todayCalendar = Calendar.getInstance()
                    val dateTime = forecastPart.getDateTime()
                    if (dateTime.get(Calendar.DAY_OF_MONTH) == todayCalendar.get(Calendar.DAY_OF_MONTH)
                            && dateTime.get(Calendar.MONTH) == todayCalendar.get(Calendar.MONTH)
                            && dateTime.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR)) {
                        foundEntries = foundEntries.plus(forecastPart)
                    }
                }
                "Tomorrow", "Morgen", "Manana", "Nalai" -> {
                    val tomorrowCalendar = Calendar.getInstance()
                    tomorrowCalendar.add(Calendar.DAY_OF_MONTH, 1)
                    val dateTime = forecastPart.getDateTime()
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

        return WeatherForecast(forecast.getCity(), foundEntries)
    }

    override fun dispose() {
        receiverController.dispose()
        reloadHandler.removeCallbacks(reloadRunnable)
    }

    private fun restartHandler() {
        reloadHandler.removeCallbacks(reloadRunnable)
        if (reloadEnabled && networkController.networkAvailable()) {
            reloadHandler.postDelayed(reloadRunnable, reloadTimeout)
        }
    }

    private fun handleCurrentWeatherUpdate(success: Boolean, jsonString: String) {
        if (!success) {
            onWeatherUpdateListener.onCurrentWeather(null, false)
        } else {
            val convertedCurrentWeather = converter.convertToWeatherCurrent(jsonString)
            if (convertedCurrentWeather == null) {
                onWeatherUpdateListener.onCurrentWeather(null, false)
            } else {
                currentWeather = convertedCurrentWeather
                onWeatherUpdateListener.onCurrentWeather(currentWeather, true)
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
            onWeatherUpdateListener.onForecastWeather(null, false)
        } else {
            val convertedForecastWeather = converter.convertToWeatherForecast(jsonString)
            if (convertedForecastWeather == null) {
                onWeatherUpdateListener.onForecastWeather(null, false)
            } else {
                forecastWeather = convertedForecastWeather
                onWeatherUpdateListener.onForecastWeather(forecastWeather, true)
                if (notificationEnabled) {
                    displayNotification()
                }
            }
        }
    }

    private fun displayNotification() {
        if (currentWeather != null) {
            val currentWeatherNotificationContent = NotificationContent(
                    currentWeatherId,
                    currentWeather!!.getDescription(),
                    currentWeather!!.getDescription(),
                    currentWeather!!.getWeatherCondition().iconId,
                    currentWeather!!.getWeatherCondition().wallpaperId,
                    receiverActivity!!)
            notificationController.create(currentWeatherNotificationContent)
        }

        if (forecastWeather != null) {
            val forecastWeatherNotificationContent = NotificationContent(
                    forecastWeatherId,
                    forecastWeather!!.getMostWeatherCondition().description,
                    forecastWeather!!.getMostWeatherCondition().description,
                    forecastWeather!!.getMostWeatherCondition().iconId,
                    forecastWeather!!.getMostWeatherCondition().wallpaperId,
                    receiverActivity!!)
            notificationController.create(forecastWeatherNotificationContent)
        }
    }

    private fun changeWallpaper() {
        if (currentWeather != null) {
            try {
                val wallpaperManager = WallpaperManager.getInstance(context.applicationContext)
                wallpaperManager.setResource(currentWeather!!.getWeatherCondition().wallpaperId)
            } catch (exception: IOException) {
                Logger.instance.error(tag, exception)
            }
        }
    }
}