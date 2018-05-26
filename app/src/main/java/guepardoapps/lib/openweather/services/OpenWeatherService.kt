package guepardoapps.lib.openweather.services

import android.content.Context
import android.os.Handler
import guepardoapps.lib.openweather.controller.*
import guepardoapps.lib.openweather.converter.IJsonToWeatherConverter
import guepardoapps.lib.openweather.converter.JsonToWeatherConverter
import guepardoapps.lib.openweather.downloader.Downloader
import guepardoapps.lib.openweather.downloader.IDownloader
import guepardoapps.lib.openweather.downloader.OnDownloadListener
import guepardoapps.lib.openweather.enums.DownloadResult
import guepardoapps.lib.openweather.enums.DownloadType
import guepardoapps.lib.openweather.models.IWeatherForecast
import guepardoapps.lib.openweather.utils.Logger

class OpenWeatherService(
        context: Context,
        private var notificationEnabled: Boolean = false,
        private var receiverActivity: Class<*>? = null,
        private var wallpaperEnabled: Boolean = false,
        private var reloadEnabled: Boolean = false,
        private var reloadTimeout: Long = 15 * 60 * 1000) : IOpenWeatherService {

    private val tag: String = OpenWeatherService::class.java.canonicalName

    private val minTimeoutMs: Long = 5 * 60 * 1000
    private val maxTimeoutMs: Long = 24 * 60 * 60 * 1000

    private var converter: IJsonToWeatherConverter? = null
    private var downloader: IDownloader? = null
    private var networkController: INetworkController? = null
    private var notificationController: INotificationController? = null
    private var receiverController: IReceiverController? = null
    private var onWeatherUpdateListener: OnWeatherUpdateListener? = null

    private var reloadHandler: Handler? = null
    private var reloadRunnable: Runnable = Runnable {
        loadCurrentWeather()
        loadForecastWeather()
        restartHandler()
    }

    init {
        converter = JsonToWeatherConverter()
        downloader = Downloader(context)
        networkController = NetworkController(context)
        notificationController = NotificationController(context)
        receiverController = ReceiverController(context)

        downloader?.setOnDownloadListener(object : OnDownloadListener {
            override fun onFinished(downloadType: DownloadType, jsonString: String, success: Boolean) {
                when (downloadType) {
                    DownloadType.Current -> {
                        // TODO
                    }
                    DownloadType.Forecast -> {
                        // TODO
                    }
                    DownloadType.Null -> {
                        // TODO
                    }
                }
            }
        })
    }

    override fun setCity(city: String) {
        downloader?.city = city
    }

    override fun getCity(): String? {
        return downloader?.city
    }

    override fun setApiKey(apiKey: String) {
        downloader?.apiKey = apiKey
    }

    override fun getApiKey(): String? {
        return downloader?.apiKey
    }

    override fun setNotificationEnabled(notificationEnabled: Boolean) {
        this.notificationEnabled = notificationEnabled
        if (this.notificationEnabled) {
            displayNotification()
        } else {
            notificationController?.close()
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
        val result = downloader?.currentWeather()
        if (result != DownloadResult.Performing) {
            Logger.instance.error(tag, "Failure in loadCurrentWeather: $result")
            onWeatherUpdateListener?.onCurrentWeather(null, false)
        }
        restartHandler()
    }

    override fun loadForecastWeather() {
        val result = downloader?.forecastWeather()
        if (result != DownloadResult.Performing) {
            Logger.instance.error(tag, "Failure in loadForecastWeather: $result")
            onWeatherUpdateListener?.onForecastWeather(null, false)
        }
        restartHandler()
    }

    override fun searchForecast(forecast: IWeatherForecast, searchValue: String): IWeatherForecast {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dispose() {
        receiverController?.dispose()
        reloadHandler?.removeCallbacks(reloadRunnable)
    }

    private fun restartHandler() {
        reloadHandler?.removeCallbacks(reloadRunnable)
        if (reloadEnabled && networkController!!.networkAvailable()) {
            reloadHandler?.postDelayed(reloadRunnable, reloadTimeout)
        }
    }

    private fun displayNotification() {
        // TODO
    }

    private fun changeWallpaper() {
        // TODO
    }
}