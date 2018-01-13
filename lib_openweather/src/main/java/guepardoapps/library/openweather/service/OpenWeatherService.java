package guepardoapps.library.openweather.service;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import guepardoapps.library.openweather.common.classes.NotificationContent;
import guepardoapps.library.openweather.common.classes.SerializableTime;
import guepardoapps.library.openweather.common.utils.Logger;
import guepardoapps.library.openweather.controller.BroadcastController;
import guepardoapps.library.openweather.controller.NetworkController;
import guepardoapps.library.openweather.controller.NotificationController;
import guepardoapps.library.openweather.controller.ReceiverController;
import guepardoapps.library.openweather.converter.JsonToWeatherConverter;
import guepardoapps.library.openweather.converter.NotificationContentConverter;
import guepardoapps.library.openweather.downloader.OpenWeatherDownloader;
import guepardoapps.library.openweather.enums.WeatherCondition;
import guepardoapps.library.openweather.models.ForecastModel;
import guepardoapps.library.openweather.models.ForecastPartModel;
import guepardoapps.library.openweather.models.WeatherModel;

@SuppressWarnings({"unused", "WeakerAccess"})
public class OpenWeatherService {
    public static class CurrentWeatherDownloadFinishedContent implements Serializable {
        public WeatherModel CurrentWeather;
        public boolean Success;
        public String Response;

        public CurrentWeatherDownloadFinishedContent(WeatherModel currentWeather, boolean succcess, String response) {
            CurrentWeather = currentWeather;
            Success = succcess;
            Response = response;
        }
    }

    public static class ForecastWeatherDownloadFinishedContent implements Serializable {
        public ForecastModel ForecastWeather;
        public boolean Success;
        public String Response;

        public ForecastWeatherDownloadFinishedContent(ForecastModel forecastWeather, boolean succcess, String response) {
            ForecastWeather = forecastWeather;
            Success = succcess;
            Response = response;
        }
    }

    public static final String CurrentWeatherDownloadFinishedBroadcast = "guepardoapps.lucahome.openweather.service.weather.current.download.finished";
    public static final String CurrentWeatherDownloadFinishedBundle = "CurrentWeatherDownloadFinishedBundle";

    public static final String ForecastWeatherDownloadFinishedBroadcast = "guepardoapps.lucahome.openweather.service.weather.forecast.download.finished";
    public static final String ForecastWeatherDownloadFinishedBundle = "ForecastWeatherDownloadFinishedBundle";

    private static final OpenWeatherService SINGLETON = new OpenWeatherService();
    private boolean _isInitialized;

    private static final String TAG = OpenWeatherService.class.getSimpleName();

    private Date _lastUpdate;

    private Context _context;

    private OpenWeatherDownloader _openWeatherDownloader;

    private BroadcastController _broadcastController;
    private NetworkController _networkController;
    private NotificationController _notificationController;
    private ReceiverController _receiverController;

    private String _city;
    private WeatherModel _currentWeather;
    private ForecastModel _forecastWeather;

    private boolean _displayCurrentWeatherNotification;
    private boolean _displayForecastWeatherNotification;
    private Class<?> _currentWeatherReceiverActivity;
    private Class<?> _forecastWeatherReceiverActivity;

    private static final int MIN_TIMEOUT_MS = 5 * 60 * 1000;
    private static final int MAX_TIMEOUT_MS = 24 * 60 * 60 * 1000;
    private static final int TIMEOUT_MS = 15 * 60 * 1000;

    private boolean _reloadEnabled;
    private int _reloadTimeout;
    private Handler _reloadHandler = new Handler();
    private Runnable _reloadRunnable = new Runnable() {
        @Override
        public void run() {
            LoadCurrentWeather();
            LoadForecastWeather();
            if (_reloadEnabled && _networkController.IsNetworkAvailable()) {
                _reloadHandler.postDelayed(_reloadRunnable, _reloadTimeout);
            }
        }
    };

    private boolean _changeWallpaper;

    private BroadcastReceiver _currentWeatherDownloadFinishedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            OpenWeatherDownloader.DownloadFinishedBroadcastContent content = (OpenWeatherDownloader.DownloadFinishedBroadcastContent) intent.getSerializableExtra(OpenWeatherDownloader.DownloadFinishedBundle);
            if (content == null) {
                Logger.getInstance().Error(TAG, "_currentWeatherDownloadFinishedReceiver content is null");
                return;
            }

            OpenWeatherDownloader.WeatherDownloadType downloadType = content.CurrentWeatherDownloadType;
            if (downloadType != OpenWeatherDownloader.WeatherDownloadType.CurrentWeather) {
                return;
            }

            String result = content.Result;
            if (result == null || result.length() == 0) {
                Logger.getInstance().Error(TAG, "Result is null!");
                sendFailedCurrentWeatherBroadcast("Result is null!");
                return;
            }

            boolean succcess = content.Success;
            if (!succcess) {
                Logger.getInstance().Error(TAG, result);
                sendFailedCurrentWeatherBroadcast(result);
                return;
            }

            WeatherModel currentWeather = JsonToWeatherConverter.getInstance().ConvertFromJsonToWeatherModel(result);
            if (currentWeather == null) {
                Logger.getInstance().Error(TAG, "Converted currentWeather is null!");
                sendFailedCurrentWeatherBroadcast("Converted currentWeather is null!");
                return;
            }

            _lastUpdate = new Date();

            _currentWeather = currentWeather;

            _broadcastController.SendSerializableBroadcast(
                    CurrentWeatherDownloadFinishedBroadcast,
                    CurrentWeatherDownloadFinishedBundle,
                    new CurrentWeatherDownloadFinishedContent(_currentWeather, true, result));

            if (_displayCurrentWeatherNotification) {
                displayCurrentWeatherNotification();
            }

            if (_changeWallpaper) {
                changeWallpaper();
            }
        }
    };

    private BroadcastReceiver _forecastWeatherDownloadFinishedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            OpenWeatherDownloader.DownloadFinishedBroadcastContent content = (OpenWeatherDownloader.DownloadFinishedBroadcastContent) intent.getSerializableExtra(OpenWeatherDownloader.DownloadFinishedBundle);
            if (content == null) {
                Logger.getInstance().Error(TAG, "_forecastWeatherDownloadFinishedReceiver content is null");
                return;
            }

            OpenWeatherDownloader.WeatherDownloadType downloadType = content.CurrentWeatherDownloadType;
            if (downloadType != OpenWeatherDownloader.WeatherDownloadType.ForecastWeather) {
                return;
            }

            String result = content.Result;
            if (result == null || result.length() == 0) {
                Logger.getInstance().Error(TAG, "Result is null!");
                sendFailedForecastWeatherBroadcast("Result is null!");
                return;
            }

            boolean succcess = content.Success;
            if (!succcess) {
                Logger.getInstance().Error(TAG, result);
                sendFailedCurrentWeatherBroadcast(result);
                return;
            }

            ForecastModel forecastWeather = JsonToWeatherConverter.getInstance().ConvertFromJsonToForecastModel(result);
            if (forecastWeather == null) {
                Logger.getInstance().Error(TAG, "Converted forecastWeather is null!");
                sendFailedForecastWeatherBroadcast("Converted forecastWeather is null!");
                return;
            }

            _lastUpdate = new Date();

            _forecastWeather = forecastWeather;

            _broadcastController.SendSerializableBroadcast(
                    ForecastWeatherDownloadFinishedBroadcast,
                    ForecastWeatherDownloadFinishedBundle,
                    new ForecastWeatherDownloadFinishedContent(_forecastWeather, true, result));

            if (_displayForecastWeatherNotification) {
                displayForecastWeatherNotification();
            }
        }
    };

    private OpenWeatherService() {
    }

    public static OpenWeatherService getInstance() {
        return SINGLETON;
    }

    public void Initialize(
            @NonNull Context context,
            @NonNull String city,
            boolean displayCurrentWeatherNotification,
            boolean displayForecastWeatherNotification,
            Class<?> currentWeatherReceiverActivity,
            Class<?> forecastWeatherReceiverActivity,
            boolean changeWallpaper,
            boolean reloadEnabled,
            int reloadTimeout) {
        if (_isInitialized) {
            Logger.getInstance().Warning(TAG, "Already initialized!");
            return;
        }

        _lastUpdate = new Date();

        _city = city;
        _displayCurrentWeatherNotification = displayCurrentWeatherNotification;
        _displayForecastWeatherNotification = displayForecastWeatherNotification;
        _currentWeatherReceiverActivity = currentWeatherReceiverActivity;
        _forecastWeatherReceiverActivity = forecastWeatherReceiverActivity;

        _changeWallpaper = changeWallpaper;
        _reloadEnabled = reloadEnabled;

        _context = context;

        _openWeatherDownloader = new OpenWeatherDownloader(_context, _city);

        _broadcastController = new BroadcastController(_context);
        _networkController = new NetworkController(_context);
        _notificationController = new NotificationController(_context);
        _receiverController = new ReceiverController(_context);

        _receiverController.RegisterReceiver(_currentWeatherDownloadFinishedReceiver, new String[]{OpenWeatherDownloader.DownloadFinishedBroadcast});
        _receiverController.RegisterReceiver(_forecastWeatherDownloadFinishedReceiver, new String[]{OpenWeatherDownloader.DownloadFinishedBroadcast});

        SetReloadTimeout(reloadTimeout);

        _isInitialized = true;
    }

    public void Initialize(
            @NonNull Context context,
            @NonNull String city,
            boolean displayCurrentWeatherNotification,
            boolean displayForecastWeatherNotification,
            boolean changeWallpaper,
            boolean reloadEnabled,
            int reloadTimeout) {
        Initialize(context, city, displayCurrentWeatherNotification, displayForecastWeatherNotification, null, null, changeWallpaper, reloadEnabled, reloadTimeout);
    }

    public void Initialize(
            @NonNull Context context,
            @NonNull String city) {
        Initialize(context, city, false, false, false, false, TIMEOUT_MS);
    }

    public void Dispose() {
        _receiverController.Dispose();
        _reloadHandler.removeCallbacks(_reloadRunnable);
        _isInitialized = false;
    }

    public String GetCity() {
        return _city;
    }

    public void SetCity(@NonNull String city) {
        _city = city;
        _openWeatherDownloader.SetCity(_city);
        LoadCurrentWeather();
        LoadForecastWeather();
    }

    public boolean GetDisplayCurrentWeatherNotification() {
        return _displayCurrentWeatherNotification;
    }

    public void SetDisplayCurrentWeatherNotification(boolean displayCurrentWeatherNotification) {
        _displayCurrentWeatherNotification = displayCurrentWeatherNotification;
        if (!_displayCurrentWeatherNotification) {
            _notificationController.CloseNotification(NotificationController.CURRENT_NOTIFICATION_ID);
        } else {
            displayCurrentWeatherNotification();
        }
    }

    public boolean GetDisplayForecastWeatherNotification() {
        return _displayForecastWeatherNotification;
    }

    public void SetDisplayForecastWeatherNotification(boolean displayForecastWeatherNotification) {
        _displayForecastWeatherNotification = displayForecastWeatherNotification;
        if (!_displayForecastWeatherNotification) {
            _notificationController.CloseNotification(NotificationController.FORECAST_NOTIFICATION_ID);
        } else {
            displayForecastWeatherNotification();
        }
    }

    public Class<?> GetCurrentWeatherReceiverActivity() {
        return _currentWeatherReceiverActivity;
    }

    public void SetCurrentWeatherReceiverActivity(@NonNull Class<?> currentWeatherReceiverActivity) {
        _currentWeatherReceiverActivity = currentWeatherReceiverActivity;
    }

    public Class<?> GetForecastWeatherReceiverActivity() {
        return _forecastWeatherReceiverActivity;
    }

    public void SetForecastWeatherReceiverActivity(@NonNull Class<?> forecastWeatherReceiverActivity) {
        _forecastWeatherReceiverActivity = forecastWeatherReceiverActivity;
    }

    public boolean GetChangeWallpaper() {
        return _changeWallpaper;
    }

    public void SetChangeWallpaper(boolean changeWallpaper) {
        _changeWallpaper = changeWallpaper;
        if (_changeWallpaper) {
            changeWallpaper();
        }
    }

    public boolean GetReloadEnabled() {
        return _reloadEnabled;
    }

    public void SetReloadEnabled(boolean reloadEnabled) {
        _reloadEnabled = reloadEnabled;
        if (_reloadEnabled && _networkController.IsNetworkAvailable()) {
            _reloadHandler.removeCallbacks(_reloadRunnable);
            _reloadHandler.postDelayed(_reloadRunnable, _reloadTimeout);
        }
    }

    public int GetReloadTimeout() {
        return _reloadTimeout;
    }

    public void SetReloadTimeout(int reloadTimeout) {
        if (reloadTimeout < MIN_TIMEOUT_MS) {
            reloadTimeout = MIN_TIMEOUT_MS;
        }
        if (reloadTimeout > MAX_TIMEOUT_MS) {
            reloadTimeout = MAX_TIMEOUT_MS;
        }

        _reloadTimeout = reloadTimeout;
        if (_reloadEnabled) {
            _reloadHandler.removeCallbacks(_reloadRunnable);
            _reloadHandler.postDelayed(_reloadRunnable, _reloadTimeout);
        }
    }

    public WeatherModel CurrentWeather() {
        if (_currentWeather == null) {
            return new WeatherModel(
                    "Null", "Null", "Null",
                    -273.15, -1, -1,
                    new SerializableTime(), new SerializableTime(), new SerializableTime(),
                    WeatherCondition.NULL);
        }

        return _currentWeather;
    }

    public ForecastModel ForecastWeather() {
        return _forecastWeather;
    }

    public ForecastModel FoundForecastItem(@NonNull String searchKey) {
        List<ForecastPartModel> foundEntries = new ArrayList<>();

        for (int index = 0; index < _forecastWeather.GetList().size(); index++) {
            ForecastPartModel entry = _forecastWeather.GetList().get(index);

            if (searchKey.contentEquals("Today") || searchKey.contentEquals("Heute")) {
                Calendar today = Calendar.getInstance();
                int dayOfMonth = today.get(Calendar.DAY_OF_MONTH);
                if (entry.GetDate().endsWith(String.valueOf(dayOfMonth))) {
                    foundEntries.add(entry);
                }

            } else if (searchKey.contentEquals("Tomorrow") || searchKey.contains("Morgen")) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) + 1);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                if (entry.GetDate().endsWith(String.valueOf(dayOfMonth))) {
                    foundEntries.add(entry);
                }

            } else {
                if (entry.GetCondition().toString().contains(searchKey)
                        || entry.GetDate().contains(searchKey)
                        || entry.GetTime().contains(searchKey)
                        || entry.GetDescription().contains(searchKey)
                        || entry.GetTemperatureString().contains(searchKey)
                        || entry.GetHumidityString().contains(searchKey)
                        || entry.GetPressureString().contains(searchKey)) {
                    foundEntries.add(entry);
                }
            }
        }

        return new ForecastModel(_forecastWeather.GetCity(), _forecastWeather.GetCountry(), foundEntries);
    }

    public void LoadCurrentWeather() {
        if (!_isInitialized || _city == null || _city.length() == 0) {
            Logger.getInstance().Error(TAG, "Failure in LoadCurrentWeather!");
            sendFailedCurrentWeatherBroadcast("Not initialized or no city given!");
            return;
        }
        _openWeatherDownloader.DownloadCurrentWeatherJson();
    }

    public void LoadForecastWeather() {
        if (!_isInitialized || _city == null || _city.length() == 0) {
            Logger.getInstance().Error(TAG, "Failure in LoadForecastWeather!");
            sendFailedForecastWeatherBroadcast("Not initialized or no city given!");
            return;
        }
        _openWeatherDownloader.DownloadForecastWeatherJson();
    }

    public Date GetLastUpdate() {
        return _lastUpdate;
    }

    private void displayCurrentWeatherNotification() {
        if (_currentWeather == null) {
            Logger.getInstance().Warning(TAG, "_currentWeather is null!");
            return;
        }

        NotificationContent notificationContent = new NotificationContent(
                _currentWeather.GetCondition().GetDescription(),
                String.format(Locale.getDefault(), "%.1f °C | %.2f %% | %.2f mBar", _currentWeather.GetTemperature(), _currentWeather.GetHumidity(), _currentWeather.GetPressure()),
                _currentWeather.GetCondition().GetIcon(),
                _currentWeather.GetCondition().GetWallpaper());

        _notificationController.CreateNotification(
                NotificationController.CURRENT_NOTIFICATION_ID,
                _currentWeatherReceiverActivity,
                notificationContent);
    }

    private void displayForecastWeatherNotification() {
        if (_forecastWeather == null) {
            Logger.getInstance().Warning(TAG, "_forecastWeather is null!");
            return;
        }

        NotificationContent notificationContent = NotificationContentConverter.getInstance().TellForecastWeather(_forecastWeather.GetList());

        _notificationController.CreateNotification(
                NotificationController.FORECAST_NOTIFICATION_ID,
                _forecastWeatherReceiverActivity,
                notificationContent);
    }

    private void changeWallpaper() {
        if (_currentWeather == null) {
            Logger.getInstance().Warning(TAG, "_currentWeather is null!");
            return;
        }

        WallpaperManager wallpaperManager = WallpaperManager.getInstance(_context.getApplicationContext());

        try {
            wallpaperManager.setResource(_currentWeather.GetCondition().GetWallpaper());
        } catch (IOException exception) {
            Logger.getInstance().Error(TAG, exception.getMessage());
        }
    }

    private void sendFailedCurrentWeatherBroadcast(@NonNull String response) {
        _broadcastController.SendSerializableBroadcast(
                CurrentWeatherDownloadFinishedBroadcast,
                CurrentWeatherDownloadFinishedBundle,
                new CurrentWeatherDownloadFinishedContent(null, false, response));
    }

    private void sendFailedForecastWeatherBroadcast(@NonNull String response) {
        _broadcastController.SendSerializableBroadcast(
                ForecastWeatherDownloadFinishedBroadcast,
                ForecastWeatherDownloadFinishedBundle,
                new ForecastWeatherDownloadFinishedContent(null, false, response));
    }
}
