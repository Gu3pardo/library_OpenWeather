package guepardoapps.library.openweather.controller;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import guepardoapps.library.openweather.R;
import guepardoapps.library.openweather.downloader.ForecastModelDownloader;
import guepardoapps.library.openweather.downloader.WeatherModelDownloader;

import guepardoapps.toolset.common.Logger;
import guepardoapps.toolset.controller.DialogController;
import guepardoapps.toolset.controller.NetworkController;

public class OpenWeatherController {

	private static final String TAG = OpenWeatherController.class.getSimpleName();
	private Logger _logger;

	private String _city;

	private Context _context;

	private ForecastModelDownloader _forecastWeatherDownloader;
	private NetworkController _networkController;
	private WeatherModelDownloader _weatherDownloader;

	public OpenWeatherController(Context context, String city) {
		_logger = new Logger(TAG);
		_logger.Debug(OpenWeatherController.class.getName() + " created...");

		_context = context;

		_city = city;

		_logger.Debug("City: " + _city);

		_forecastWeatherDownloader = new ForecastModelDownloader(_context, _city);
		_networkController = new NetworkController(_context,
				new DialogController(_context, ContextCompat.getColor(_context, R.color.colorWhite),
						ContextCompat.getColor(_context, R.color.colorPrimary)));
		_weatherDownloader = new WeatherModelDownloader(_context, _city);
	}

	public void loadCurrentWeather() {
		if (_networkController.IsNetworkAvailable()) {
			_weatherDownloader.GetJson();
		} else {
			_logger.Warn("No network available!");
		}
	}

	public void loadForecastWeather() {
		if (_networkController.IsNetworkAvailable()) {
			_forecastWeatherDownloader.GetJson();
		} else {
			_logger.Warn("No network available!");
		}
	}
}
