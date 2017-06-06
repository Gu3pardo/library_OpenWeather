# library_OpenWeather

[![Platform](https://img.shields.io/badge/platform-Android-blue.svg)](https://www.android.com)
<a target="_blank" href="https://www.paypal.me/GuepardoApps" title="Donate using PayPal"><img src="https://img.shields.io/badge/paypal-donate-blue.svg" /></a>
<a target="_blank" href="https://android-arsenal.com/api?level=21" title="API21+"><img src="https://img.shields.io/badge/API-21+-blue.svg" /></a>
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

library for downloading and handling data from openweather
used in https://github.com/Gu3pardo/LucaHome-AndroidApplication

---

![alt tag](https://github.com/Gu3pardo/library_OpenWeather/blob/master/screenshots/example_usage.png)

---

IMPORTANT:
This library uses https://github.com/Gu3pardo/library_GuepardoAppsToolSet!

---

# Integration

First you have to register an account at http://www.openweathermap.org/

Then enter your key in following class:

```java
package guepardoapps.library.openweather.common;

public class OWKeys {
	public static final String OPEN_WEATHER_KEY = ENTER_YOUR_KEY_HERE;
}
```

The easiest way to integrate the library is to have one OpenWeatherController and to register some BroadcastReceiver in your Activity in onResume.
After registering your Receiver, you call for the data.

```java
public class MainActivity extends Activity {
	
	...
	private OpenWeatherController _openWeatherController;
	private ReceiverController _receiverController;
	
	private BroadcastReceiver _currentWeatherReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			...
			WeatherModel currentWeather = (WeatherModel) intent.getSerializableExtra(OWBundles.EXTRA_WEATHER_MODEL);
			...
		}
	};
	
	private BroadcastReceiver _forecastWeatherReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			...
			ForecastModel forecastWeather = (ForecastModel) intent.getSerializableExtra(OWBundles.EXTRA_FORECAST_MODEL);
			...
		}
	};
	
	...
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		...
		
		_openWeatherController = new OpenWeatherController(this, "Munich, DE");
		_receiverController = new ReceiverController(this);
		
		...
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		_receiverController.RegisterReceiver(_currentWeatherReceiver, new String[]{OWBroadcasts.CURRENT_WEATHER_JSON_FINISHED});
		_receiverController.RegisterReceiver(_forecastWeatherReceiver, new String[]{OWBroadcasts.FORECAST_WEATHER_JSON_FINISHED});
        
		// To load the current weather in your city
		_openWeatherController.LoadCurrentWeather();
		
		// To load forecast weather for your city
		_openWeatherController.LoadForecastWeather();
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
			ForecastModel forecastWeather = (ForecastModel) intent.getSerializableExtra(OWBundles.EXTRA_FORECAST_MODEL);
			
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