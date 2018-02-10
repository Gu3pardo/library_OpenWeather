# library_OpenWeather

[![Platform](https://img.shields.io/badge/platform-Android-blue.svg)](https://www.android.com)
<a target="_blank" href="https://www.paypal.me/GuepardoApps" title="Donate using PayPal"><img src="https://img.shields.io/badge/paypal-donate-blue.svg" /></a>
<a target="_blank" href="https://android-arsenal.com/api?level=21" title="API21+"><img src="https://img.shields.io/badge/API-21+-blue.svg" /></a>
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

[![Build](https://img.shields.io/badge/build-passing-green.svg)](https://github.com/GuepardoApps/library_OpenWeather)
[![Version](https://img.shields.io/badge/version-v1.0.0.180210-blue.svg)](https://github.com/GuepardoApps/library_OpenWeather)

library for downloading and handling data from openweather
used in https://github.com/GuepardoApps/LucaHome-AndroidApplication

---

![alt tag](https://github.com/GuepardoApps/library_OpenWeather/blob/master/screenshots/example_usage.png)

---

# Integration

First you have to register an account at [OpenWeatherMap.org](http://www.openweathermap.org/) and receive an API key.
This key is an important parameter of the constructor of the OpenWeatherService!

The easiest way to integrate the library is to have one OpenWeatherService and to register some BroadcastReceiver in your Activity in onResume.
After registering your Receiver, you call for the data.

```java
public class MainActivity extends Activity {

	...
	private IOpenWeatherService _openWeatherService;
	private IReceiverController _receiverController;

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

		// initialize the service with the current context and a city. Please replace YOUR_API_KEY with your personal key!
		_openWeatherService.Initialize(this, "Nuremberg, DE", YOUR_API_KEY);
		// or initialize the service with the current context, a city and the enable/disable for notifications, changing the launcher wallpaper and enable for automatic data reload and timeout (in ms). Please replace YOUR_API_KEY with your personal key!
		_openWeatherService.Initialize(this, "Nuremberg, DE", YOUR_API_KEY, true, true, true, true, 5 * 60 * 1000);
		// or initialize the service with the current context, a city, the enable/disable for notifications and activities which will be started after clicking on the notifications. Please replace YOUR_API_KEY with your personal key!
		_openWeatherService.Initialize(this, "Nuremberg, DE", YOUR_API_KEY, true, true, YourCurrentWeatherActiviy.class, MyForecastActiviy.class, true, true, 5 * 60 * 1000);

		_receiverController = new ReceiverController(this);

		...
	}

	@Override
	public void onResume() {
		super.onResume();

		// register the receiver to get the data from the service
		_receiverController.RegisterReceiver(_currentWeatherReceiver, new String[]{IOpenWeatherService.CurrentWeatherDownloadFinishedBroadcast});
		_receiverController.RegisterReceiver(_forecastWeatherReceiver, new String[]{IOpenWeatherService.ForecastWeatherDownloadFinishedBroadcast});

		// To load the current weather in your city
		_openWeatherService.LoadCurrentWeather();

		// To load forecast weather for your city
		_openWeatherService.LoadForecastWeather();
	}

	...

	public void SomeMethod() {
		// you can also get the data from the service if it already downloaded it
		IWeatherModel currentWeather = _openWeatherService.CurrentWeather()
		IForecastModel forecastWeather = _openWeatherService.ForecastWeather()

		// you can change the apikey on the fly
		_openWeatherService.SetApiKey(ANOTHER_API_KEY)

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
			IForecastModel forecastWeather = content.ForecastModel;

			if (forecastWeather != null) {
				...
				ArrayList<IForecastWeatherModel> list = forecastWeather.GetList();
				listView.setAdapter(new ForecastListAdapter(this, list));
				...
			}
		}
	};

	...
}
```
