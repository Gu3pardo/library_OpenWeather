package guepardoapps.library.openweather.service;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Calendar;

import guepardoapps.library.openweather.models.IForecastModel;
import guepardoapps.library.openweather.models.IWeatherModel;

@SuppressWarnings({"unused"})
public interface IOpenWeatherService {
    String CurrentWeatherDownloadFinishedBroadcast = "guepardoapps.lucahome.openweather.service.weather.current.download.finished";
    String CurrentWeatherDownloadFinishedBundle = "CurrentWeatherDownloadFinishedBundle";

    String ForecastWeatherDownloadFinishedBroadcast = "guepardoapps.lucahome.openweather.service.weather.forecast.download.finished";
    String ForecastWeatherDownloadFinishedBundle = "ForecastWeatherDownloadFinishedBundle";

    void Initialize(@NonNull Context context, @NonNull String city, @NonNull String apiKey, boolean displayCurrentWeatherNotification, boolean displayForecastWeatherNotification, Class<?> currentWeatherReceiverActivity, Class<?> forecastWeatherReceiverActivity, boolean changeWallpaper, boolean reloadEnabled, int reloadTimeout);

    void Initialize(@NonNull Context context, @NonNull String city, @NonNull String apiKey, boolean displayCurrentWeatherNotification, boolean displayForecastWeatherNotification, boolean changeWallpaper, boolean reloadEnabled, int reloadTimeout);

    void Initialize(@NonNull Context context, @NonNull String city, @NonNull String apiKey);

    void Dispose();

    void SetCity(@NonNull String city);

    String GetCity();

    void SetApiKey(@NonNull String apiKey);

    String GetApiKey();

    void SetDisplayCurrentWeatherNotification(boolean displayCurrentWeatherNotification);

    boolean GetDisplayCurrentWeatherNotification();

    void SetDisplayForecastWeatherNotification(boolean displayForecastWeatherNotification);

    boolean GetDisplayForecastWeatherNotification();

    void SetCurrentWeatherReceiverActivity(@NonNull Class<?> currentWeatherReceiverActivity);

    Class<?> GetCurrentWeatherReceiverActivity();

    void SetForecastWeatherReceiverActivity(@NonNull Class<?> forecastWeatherReceiverActivity);

    Class<?> GetForecastWeatherReceiverActivity();

    void SetChangeWallpaper(boolean changeWallpaper);

    boolean GetChangeWallpaper();

    void SetReloadEnabled(boolean reloadEnabled);

    boolean GetReloadEnabled();

    void SetReloadTimeout(int reloadTimeout);

    int GetReloadTimeout();

    IWeatherModel GetCurrentWeather();

    IForecastModel GetForecastWeather();

    IForecastModel SearchForecastModel(@NonNull String searchKey);

    void LoadCurrentWeather();

    void LoadForecastWeather();

    Calendar GetLastUpdate();
}
