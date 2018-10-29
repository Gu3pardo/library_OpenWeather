# OpenWeather-Android - master branch

[![Platform](https://img.shields.io/badge/platform-Android-blue.svg)](https://www.android.com)
<a target="_blank" href="https://www.paypal.me/GuepardoApps" title="Donate using PayPal"><img src="https://img.shields.io/badge/paypal-donate-blue.svg" /></a>
<a target="_blank" href="https://android-arsenal.com/api?level=24" title="API24+"><img src="https://img.shields.io/badge/API-24+-blue.svg" /></a>
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

[![](https://jitpack.io/v/OpenWeatherLib/OpenWeather-Android.svg)](https://jitpack.io/#OpenWeatherLib/OpenWeather-Android)
[![Version](https://img.shields.io/badge/version-v1.6.0.181029-green.svg)](https://github.com/OpenWeatherLib/OpenWeather-Android/releases/tag/v1.6.0-alpha01)
[![Build](https://img.shields.io/badge/build-passing-green.svg)](https://github.com/OpenWeatherLib/OpenWeather-Android/tree/master/)

## About

library for downloading and handling data from openweather
example application can be found here: https://github.com/OpenWeatherLib/OpenWeather-Android/tree/master/app (Fork project and add your private OpenWeather ApiKey to settings.xml)
Based on Kotlin, using Listener, Extensions and more.

---

![alt tag](https://github.com/OpenWeatherLib/OpenWeather-Android/blob/master/screenshots/example_usage.png)

---

![alt tag](https://github.com/OpenWeatherLib/OpenWeather-Android/blob/master/screenshots/example_app.png)

---

## Integration

First you have to register an account at [OpenWeatherMap.org](http://www.openweathermap.org/) and receive an API key.
This key is an important parameter of the OpenWeatherService and needs to be set in the settings.xml along with the key for unsplash api (new, not needed but fancy for city image)!

The easiest way to integrate the library is to use the OpenWeatherService and to subscribe on weatherCurrentPublishSubject and weatherForecastPublishSubject using ReactiveX2.
You can also use the build in views for the city, current and forecast weather and  the uv index.

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
        OpenWeatherService.instance.reloadTimeout = 30.minutes.inMilliseconds           // Set timeout of reload of data in millisecond (using [TimeXt](https://github.com/TimeXt/TimeXt-Kotlin))
		
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

## Installation

Add the JitPack repository to your `build.gradle`:

```groovy
allprojects {
 repositories {
    maven { url "https://jitpack.io" }
    }
}
```

Add the dependency to your `build.gradle`:

```groovy
dependencies {
    implementation 'com.github.OpenWeatherLib.OpenWeatherLib-Android:lib:v1.6.0-alpha01'
}
```

## Requirements

- Use at least JVM 1.7

## License

OpenWeather-Android is distributed under the MIT license. [See LICENSE](https://github.com/OpenWeatherLib/OpenWeather-Android/blob/develop/LICENSE.md) for details.
