# OpenWeather-Android - development branch

[![Platform](https://img.shields.io/badge/platform-Android-blue.svg)](https://www.android.com)
<a target="_blank" href="https://www.paypal.me/GuepardoApps" title="Donate using PayPal"><img src="https://img.shields.io/badge/paypal-donate-blue.svg" /></a>
<a target="_blank" href="https://android-arsenal.com/api?level=21" title="API21+"><img src="https://img.shields.io/badge/API-21+-blue.svg" /></a>
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

[![Build](https://img.shields.io/badge/build-failing-red.svg)](https://github.com/OpenWeatherLib/OpenWeather-Android/tree/develop/releases)
[![Version](https://img.shields.io/badge/version-v1.5.0.180923-green.svg)](https://github.com/OpenWeatherLib/OpenWeather-Android/tree/develop/releases/openweather-android-2018-09-23.aar)

library for downloading and handling data from openweather
example application can be found here: https://github.com/OpenWeatherLib/OpenWeather-Android/tree/develop/app (Fork project and add your private OpenWeather ApiKey to strings.xml)

Based on Kotlin, using Listener, Extensions and more.

Used Libraries are

- com.baoyz.pullrefreshlayout:library:1.2.0
- com.flaviofaria:kenburnsview:1.0.7
- com.github.florent37:expansionpanel:1.1.1
- com.github.GrenderG:Toasty:1.2.5
- com.github.matecode:Snacky:1.0.2
- com.github.rey5137:material:1.2.4
- com.google.code.gson:gson:2.8.5
- com.squareup.okhttp3:okhttp:3.9.1
- it.sephiroth.android.library.bottomnavigation:bottom-navigation:2.0.1-rc1

- io.reactivex.rxjava2:rxkotlin:2.2.0

- com.android.support.constraint:constraint-layout:1.1.3
- using also latest API28 libs

- tests based on mockito and spek

- and some more

## Warning 
latest Android Studio caused some errors on my machine. Have to figure this out in the next few days

---

![alt tag](https://github.com/OpenWeatherLib/OpenWeather-Android/blob/develop/screenshots/example_usage.png)

---

![alt tag](https://github.com/OpenWeatherLib/OpenWeather-Android/blob/develop/screenshots/example_app.png)

---

# Integration

First you have to register an account at [OpenWeatherMap.org](http://www.openweathermap.org/) and receive an API key.
This key is an important parameter of the OpenWeatherService!

The easiest way to integrate the library is to use the OpenWeatherService and to subscribe on weatherCurrentPublishSubject and weatherForecastPublishSubject using ReactiveX2

```java
class MainActivity : AppCompatActivity() {

    private var subscriptions: Array<Disposable?> = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        ...

        OpenWeatherService.instance.initialize(this, getString(R.string.openweather_city)) // Initialize service already with your preferred city
        OpenWeatherService.instance.apiKey = getString(R.string.openweather_api_key)    // Set ApiKey => Will be read from xml file

        ImageService.instance.initialize(this) 											// Initialize service
        ImageService.instance.accessKey = getString(R.string.image_api_access_key)    	// Set AccessKey => Will be read from xml file

        OpenWeatherService.instance.notificationEnabled = true                          // Enable/Disable notifications
        OpenWeatherService.instance.wallpaperEnabled = true                             // Enable/Disable set of wallpaper
        OpenWeatherService.instance.receiverActivity = MainActivity::class.java         // Set receiver for notifications
        OpenWeatherService.instance.reloadEnabled = true                                // Enable/Disable reload of data
        OpenWeatherService.instance.reloadTimeout = 30 * 60 * 1000                      // Set timeout of reload of data in millisecond
		
        // Subscribe on weatherCurrentPublishSubject (Using ReactiveX2)
		subscriptions = subscriptions.plus(
			OpenWeatherService.instance.weatherCurrentPublishSubject
				.subscribeOn(Schedulers.io())
				.subscribe(
					{
						response -> TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
					},
					{
						responseError -> TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
					}
				))

        // Subscribe on weatherForecastPublishSubject (Using ReactiveX2)
		subscriptions = subscriptions.plus(
			OpenWeatherService.instance.weatherForecastPublishSubject
				.subscribeOn(Schedulers.io())
				.subscribe(
					{
						response -> TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
					},
					{
						responseError -> TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
					}
				))

        // Subscribe on uvIndexPublishSubject (Using ReactiveX2)
		subscriptions = subscriptions.plus(
			OpenWeatherService.instance.uvIndexPublishSubject
				.subscribeOn(Schedulers.io())
				.subscribe(
					{
						response -> TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
					},
					{
						responseError -> TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
					}
				))

        // Finally start everything (IMPORTANT)
        OpenWeatherService.instance.start()

        ...
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.forEach { x -> x?.dispose() }
        subscriptions = arrayOf()
        OpenWeatherService.instance.dispose() // Dispose the service
    }
}
```

To display received data use the custom adapter in the library

```java
class MainActivity : AppCompatActivity() {

    private var subscriptions: Array<Disposable?> = arrayOf()

    ...
    // Subscribe on weatherForecastPublishSubject (Using ReactiveX2)
	subscriptions = subscriptions.plus(
		OpenWeatherService.instance.weatherForecastPublishSubject
			.subscribeOn(Schedulers.io())
			.subscribe(
				{
					response -> 
						if (response.value != null) {
							val data = response.value as WeatherForecast
							val list = data.list
							if (list.isNotEmpty()) {
								val adapter = ForecastListAdapter(this, list)
								listView.adapter = adapter
								mainImageView.setImageResource(forecastWeather.getMostWeatherCondition().wallpaperId)
							} else {
								Logger.instance.warning(tag, "list is empty")
							}
						} else {
							Logger.instance.warning(tag, "weather forecast subscribe was  not successfully")
						}
				},
				{
					responseError -> // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
				}
			))
    ...
}
```