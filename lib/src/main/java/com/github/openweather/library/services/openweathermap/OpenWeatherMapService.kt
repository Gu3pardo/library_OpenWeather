package com.github.openweather.library.services.openweathermap

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Context
import android.util.Log
import com.github.guepardoapps.timext.kotlin.TimeXt
import com.github.guepardoapps.timext.kotlin.extensions.minus
import com.github.guepardoapps.timext.kotlin.extensions.minutes
import com.github.openweather.library.R
import com.github.openweather.library.controller.*
import com.github.openweather.library.converter.*
import com.github.openweather.library.enums.*
import com.github.openweather.library.extensions.*
import com.github.openweather.library.models.*
import com.github.openweather.library.services.api.OnApiServiceListener
import com.github.openweather.library.tasks.ApiRestCallTask
import com.google.gson.Gson
import io.reactivex.subjects.BehaviorSubject
import java.io.IOException
import java.util.*

class OpenWeatherMapService private constructor() : IOpenWeatherMapService {

    private val tag: String = OpenWeatherMapService::class.java.simpleName

    private val geoCodeForCityUrl: String = "http://www.datasciencetoolkit.org/maps/api/geocode/json?address=%s"

    private val currentWeatherUrl: String = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&APPID=%s"

    private val forecastWeatherUrl: String = "http://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric&APPID=%s"

    private val uvIndexUrl: String = "http://api.openweathermap.org/data/2.5/uvi?lat=%.2f&lon=%.2f&APPID=%s"

    // e.g. https://samples.openweathermap.org/pollution/v1/co/0.0,10.0/2016-12-25T01:04:08Z.json?appid=b1b15e88fa797225412429c1c50c122a1
    private val airPollutionUrl: String = "http://api.openweathermap.org/pollution/v1/%s/%s/%s.json?appid=%s"

    private val currentWeatherNotificationId: Int = 260520181

    private val forecastWeatherNotificationId: Int = 260520182

    private val uvIndexNotificationId: Int = 260520183

    private var mayLoad: Boolean = false

    private var converter: JsonToWeatherConverter = JsonToWeatherConverter()

    private lateinit var context: Context

    private lateinit var networkController: NetworkController

    private lateinit var notificationController: NotificationController

    private lateinit var sharedPreferenceController: SharedPreferenceController

    private val minReloadTime: TimeXt = 5.minutes

    private val sharedPrefKeyCity: String = context.resources.getString(R.string.sharedPreferencesKeyCity)

    var city: City? = null
        get() = Gson().fromJson(sharedPreferenceController.load(sharedPrefKeyCity, Gson().toJson(field)), City::class.java)
        private set(value) {
            field = value ?: City()
            sharedPreferenceController.save(sharedPrefKeyCity, Gson().toJson(field))
            cityPublishSubject.onNext(RxOptional(value))
        }
    override val cityPublishSubject = BehaviorSubject.create<RxOptional<City>>()
    private var lastCityCallDate: Date = Date(0)

    var weatherCurrent: WeatherCurrent? = null
        private set(value) {
            field = value
            weatherCurrentPublishSubject.onNext(RxOptional(value))
        }
    override val weatherCurrentPublishSubject = BehaviorSubject.create<RxOptional<WeatherCurrent>>()
    private var lastWeatherCurrentCallDate: Date = Date(0)

    var weatherForecast: WeatherForecast? = null
        private set(value) {
            field = value
            weatherForecastPublishSubject.onNext(RxOptional(value))
        }
    override val weatherForecastPublishSubject = BehaviorSubject.create<RxOptional<WeatherForecast>>()
    private var lastWeatherForecastCallDate: Date = Date(0)

    var uvIndex: UvIndex? = null
        private set(value) {
            field = value
            uvIndexPublishSubject.onNext(RxOptional(value))
        }
    override val uvIndexPublishSubject = BehaviorSubject.create<RxOptional<UvIndex>>()
    private var lastUvIndexCallDate: Date = Date(0)

    var carbonMonoxide: CarbonMonoxide? = null
        private set(value) {
            field = value
            carbonMonoxidePublishSubject.onNext(RxOptional(value))
        }
    override val carbonMonoxidePublishSubject = BehaviorSubject.create<RxOptional<CarbonMonoxide>>()
    private var lastCarbonMonoxideCallDate: Date = Date(0)

    var nitrogenDioxide: NitrogenDioxide? = null
        private set(value) {
            field = value
            nitrogenDioxidePublishSubject.onNext(RxOptional(value))
        }
    override val nitrogenDioxidePublishSubject = BehaviorSubject.create<RxOptional<NitrogenDioxide>>()
    private var lastNitrogenDioxideCallDate: Date = Date(0)

    var ozone: Ozone? = null
        private set(value) {
            field = value
            ozonePublishSubject.onNext(RxOptional(value))
        }
    override val ozonePublishSubject = BehaviorSubject.create<RxOptional<Ozone>>()
    private var lastOzoneCallDate: Date = Date(0)

    var sulfurDioxide: SulfurDioxide? = null
        private set(value) {
            field = value
            sulfurDioxidePublishSubject.onNext(RxOptional(value))
        }
    override val sulfurDioxidePublishSubject = BehaviorSubject.create<RxOptional<SulfurDioxide>>()
    private var lastSulfurDioxideCallDate: Date = Date(0)

    private val onApiServiceListener = object : OnApiServiceListener {
        override fun onFinished(downloadType: DownloadType, jsonString: String, success: Boolean) {
            Log.v(tag, "Received onFinished")
            when (downloadType) {
                DownloadType.CarbonMonoxide -> handleCarbonMonoxideDataUpdate(success, jsonString)
                DownloadType.CityData -> handleCityDataUpdate(success, jsonString)
                DownloadType.CurrentWeather -> handleCurrentWeatherUpdate(success, jsonString)
                DownloadType.ForecastWeather -> handleForecastWeatherUpdate(success, jsonString)
                DownloadType.NitrogenDioxide -> handleNitrogenDioxideDataUpdate(success, jsonString)
                DownloadType.Ozone -> handleOzoneDataUpdate(success, jsonString)
                DownloadType.SulfurDioxide -> handleSulfurDioxideDataUpdate(success, jsonString)
                DownloadType.UvIndex -> handleUvIndexUpdate(success, jsonString)
                DownloadType.CityImage -> {
                    // Nothing to do here
                }
                DownloadType.Null -> {
                    Log.e(tag, "Received download update with downloadType Null and jsonString: $jsonString")
                    city = null

                    weatherCurrent = null
                    weatherForecast = null
                    uvIndex = null

                    carbonMonoxide = null
                    nitrogenDioxide = null
                    ozone = null
                    sulfurDioxide = null
                }
            }
        }
    }

    override lateinit var apiKey: String

    override var notificationEnabledWeatherCurrent: Boolean = true
        set(value) {
            field = value
            displayNotificationWeatherCurrent()
        }

    override var notificationEnabledWeatherForecast: Boolean = true
        set(value) {
            field = value
            displayNotificationWeatherForecast()
        }

    override var notificationEnabledUvIndex: Boolean = true
        set(value) {
            field = value
            displayNotificationUvIndex()
        }

    override var receiverActivity: Class<*>? = null
        set(value) {
            field = value
            displayNotificationWeatherCurrent()
            displayNotificationWeatherForecast()
            displayNotificationUvIndex()
        }

    override var wallpaperEnabled: Boolean = false
        set(value) {
            field = value
            changeWallpaper()
        }

    private object Holder {
        @SuppressLint("StaticFieldLeak")
        val instance: OpenWeatherMapService = OpenWeatherMapService()
    }

    companion object {
        val instance: OpenWeatherMapService by lazy { Holder.instance }
    }

    override fun initialize(context: Context, cityName: String) {
        this.context = context

        this.networkController = NetworkController(this.context)
        this.notificationController = NotificationController(this.context)
        this.sharedPreferenceController = SharedPreferenceController(this.context)

        if (!this.sharedPreferenceController.exists(sharedPrefKeyCity)) {
            Log.i(tag, "Initial save of default city")
            val city = City()
            city.name = cityName
            this.sharedPreferenceController.save(sharedPrefKeyCity, Gson().toJson(city))
        }

        loadCityData(cityName)
    }

    override fun start() {
        mayLoad = true

        loadWeatherCurrent()
        loadWeatherForecast()
        loadUvIndex()

        loadCarbonMonoxide(Calendar.getInstance().airPollutionCurrentDateTime(), 1)
        loadNitrogenDioxide(Calendar.getInstance().airPollutionCurrentDateTime(), 1)
        loadOzone(Calendar.getInstance().airPollutionCurrentDateTime(), 1)
        loadSulfurDioxide(Calendar.getInstance().airPollutionCurrentDateTime(), 1)
    }

    override fun dispose() {
        mayLoad = false
    }

    override fun loadWeatherCurrent(): Boolean {
        if (!mayLoad || city == null || city!!.isDefault()) {
            return false
        }

        if (Date() - lastWeatherCurrentCallDate < minReloadTime) {
            // Set it to itself again to trigger subscriptions
            weatherCurrent = weatherCurrent
        } else {
            doApiRestCall(DownloadType.CurrentWeather, String.format(currentWeatherUrl, city?.name, apiKey))
        }

        return true
    }

    override fun loadWeatherForecast(): Boolean {
        if (!mayLoad || city == null || city!!.isDefault()) {
            return false
        }

        if (Date() - lastWeatherCurrentCallDate < minReloadTime) {
            // Set it to itself again to trigger subscriptions
            weatherForecast = weatherForecast
        } else {
            doApiRestCall(DownloadType.ForecastWeather, String.format(forecastWeatherUrl, city?.name, apiKey))
        }

        return true
    }

    override fun loadUvIndex(): Boolean {
        if (!mayLoad || city == null || city!!.isDefault()) {
            return false
        }

        if (Date() - lastWeatherCurrentCallDate < minReloadTime) {
            // Set it to itself again to trigger subscriptions
            uvIndex = uvIndex
        } else {
            doApiRestCall(DownloadType.UvIndex, String.format(uvIndexUrl, city?.coordinates?.lat, city?.coordinates?.lon, apiKey))
        }

        return true
    }

    override fun loadCarbonMonoxide(dateTime: String, accuracy: Int): Boolean = loadAirPollution(dateTime, accuracy, DownloadType.CarbonMonoxide, AirPollutionType.CarbonMonoxide)

    override fun loadNitrogenDioxide(dateTime: String, accuracy: Int): Boolean = loadAirPollution(dateTime, accuracy, DownloadType.NitrogenDioxide, AirPollutionType.NitrogenDioxide)

    override fun loadOzone(dateTime: String, accuracy: Int): Boolean = loadAirPollution(dateTime, accuracy, DownloadType.Ozone, AirPollutionType.Ozone)

    override fun loadSulfurDioxide(dateTime: String, accuracy: Int): Boolean = loadAirPollution(dateTime, accuracy, DownloadType.SulfurDioxide, AirPollutionType.SulfurDioxide)

    private fun loadAirPollution(dateTime: String, accuracy: Int, downloadType: DownloadType, airPollutionType: AirPollutionType): Boolean {
        if (!mayLoad || city == null || city!!.isDefault() || dateTime.isEmpty()) {
            return false
        }

        if (downloadType == DownloadType.CarbonMonoxide && Date() - lastCarbonMonoxideCallDate < minReloadTime) {
            // Set it to itself again to trigger subscriptions
            carbonMonoxide = carbonMonoxide
        } else if (downloadType == DownloadType.NitrogenDioxide && Date() - lastNitrogenDioxideCallDate < minReloadTime) {
            // Set it to itself again to trigger subscriptions
            nitrogenDioxide = nitrogenDioxide
        } else if (downloadType == DownloadType.Ozone && Date() - lastOzoneCallDate < minReloadTime) {
            // Set it to itself again to trigger subscriptions
            ozone = ozone
        } else if (downloadType == DownloadType.SulfurDioxide && Date() - lastSulfurDioxideCallDate < minReloadTime) {
            // Set it to itself again to trigger subscriptions
            sulfurDioxide = sulfurDioxide
        } else {
            doApiRestCall(downloadType,
                    String.format(airPollutionUrl,
                            airPollutionType.type,
                            "${city?.coordinates?.lat?.doubleFormat(accuracy)},${city?.coordinates?.lon?.doubleFormat(accuracy)}",
                            dateTime,
                            apiKey))
        }

        return true
    }

    override fun loadCityData(cityName: String): Boolean {
        if (Date() - lastCityCallDate < minReloadTime) {
            // Set it to itself again to trigger subscriptions
            city = city
        } else {
            doApiRestCall(DownloadType.CityData, String.format(geoCodeForCityUrl, cityName))
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

    private fun handleCarbonMonoxideDataUpdate(success: Boolean, jsonString: String) {
        carbonMonoxide = if (!success) {
            null
        } else {
            lastCarbonMonoxideCallDate = Date()
            converter.convertToCarbonMonoxide(jsonString)
        }
    }

    private fun handleNitrogenDioxideDataUpdate(success: Boolean, jsonString: String) {
        nitrogenDioxide = if (!success) {
            null
        } else {
            lastNitrogenDioxideCallDate = Date()
            converter.convertToNitrogenDioxide(jsonString)
        }
    }

    private fun handleOzoneDataUpdate(success: Boolean, jsonString: String) {
        ozone = if (!success) {
            null
        } else {
            lastOzoneCallDate = Date()
            converter.convertToOzone(jsonString)
        }
    }

    private fun handleSulfurDioxideDataUpdate(success: Boolean, jsonString: String) {
        sulfurDioxide = if (!success) {
            null
        } else {
            lastSulfurDioxideCallDate = Date()
            converter.convertToSulfurDioxide(jsonString)
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
                lastWeatherCurrentCallDate = Date()
                weatherCurrent = convertedCurrentWeather
                changeWallpaper()
                displayNotificationWeatherCurrent()
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
                lastWeatherForecastCallDate = Date()
                weatherForecast = convertedForecastWeather
                displayNotificationWeatherForecast()
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
                lastUvIndexCallDate = Date()
                uvIndex = convertedUvIndex
                displayNotificationUvIndex()
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
                lastCityCallDate = Date()

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

    private fun displayNotificationWeatherCurrent() {
        if (notificationEnabledWeatherCurrent && weatherCurrent != null) {
            val currentWeatherNotificationContent = NotificationContent(
                    currentWeatherNotificationId,
                    "Current Weather: " + weatherCurrent!!.description,
                    weatherCurrent!!.temperature.doubleFormat(1) + "${String.degreeSign}C, "
                            + "(" + weatherCurrent!!.temperatureMin.doubleFormat(1) + "${String.degreeSign}C - "
                            + weatherCurrent!!.temperatureMax.doubleFormat(1) + "${String.degreeSign}C), "
                            + weatherCurrent!!.pressure.doubleFormat(1) + "mBar, "
                            + weatherCurrent!!.humidity.doubleFormat(1) + "%",
                    weatherCurrent!!.weatherCondition.iconId,
                    weatherCurrent!!.weatherCondition.wallpaperId,
                    receiverActivity!!,
                    true)
            notificationController.create(currentWeatherNotificationContent)
        } else {
            notificationController.close(currentWeatherNotificationId)
        }
    }

    private fun displayNotificationWeatherForecast() {
        if (notificationEnabledWeatherForecast && weatherForecast != null) {
            val forecastWeatherNotificationContent = NotificationContent(
                    forecastWeatherNotificationId,
                    "Forecast: " + weatherForecast!!.getMostWeatherCondition().description,
                    weatherForecast!!.getMinTemperature().doubleFormat(1) + "${String.degreeSign}C - "
                            + weatherForecast!!.getMaxTemperature().doubleFormat(1) + "${String.degreeSign}C, "
                            + weatherForecast!!.getMinPressure().doubleFormat(1) + "mBar - "
                            + weatherForecast!!.getMaxPressure().doubleFormat(1) + "mBar, "
                            + weatherForecast!!.getMinHumidity().doubleFormat(1) + "% - "
                            + weatherForecast!!.getMaxHumidity().doubleFormat(1) + "%",
                    weatherForecast!!.getMostWeatherCondition().iconId,
                    weatherForecast!!.getMostWeatherCondition().wallpaperId,
                    receiverActivity!!,
                    true)
            notificationController.create(forecastWeatherNotificationContent)
        } else {
            notificationController.close(forecastWeatherNotificationId)
        }
    }

    private fun displayNotificationUvIndex() {
        if (notificationEnabledUvIndex && uvIndex != null) {
            val forecastWeatherNotificationContent = NotificationContent(
                    uvIndexNotificationId,
                    "UvIndex",
                    "Value is ${uvIndex!!.value}",
                    R.drawable.uv_index,
                    R.drawable.uv_index,
                    receiverActivity!!,
                    true)
            notificationController.create(forecastWeatherNotificationContent)
        } else {
            notificationController.close(uvIndexNotificationId)
        }
    }

    private fun changeWallpaper() {
        if (wallpaperEnabled && weatherCurrent != null) {
            try {
                val wallpaperManager = WallpaperManager.getInstance(context.applicationContext)
                wallpaperManager.setResource(weatherCurrent!!.weatherCondition.wallpaperId)
            } catch (exception: IOException) {
                Log.e(tag, exception.message)
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