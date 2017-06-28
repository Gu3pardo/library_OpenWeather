package guepardoapps.library.openweather.controller;

import android.content.Context;
import android.support.annotation.NonNull;

import guepardoapps.library.openweather.common.tools.Logger;
import guepardoapps.library.openweather.downloader.ForecastModelDownloader;
import guepardoapps.library.openweather.downloader.WeatherModelDownloader;

public class OpenWeatherController {

    private static final String TAG = OpenWeatherController.class.getSimpleName();
    private Logger _logger;

    private ForecastModelDownloader _forecastWeatherDownloader;
    private NetworkController _networkController;
    private WeatherModelDownloader _weatherDownloader;

    public OpenWeatherController(
            @NonNull Context context,
            @NonNull String city) {
        _logger = new Logger(TAG);
        _logger.Debug(TAG + " created...");

        _logger.Debug("City: " + city);

        _forecastWeatherDownloader = new ForecastModelDownloader(context, city);
        _networkController = new NetworkController(context);
        _weatherDownloader = new WeatherModelDownloader(context, city);
    }

    public void LoadCurrentWeather() {
        if (_networkController.IsNetworkAvailable()) {
            _weatherDownloader.GetJson();
        } else {
            _logger.Warn("No network available!");
        }
    }

    public void LoadForecastWeather() {
        if (_networkController.IsNetworkAvailable()) {
            _forecastWeatherDownloader.GetJson();
        } else {
            _logger.Warn("No network available!");
        }
    }
}
