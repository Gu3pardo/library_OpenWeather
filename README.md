# library_OpenWeather - Develop Tree

[![Platform](https://img.shields.io/badge/platform-Android-blue.svg)](https://www.android.com)
<a target="_blank" href="https://www.paypal.me/GuepardoApps" title="Donate using PayPal"><img src="https://img.shields.io/badge/paypal-donate-blue.svg" /></a>
<a target="_blank" href="https://android-arsenal.com/api?level=21" title="API21+"><img src="https://img.shields.io/badge/API-21+-blue.svg" /></a>
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

[![Build](https://img.shields.io/badge/build-passing-green.svg)](https://github.com/GuepardoApps/library_OpenWeather/tree/develop/releases)
[![Version](https://img.shields.io/badge/version-v1.0.6.180706-green.svg)](https://github.com/GuepardoApps/library_OpenWeather/tree/develop/releases/openweather-2018-07-06.aar)

library for downloading and handling data from openweather
example application can be found here: https://github.com/GuepardoApps/library_OpenWeather/tree/develop/app (Fork project and add your private OpenWeather ApiKey to MainActivity)

Based on Kotlin, using Listener, Extensions and more.

Used Libraries are

- com.baoyz.pullrefreshlayout:library:1.2.0
- com.flaviofaria:kenburnsview:1.0.7
- com.github.florent37:expansionpanel:1.1.1
- com.github.GrenderG:Toasty:1.2.5
- com.github.matecode:Snacky:1.0.2
- com.github.rey5137:material:1.2.4
- com.squareup.okhttp3:okhttp:3.9.1

- android.arch.work:work-runtime-ktx:1.0.0-alpha04

- com.android.support.constraint:constraint-layout:1.1.2
- using also latest API28 libs

- and some more

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

        openWeatherService = OpenWeatherService.instance
        openWeatherService.initialize(this)
		
        // Set ApiKey => Will be read from xml file
        openWeatherService.apiKey = getString(R.string.openweather_api_key)
		
        // Set your preferred city
        openWeatherService.city = getString(R.string.openweather_city)
		
        // Enable/Disable notifications
        openWeatherService.notificationEnabled = true
		
        // Enable/Disable set of wallpaper
        openWeatherService.wallpaperEnabled = true
		
        // Set receiver for notifications
        openWeatherService.receiverActivity = MainActivity::class.java
		
        // Set OnWeatherUpdateListener
        openWeatherService.onWeatherServiceListener = (object : OnWeatherServiceListener {
            override fun onCurrentWeather(currentWeather: WeatherCurrent?, success: Boolean) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onForecastWeather(forecastWeather: WeatherForecast?, success: Boolean) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        // Enable/Disable reload of data
        openWeatherService.reloadEnabled = true
		
        // Set timeout of reload of data
        openWeatherService.reloadTimeout = 30 * 60 * 1000
		
        ...
    }
}
```

To display received data use the customadapter in the library

```java
public class MainActivity extends Activity {
    ...
        openWeatherService.onWeatherServiceListener = (object : OnWeatherServiceListener {
	    ...
        override fun onForecastWeather(forecastWeather: WeatherForecast?, success: Boolean) {
            if (success) {
                this.forecastWeather = forecastWeather!!
                val forecastList = forecastWeather.list
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