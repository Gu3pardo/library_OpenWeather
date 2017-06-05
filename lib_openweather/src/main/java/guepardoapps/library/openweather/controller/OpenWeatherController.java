package guepardoapps.library.openweather.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import guepardoapps.library.openweather.R;
import guepardoapps.library.openweather.common.OWEnables;
import guepardoapps.library.openweather.downloader.ForecastModelDownloader;
import guepardoapps.library.openweather.downloader.WeatherModelDownloader;

import guepardoapps.library.toolset.common.Logger;
import guepardoapps.library.toolset.controller.DialogController;
import guepardoapps.library.toolset.controller.NetworkController;

public class OpenWeatherController {

    private static final String TAG = OpenWeatherController.class.getSimpleName();
    private Logger _logger;

    private ForecastModelDownloader _forecastWeatherDownloader;
    private NetworkController _networkController;
    private WeatherModelDownloader _weatherDownloader;

    public OpenWeatherController(
            @NonNull Context context,
            @NonNull String city) {
        _logger = new Logger(TAG, OWEnables.LOGGING);
        _logger.Debug(TAG + " created...");

        _logger.Debug("City: " + city);

        _forecastWeatherDownloader = new ForecastModelDownloader(context, city);
        _networkController = new NetworkController(context,
                new DialogController(context,
                        ContextCompat.getColor(context, R.color.colorWhite),
                        ContextCompat.getColor(context, R.color.colorPrimary)));
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
