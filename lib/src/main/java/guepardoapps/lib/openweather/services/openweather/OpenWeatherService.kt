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
import guepardoapps.lib.openweather.services.api.OnApiServiceListener
import guepardoapps.lib.openweather.tasks.ApiRestCallTask
import guepardoapps.timext.kotlin.extensions.days
import guepardoapps.timext.kotlin.extensions.minutes

class OpenWeatherService private constructor() : IOpenWeatherService {
    private val tag: String = OpenWeatherService::class.java.simpleName

    private val geoCodeForCityUrl: String = "http://www.datasciencetoolkit.org/maps/api/geocode/json?address=%s"
    private val currentWeatherUrl: String = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&APPID=%s"
    private val forecastWeatherUrl: String = "http://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric&APPID=%s"
    private val uvIndexUrl: String = "http://api.openweathermap.org/data/2.5/uvi?lat=%.2f&lon=%.2f&APPID=%s"

    private val currentWeatherNotificationId: Int = 260520181
    private val forecastWeatherNotificationId: Int = 260520182
    private val uvIndexNotificationId: Int = 260520183

    private val minTimeoutMs: Long = 15.minutes.inMilliSeconds.value.toLong()
    private val maxTimeoutMs: Long = 1.days.inMilliSeconds.value.toLong()

    private var mayLoad: Boolean = false

    private var converter: JsonToWeatherConverter = JsonToWeatherConverter()

    private lateinit var context: Context
    private lateinit var networkController: NetworkController
    private lateinit var notificationController: NotificationController
    private lateinit var sharedPreferenceController: SharedPreferenceController

    var city: City? = null
        get() = Gson().fromJson<City>(sharedPreferenceController.load(Constants.SharedPref.KeyCity, Gson().toJson(field)), City::class.java)
        private set(value) {
            field = value ?: City()
            sharedPreferenceController.save(Constants.SharedPref.KeyCity, Gson().toJson(field))
            cityPublishSubject.onNext(RxOptional(value))
        }
    override val cityPublishSubject = PublishSubject.create<RxOptional<City>>()!!

    var weatherCurrent: WeatherCurrent? = null
        private set(value) {
            field = value
            weatherCurrentPublishSubject.onNext(RxOptional(value))
        }
    override val weatherCurrentPublishSubject = PublishSubject.create<RxOptional<WeatherCurrent>>()!!

    var weatherForecast: WeatherForecast? = null
        private set(value) {
            field = value
            weatherForecastPublishSubject.onNext(RxOptional(value))
        }
    override val weatherForecastPublishSubject = PublishSubject.create<RxOptional<WeatherForecast>>()!!

    var uvIndex: UvIndex? = null
        private set(value) {
            field = value
            uvIndexPublishSubject.onNext(RxOptional(value))
        }
    override val uvIndexPublishSubject = PublishSubject.create<RxOptional<UvIndex>>()!!

    private val onApiServiceListener = object : OnApiServiceListener {
        override fun onFinished(downloadType: DownloadType, jsonString: String, success: Boolean) {
            Logger.instance.verbose(tag, "Received onFinished")
            when (downloadType) {
                DownloadType.CityData -> {
                    handleCityDataUpdate(success, jsonString)
                }
                DownloadType.CityImage -> {
                    // Nothing to do here
                }
                DownloadType.CurrentWeather -> {
                    handleCurrentWeatherUpdate(success, jsonString)
                }
                DownloadType.ForecastWeather -> {
                    handleForecastWeatherUpdate(success, jsonString)
                }
                DownloadType.UvIndex -> {
                    handleUvIndexUpdate(success, jsonString)
                }
                DownloadType.Null -> {
                    Logger.instance.error(tag, "Received download update with downloadType Null and jsonString: $jsonString")
                    city = null
                    weatherCurrent = null
                    weatherForecast = null
                    uvIndex = null
                }
            }
        }
    }

    override lateinit var apiKey: String

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

    override fun initialize(context: Context, cityName: String) {
        this.context = context

        this.networkController = NetworkController(this.context)
        this.notificationController = NotificationController(this.context)
        this.sharedPreferenceController = SharedPreferenceController(this.context)

        Logger.instance.initialize(context)

        if (!this.sharedPreferenceController.exists(Constants.SharedPref.KeyCity)) {
            Logger.instance.info(tag, "Initial save of default city")
            val city = City()
            city.name = cityName
            this.sharedPreferenceController.save(Constants.SharedPref.KeyCity, Gson().toJson(city))
        }

        loadCityData(cityName)
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

    override fun loadCityData(cityName: String): Boolean {
        doApiRestCall(DownloadType.CityData, String.format(geoCodeForCityUrl, cityName))
        return true
    }

    override fun loadWeatherCurrent(): Boolean {
        if (!mayLoad || city == null || city!!.isDefault()) {
            return false
        }

        doApiRestCall(DownloadType.CurrentWeather, String.format(currentWeatherUrl, city?.name, apiKey))

        cancelReload()
        if (reloadEnabled) {
            scheduleReload()
        }

        return true
    }

    override fun loadWeatherForecast(): Boolean {
        if (!mayLoad || city == null || city!!.isDefault()) {
            return false
        }

        doApiRestCall(DownloadType.ForecastWeather, String.format(forecastWeatherUrl, city?.name, apiKey))

        cancelReload()
        if (reloadEnabled) {
            scheduleReload()
        }
        return true
    }

    override fun loadUvIndex(): Boolean {
        if (!mayLoad || city == null || city!!.isDefault()) {
            return false
        }

        doApiRestCall(DownloadType.UvIndex, String.format(uvIndexUrl, city?.coordinates?.lat, city?.coordinates?.lon, apiKey))

        cancelReload()
        if (reloadEnabled) {
            scheduleReload()
        }
        return true
    }

    override fun searchForecast(searchValue: String): WeatherForecast {
        val foundEntries = this.weatherForecast!!.list.filter { x ->
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
        weatherForecast.city = this.weatherForecast!!.city
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
                city!!.id = weatherCurrent!!.city.id
                city!!.population = weatherCurrent!!.city.population
            }
        }
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
                city!!.id = weatherForecast!!.city.id
                city!!.population = weatherForecast!!.city.population
            }
        }
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
    }

    private fun handleCityDataUpdate(success: Boolean, jsonString: String) {
        if (!success) {
            city = null
        } else {
            val convertedCity2 = converter.convertToCity(jsonString)
            if (convertedCity2 == null) {
                city = null
            } else {
                val newCoordinates = Coordinates()
                newCoordinates.lat = convertedCity2.geometry.location.lat
                newCoordinates.lon = convertedCity2.geometry.location.lng

                val newCity = City()
                newCity.id = city!!.id
                newCity.name = convertedCity2.addressComponents.first().shortName
                newCity.country = convertedCity2.addressComponents.second().shortName
                newCity.population = city!!.population
                newCity.coordinates = newCoordinates

                city = newCity
            }
        }
    }

    private fun displayNotification() {
        if (weatherCurrent != null) {
            val currentWeatherNotificationContent = NotificationContent(
                    currentWeatherNotificationId,
                    "Current Weather: " + weatherCurrent!!.description,
                    weatherCurrent!!.temperature.doubleFormat(1) + "${Constants.String.DegreeSign}C, "
                            + "(" + weatherCurrent!!.temperatureMin.doubleFormat(1) + "${Constants.String.DegreeSign}C - "
                            + weatherCurrent!!.temperatureMax.doubleFormat(1) + "${Constants.String.DegreeSign}C), "
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
                    weatherForecast!!.getMinTemperature().doubleFormat(1) + "${Constants.String.DegreeSign}C - "
                            + weatherForecast!!.getMaxTemperature().doubleFormat(1) + "${Constants.String.DegreeSign}C, "
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

    private fun doApiRestCall(downloadType: DownloadType, url: String) {
        val task = ApiRestCallTask()
        task.onApiServiceListener = onApiServiceListener
        task.downloadType = downloadType
        task.execute(url)
    }
}