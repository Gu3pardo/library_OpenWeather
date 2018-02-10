package guepardoapps.library.openweather.downloader;

import android.support.annotation.NonNull;

import java.io.Serializable;

public interface IOpenWeatherDownloader {
    enum WeatherDownloadType implements Serializable {CurrentWeather, ForecastWeather}

    enum DownloadActionResult {InvalidCity, InvalidApiKey, Performing}

    String DownloadFinishedBroadcast = "guepardoapps.library.openweather.downloader.broadcast.finished";
    String DownloadFinishedBundle = "DownloadFinishedBundle ";

    void SetCity(@NonNull String city);

    String GetCity();

    void SetApiKey(@NonNull String apiKey);

    String GetApiKey();

    DownloadActionResult DownloadCurrentWeather();

    DownloadActionResult DownloadForecastWeather();
}
