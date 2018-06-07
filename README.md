# library_OpenWeather - Develop Tree

[![Platform](https://img.shields.io/badge/platform-Android-blue.svg)](https://www.android.com)
<a target="_blank" href="https://www.paypal.me/GuepardoApps" title="Donate using PayPal"><img src="https://img.shields.io/badge/paypal-donate-blue.svg" /></a>
<a target="_blank" href="https://android-arsenal.com/api?level=21" title="API21+"><img src="https://img.shields.io/badge/API-21+-blue.svg" /></a>
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

[![Build](https://img.shields.io/badge/build-passing-green.svg)](https://github.com/GuepardoApps/library_OpenWeather/tree/develop/releases)
[![Version](https://img.shields.io/badge/version-v1.0.2.180607-green.svg)](https://github.com/GuepardoApps/library_OpenWeather/tree/develop/releases/openweather-2018-06-07.aar)

library for downloading and handling data from openweather
example application can be found here: https://github.com/GuepardoApps/library_OpenWeather/tree/develop/app (Fork project and add your private OpenWeather ApiKey to MainActivity)

Based on Kotlin, using Listener, Extensions and more.

Used Libraries are
- com.squareup.okhttp3:okhttp:3.9.1
- com.github.florent37:expansionpanel:1.1.1
- using also latest API28 libs

---

![alt tag](https://github.com/GuepardoApps/library_OpenWeather/blob/develop/screenshots/example_usage.png)

---

# Integration

First you have to register an account at [OpenWeatherMap.org](http://www.openweathermap.org/) and receive an API key.
This key is an important parameter of the OpenWeatherService!

The easiest way to integrate the library is to use the OpenWeatherService and to register the OnWeatherUpdateListener
After registering your Receiver, you call for the data.

```java
public class MainActivity extends Activity {

    ...
    private lateinit var openWeatherService: OpenWeatherService
    ...

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ...

        openWeatherService = OpenWeatherService(this)
		
        // Set ApiKey
        openWeatherService.setApiKey("") // TODO Add ApiKey
		
        // Set your preferred city
        openWeatherService.setCity("Nuremberg")
		
        // Enable/Disable notifications
        openWeatherService.setNotificationEnabled(true)
		
        // Enable/Disable reload of data
        openWeatherService.setReloadEnabled(true
		
        // Set timeout of reload of data
        openWeatherService.setReloadTimeout(30 * 60 * 1000)
		
        // Enable/Disable set of wallpaper
        openWeatherService.setWallpaperEnabled(true)
		
        // Set receiver for notifications
        openWeatherService.setReceiverActivity(MainActivity::class.java)
		
        // Set OnWeatherUpdateListener
        openWeatherService.setOnWeatherUpdateListener(object : OnWeatherUpdateListener {
            override fun onCurrentWeather(currentWeather: IWeatherCurrent?, success: Boolean) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onForecastWeather(forecastWeather: IWeatherForecast?, success: Boolean) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        // Load the current weather
        openWeatherService.loadCurrentWeather()
		
        // Load the forecast weather
        openWeatherService.loadForecastWeather()
		
        ...
    }
}
```

To display received data use the customadapter in the library

```java
public class MainActivity extends Activity {
    ...
    openWeatherService.setOnWeatherUpdateListener(object : OnWeatherUpdateListener {
	    ...
        override fun onForecastWeather(forecastWeather: IWeatherForecast?, success: Boolean) {
            if (success) {
                this.forecastWeather = forecastWeather!!
                val forecastList = forecastWeather.getList()
                if (forecastList.isNotEmpty()) {
                    val adapter = ForecastListAdapter(this, forecastList)
                    listView.adapter = adapter
                    mainImageView.setImageResource(forecastWeather.getMostWeatherCondition().wallpaperId)
                } else {
                    Logger.instance.warning(tag, "forecastList is empty")
                }
            } else {
                Logger.instance.warning(tag, "onForecastWeather download was  not successfully")
                Toasty.warning(context, "onForecastWeather download was  not successfully", Toast.LENGTH_LONG).show()
            }
        }
    })
    ...
}
```