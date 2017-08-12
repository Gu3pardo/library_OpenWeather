# library_OpenWeather

[![Platform](https://img.shields.io/badge/platform-Android-blue.svg)](https://www.android.com)
<a target="_blank" href="https://www.paypal.me/GuepardoApps" title="Donate using PayPal"><img src="https://img.shields.io/badge/paypal-donate-blue.svg" /></a>
<a target="_blank" href="https://android-arsenal.com/api?level=21" title="API21+"><img src="https://img.shields.io/badge/API-21+-blue.svg" /></a>
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

[![Build](https://img.shields.io/badge/build-passing-green.svg)](https://github.com/GuepardoApps/library_OpenWeather)
[![Version](https://img.shields.io/badge/version-v0.9.5.170812-blue.svg)](https://github.com/GuepardoApps/library_OpenWeather)

library for downloading and handling data from openweather
used in https://github.com/GuepardoApps/LucaHome-AndroidApplication

---

![alt tag](https://github.com/GuepardoApps/library_OpenWeather/blob/master/screenshots/example_usage.png)

---

# Integration

First you have to register an account at http://www.openweathermap.org/

Then enter your key in following class:

```java
package guepardoapps.library.openweather.common;

public class OWKeys {
	public static final String OPEN_WEATHER_KEY = "ENTER_YOUR_KEY_HERE";
}
```

The easiest way to integrate the library is to have one OpenWeatherController and to register some BroadcastReceiver in your Activity in onResume.
After registering your Receiver, you call for the data.

```java
public class MainActivity extends Activity {
	
	...
	private OpenWeatherService _openWeatherService;
	private ReceiverController _receiverController;
	
	private BroadcastReceiver _currentWeatherReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
            OpenWeatherService.CurrentWeatherDownloadFinishedContent content = (OpenWeatherService.CurrentWeatherDownloadFinishedContent) intent.getSerializableExtra(OpenWeatherService.CurrentWeatherDownloadFinishedBundle);
			WeatherModel currentWeather = content.CurrentWeather;
			// Do whatever you want with the data
			...
		}
	};
	
	private BroadcastReceiver _forecastWeatherReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			OpenWeatherService.ForecastWeatherDownloadFinishedContent content = (OpenWeatherService.ForecastWeatherDownloadFinishedContent) intent.getSerializableExtra(OpenWeatherService.ForecastWeatherDownloadFinishedBundle);
			ForecastModel forecastWeather = content.ForecastModel;
			// Do whatever you want with the data
			...
		}
	};
	
	...
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		...
		
		// Get the instance of the singleton service
		_openWeatherService = new OpenWeatherService.getInstance();
		
		// initialize the service with the current context and a city
		_openWeatherService.Initialize(this, "Munich, DE");
		// or initialize the service with the current context, a city and the enable/disable for notifications
		_openWeatherService.Initialize(this, "Munich, DE", true);
		// or initialize the service with the current context, a city, the enable/disable for notifications and activities which will be started after clicking on the notifications
		_openWeatherService.Initialize(this, "Munich, DE", true, YourCurrentWeatherActiviy.class, MyForecastActiviy.class);
		
		_receiverController = new ReceiverController(this);
		
		...
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		// register the receiver to get the data from the service
		_receiverController.RegisterReceiver(_currentWeatherReceiver, new String[]{OpenWeatherService.CurrentWeatherDownloadFinishedBroadcast});
		_receiverController.RegisterReceiver(_forecastWeatherReceiver, new String[]{OpenWeatherService.ForecastWeatherDownloadFinishedBroadcast});
        
		// To load the current weather in your city
		_openWeatherService.LoadCurrentWeather();
		
		// To load forecast weather for your city
		_openWeatherService.LoadForecastWeather();
	}
	
	...
	
	public void SomeMethod() {
		// you can also get the data from the service if it already downloaded it
		WeatherModel currentWeather = _openWeatherService.CurrentWeather()
		ForecastModel forecastWeather = _openWeatherService.ForecastWeather()
		
		// you can change the city on the fly and the service starts with the download for the city as it was set
		_openWeatherService.SetCity("Another city")
		
		// you can disable/enable notifications
		// notifications will be displayed if they are enabled and a download was finished
		_openWeatherService.SetDisplayNotification(false);
		_openWeatherService.SetDisplayNotification(true);
		
		// you can set a activity which will be started after clicking on a notifications
		_openWeatherService.SetCurrentWeatherReceiverActivity(YourCurrentWeatherActiviy.class);
		_openWeatherService.SetForecastWeatherReceiverActivity(MyForecastActiviy.class);
	}
}
```

To display received data use the customadapter in the library

```java
public class MainActivity extends Activity {

	...
	
	private BroadcastReceiver _forecastWeatherReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			OpenWeatherService.ForecastWeatherDownloadFinishedContent content = (OpenWeatherService.ForecastWeatherDownloadFinishedContent) intent.getSerializableExtra(OpenWeatherService.ForecastWeatherDownloadFinishedBundle);
			ForecastModel forecastWeather = content.ForecastModel;
			
			if (forecastWeather != null) {
				...
				List<ForecastWeatherModel> list = forecastWeather.GetList();
				listView.setAdapter(new ForecastListAdapter(this, list));
				...
			}
		}
	};
	
	...
}
```
