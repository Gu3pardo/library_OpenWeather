package guepardoapps.lib.openweather.services.openweather

import android.annotation.SuppressLint
import android.content.Context
import guepardoapps.lib.openweather.controller.*
import guepardoapps.lib.openweather.converter.*
import guepardoapps.lib.openweather.enums.*
import guepardoapps.lib.openweather.extensions.*
import guepardoapps.lib.openweather.logging.Logger
import guepardoapps.lib.openweather.models.*
import android.app.WallpaperManager
import guepardoapps.lib.openweather.services.api.ApiService
import guepardoapps.lib.openweather.services.api.OnApiServiceListener
import io.reactivex.subjects.PublishSubject
import java.io.IOException
import java.util.*
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import com.google.gson.Gson
import guepardoapps.lib.openweather.R
import guepardoapps.lib.openweather.common.Constants
import guepardoapps.lib.openweather.receiver.PeriodicActionReceiver

class OpenWeatherService private constructor() : IOpenWeatherService {
    private val tag: String = OpenWeatherService::class.java.simpleName

    private val currentWeatherNotificationId: Int = 260520181
    private val forecastWeatherNotificationId: Int = 260520182
    private val uvIndexNotificationId: Int = 260520183

    private val minTimeoutMs: Long = 15 * 60 * 1000
    private val maxTimeoutMs: Long = 24 * 60 * 60 * 1000

    private var mayLoad: Boolean = false

    private var converter: JsonToWeatherConverter = JsonToWeatherConverter()
    private var apiService: ApiService = ApiService()

    private lateinit var context: Context
    private lateinit var networkController: NetworkController
    private lateinit var notificationController: NotificationController
    private lateinit var sharedPreferenceController: SharedPreferenceController

    var weatherCurrent: WeatherCurrent? = null
        private set(value) {
            field = value
            if (value != null) {
                city = value.city
            }
        }
    override val weatherCurrentPublishSubject = PublishSubject.create<RxOptional<WeatherCurrent>>()!!

    var weatherForecast: WeatherForecast? = null
        private set(value) {
            field = value
            if (value != null) {
                city = value.city
            }
        }
    override val weatherForecastPublishSubject = PublishSubject.create<RxOptional<WeatherForecast>>()!!

    var uvIndex: UvIndex? = null
        private set(value) {
            field = value
        }
    override val uvIndexPublishSubject = PublishSubject.create<RxOptional<UvIndex>>()!!

    override var city: City
        get() {
            val cityString = this.sharedPreferenceController.load(Constants.sharedPrefKeyCity, "")
            if (cityString.isBlank()) {
                return City()
            }
            Logger.instance.debug(tag,"CityStringGet: $cityString")
            return Gson().fromJson<City>(cityString, City::class.java)
        }
        set(value) {
            apiService.city = value
            Logger.instance.debug(tag,"CityStringSet: ${Gson().toJson(value)}")
            this.sharedPreferenceController.save(Constants.sharedPrefKeyCity, Gson().toJson(value).toString())
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
                notificationController.close(uvIndexNotificationId)
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
                notificationController.close(uvIndexNotificationId)
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
            cancelReload()
            if (field) {
                scheduleReload()
            }
        }
    override var reloadTimeout: Long = minTimeoutMs
        set(value) {
            field = when {
                value < minTimeoutMs -> minTimeoutMs
                value > maxTimeoutMs -> maxTimeoutMs
                else -> value
            }
            cancelReload()
            if (reloadEnabled) {
                scheduleReload()
            }
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
        this.sharedPreferenceController = SharedPreferenceController(this.context)

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
                    DownloadType.UvIndex -> {
                        handleUvIndexUpdate(success, jsonString)
                    }
                    DownloadType.Null -> {
                        Logger.instance.error(tag, "Received download update with downloadType Null and jsonString: $jsonString")

                        weatherCurrent = null
                        weatherForecast = null
                        uvIndex = null

                        weatherCurrentPublishSubject.onNext(RxOptional(weatherCurrent))
                        weatherForecastPublishSubject.onNext(RxOptional(weatherForecast))
                        uvIndexPublishSubject.onNext(RxOptional(uvIndex))
                    }
                }
            }
        })
    }

    override fun start() {
        mayLoad = true
        loadWeatherCurrent()
        loadWeatherForecast()
        loadUvIndex()
        scheduleReload()
    }

    override fun dispose() {
        mayLoad = false
        cancelReload()
    }

    override fun loadWeatherCurrent() {
        if (!mayLoad) {
            return
        }

        val result = apiService.currentWeather()
        if (result != DownloadResult.Performing) {
            Logger.instance.error(tag, "Failure in loadCurrentWeather: $result")
            weatherCurrent = null
            weatherCurrentPublishSubject.onNext(RxOptional(weatherCurrent))
        }

        cancelReload()
        if (reloadEnabled) {
            scheduleReload()
        }
    }

    override fun loadWeatherForecast() {
        if (!mayLoad) {
            return
        }

        val result = apiService.forecastWeather()
        if (result != DownloadResult.Performing) {
            Logger.instance.error(tag, "Failure in loadForecastWeather: $result")
            weatherForecast = null
            weatherForecastPublishSubject.onNext(RxOptional(weatherForecast))
        }

        cancelReload()
        if (reloadEnabled) {
            scheduleReload()
        }
    }

    override fun loadUvIndex() {
        if (!mayLoad) {
            return
        }

        val result = apiService.uvIndex()
        if (result != DownloadResult.Performing) {
            Logger.instance.error(tag, "Failure in uvIndex: $result")
            uvIndex = null
            uvIndexPublishSubject.onNext(RxOptional(uvIndex))
        }

        cancelReload()
        if (reloadEnabled) {
            scheduleReload()
        }
    }

    override fun searchForecast(forecast: WeatherForecast, searchValue: String): WeatherForecast {
        val foundEntries = forecast.list.filter { x ->
            when (searchValue) {
                "Today", "Heute", "Hoy", "Inru" -> {
                    val todayCalendar = Calendar.getInstance()
                    x.dateTime.get(Calendar.DAY_OF_MONTH) == todayCalendar.get(Calendar.DAY_OF_MONTH)
                            && x.dateTime.get(Calendar.MONTH) == todayCalendar.get(Calendar.MONTH)
                            && x.dateTime.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR)
                }
                "Tomorrow", "Morgen", "Manana", "Nalai" -> {
                    val tomorrowCalendar = Calendar.getInstance()
                    tomorrowCalendar.add(Calendar.DAY_OF_MONTH, 1)
                    x.dateTime.get(Calendar.DAY_OF_MONTH) == tomorrowCalendar.get(Calendar.DAY_OF_MONTH)
                            && x.dateTime.get(Calendar.MONTH) == tomorrowCalendar.get(Calendar.MONTH)
                            && x.dateTime.get(Calendar.YEAR) == tomorrowCalendar.get(Calendar.YEAR)
                }
                else -> {
                    x.toString().contains(searchValue)
                }
            }
        }

        val weatherForecast = WeatherForecast()
        weatherForecast.city = forecast.city
        weatherForecast.list = foundEntries
        return weatherForecast
    }

    private fun scheduleReload() {
        if (!mayLoad) {
            return
        }

        if (reloadEnabled && networkController.isInternetConnected().second) {
            val intent = Intent(context.applicationContext, PeriodicActionReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, PeriodicActionReceiver.requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), reloadTimeout, pendingIntent)
        }
    }

    private fun cancelReload() {
        val intent = Intent(context.applicationContext, PeriodicActionReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, PeriodicActionReceiver.requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarm.cancel(pendingIntent)
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

        weatherForecastPublishSubject.onNext(RxOptional(weatherForecast))
    }

    private fun handleUvIndexUpdate(success: Boolean, jsonString: String) {
        if (!success) {
            uvIndex = null
        } else {
            val convertedUvIndex = converter.convertToUvIndex(jsonString)
            if (convertedUvIndex == null) {
                uvIndex = null
            } else {
                uvIndex = convertedUvIndex
                if (notificationEnabled) {
                    displayNotification()
                }
            }
        }

        uvIndexPublishSubject.onNext(RxOptional(uvIndex))
    }

    private fun displayNotification() {
        if (weatherCurrent != null) {
            val currentWeatherNotificationContent = NotificationContent(
                    currentWeatherNotificationId,
                    "Current Weather: " + weatherCurrent!!.description,
                    weatherCurrent!!.temperature.doubleFormat(1) + "${0x00B0.toChar()}C, "
                            + "(" + weatherCurrent!!.temperatureMin.doubleFormat(1) + "${0x00B0.toChar()}C - "
                            + "(" + weatherCurrent!!.temperatureMax.doubleFormat(1) + "${0x00B0.toChar()}C), "
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

        if (uvIndex != null) {
            val forecastWeatherNotificationContent = NotificationContent(
                    uvIndexNotificationId,
                    "UvIndex",
                    "Value is ${uvIndex!!.value}",
                    R.drawable.uv_index,
                    R.drawable.uv_index,
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