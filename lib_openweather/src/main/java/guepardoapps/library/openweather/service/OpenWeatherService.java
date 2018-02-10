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
import java.util.Locale;

import guepardoapps.library.openweather.controller.*;
import guepardoapps.library.openweather.converter.*;
import guepardoapps.library.openweather.datatransferobjects.NotificationContentDto;
import guepardoapps.library.openweather.downloader.*;
import guepardoapps.library.openweather.enums.*;
import guepardoapps.library.openweather.models.*;
import guepardoapps.library.openweather.utils.Logger;

@SuppressWarnings({"WeakerAccess"})
public class OpenWeatherService implements IOpenWeatherService {
    private static final String Tag = OpenWeatherService.class.getSimpleName();

    private static final OpenWeatherService Singleton = new OpenWeatherService();

    private static final int MinTimeoutMs = 5 * 60 * 1000;
    private static final int MaxTimeoutMs = 24 * 60 * 60 * 1000;
    private static final int TimeoutMs = 15 * 60 * 1000;

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

    private Context _context;

    private IOpenWeatherDownloader _openWeatherDownloader;

    private IBroadcastController _broadcastController;
    private INetworkController _networkController;
    private INotificationController _notificationController;
    private IReceiverController _receiverController;

    private boolean _isInitialized;

    private Calendar _lastUpdate;

    private WeatherModel _currentWeather;
    private ForecastModel _forecastWeather;

    private boolean _displayCurrentWeatherNotification;
    private boolean _displayForecastWeatherNotification;
    private Class<?> _currentWeatherReceiverActivity;
    private Class<?> _forecastWeatherReceiverActivity;

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
                Logger.getInstance().Error(Tag, "_currentWeatherDownloadFinishedReceiver content is null");
                return;
            }

            OpenWeatherDownloader.WeatherDownloadType downloadType = content.CurrentWeatherDownloadType;
            if (downloadType != OpenWeatherDownloader.WeatherDownloadType.CurrentWeather) {
                return;
            }

            String result = content.Result;
            if (result == null || result.length() == 0) {
                Logger.getInstance().Error(Tag, "Result is null!");
                sendFailedCurrentWeatherBroadcast("Result is null!");
                return;
            }

            boolean success = content.Success;
            if (!success) {
                Logger.getInstance().Error(Tag, result);
                sendFailedCurrentWeatherBroadcast(result);
                return;
            }

            WeatherModel currentWeather = JsonToWeatherConverter.ConvertFromJsonToCurrentModel(result);
            if (currentWeather == null) {
                Logger.getInstance().Error(Tag, "Converted currentWeather is null!");
                sendFailedCurrentWeatherBroadcast("Converted currentWeather is null!");
                return;
            }

            _lastUpdate = Calendar.getInstance();

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
                Logger.getInstance().Error(Tag, "_forecastWeatherDownloadFinishedReceiver content is null");
                return;
            }

            OpenWeatherDownloader.WeatherDownloadType downloadType = content.CurrentWeatherDownloadType;
            if (downloadType != OpenWeatherDownloader.WeatherDownloadType.ForecastWeather) {
                return;
            }

            String result = content.Result;
            if (result == null || result.length() == 0) {
                Logger.getInstance().Error(Tag, "Result is null!");
                sendFailedForecastWeatherBroadcast("Result is null!");
                return;
            }

            boolean succcess = content.Success;
            if (!succcess) {
                Logger.getInstance().Error(Tag, result);
                sendFailedCurrentWeatherBroadcast(result);
                return;
            }

            ForecastModel forecastWeather = JsonToWeatherConverter.ConvertFromJsonToForecastModel(result);
            if (forecastWeather == null) {
                Logger.getInstance().Error(Tag, "Converted forecastWeather is null!");
                sendFailedForecastWeatherBroadcast("Converted forecastWeather is null!");
                return;
            }

            _lastUpdate = Calendar.getInstance();

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
        return Singleton;
    }

    @Override
    public void Initialize(@NonNull Context context, @NonNull String city, @NonNull String apiKey, boolean displayCurrentWeatherNotification, boolean displayForecastWeatherNotification, Class<?> currentWeatherReceiverActivity, Class<?> forecastWeatherReceiverActivity, boolean changeWallpaper, boolean reloadEnabled, int reloadTimeout) {
        if (_isInitialized) {
            Logger.getInstance().Warning(Tag, "Already initialized!");
            return;
        }

        _lastUpdate = Calendar.getInstance();

        _displayCurrentWeatherNotification = displayCurrentWeatherNotification;
        _displayForecastWeatherNotification = displayForecastWeatherNotification;
        _currentWeatherReceiverActivity = currentWeatherReceiverActivity;
        _forecastWeatherReceiverActivity = forecastWeatherReceiverActivity;

        _changeWallpaper = changeWallpaper;
        _reloadEnabled = reloadEnabled;

        _context = context;

        _openWeatherDownloader = new OpenWeatherDownloader(_context, city, apiKey);

        _broadcastController = new BroadcastController(_context);
        _networkController = new NetworkController(_context);
        _notificationController = new NotificationController(_context);
        _receiverController = new ReceiverController(_context);

        _receiverController.RegisterReceiver(_currentWeatherDownloadFinishedReceiver, new String[]{OpenWeatherDownloader.DownloadFinishedBroadcast});
        _receiverController.RegisterReceiver(_forecastWeatherDownloadFinishedReceiver, new String[]{OpenWeatherDownloader.DownloadFinishedBroadcast});

        SetReloadTimeout(reloadTimeout);

        _isInitialized = true;
    }

    @Override
    public void Initialize(@NonNull Context context, @NonNull String city, @NonNull String apiKey, boolean displayCurrentWeatherNotification, boolean displayForecastWeatherNotification, boolean changeWallpaper, boolean reloadEnabled, int reloadTimeout) {
        Initialize(context, city, apiKey, displayCurrentWeatherNotification, displayForecastWeatherNotification, null, null, changeWallpaper, reloadEnabled, reloadTimeout);
    }

    @Override
    public void Initialize(@NonNull Context context, @NonNull String city, @NonNull String apiKey) {
        Initialize(context, city, apiKey, false, false, false, false, TimeoutMs);
    }

    @Override
    public void Dispose() {
        _receiverController.Dispose();
        _reloadHandler.removeCallbacks(_reloadRunnable);
        _isInitialized = false;
    }

    @Override
    public void SetCity(@NonNull String city) {
        _openWeatherDownloader.SetCity(city);
        LoadCurrentWeather();
        LoadForecastWeather();
    }

    @Override
    public String GetCity() {
        return _openWeatherDownloader.GetCity();
    }

    @Override
    public void SetApiKey(@NonNull String apiKey) {
        _openWeatherDownloader.SetApiKey(apiKey);
    }

    @Override
    public String GetApiKey() {
        return _openWeatherDownloader.GetApiKey();
    }

    @Override
    public void SetDisplayCurrentWeatherNotification(boolean displayCurrentWeatherNotification) {
        _displayCurrentWeatherNotification = displayCurrentWeatherNotification;
        if (!_displayCurrentWeatherNotification) {
            _notificationController.CloseNotification(NotificationController.NotificationIdCurrentWeather);
        } else {
            displayCurrentWeatherNotification();
        }
    }

    @Override
    public boolean GetDisplayCurrentWeatherNotification() {
        return _displayCurrentWeatherNotification;
    }

    @Override
    public void SetDisplayForecastWeatherNotification(boolean displayForecastWeatherNotification) {
        _displayForecastWeatherNotification = displayForecastWeatherNotification;
        if (!_displayForecastWeatherNotification) {
            _notificationController.CloseNotification(NotificationController.NotificationIdForecastWeather);
        } else {
            displayForecastWeatherNotification();
        }
    }

    @Override
    public boolean GetDisplayForecastWeatherNotification() {
        return _displayForecastWeatherNotification;
    }

    @Override
    public void SetCurrentWeatherReceiverActivity(@NonNull Class<?> currentWeatherReceiverActivity) {
        _currentWeatherReceiverActivity = currentWeatherReceiverActivity;
    }

    @Override
    public Class<?> GetCurrentWeatherReceiverActivity() {
        return _currentWeatherReceiverActivity;
    }

    @Override
    public void SetForecastWeatherReceiverActivity(@NonNull Class<?> forecastWeatherReceiverActivity) {
        _forecastWeatherReceiverActivity = forecastWeatherReceiverActivity;
    }

    @Override
    public Class<?> GetForecastWeatherReceiverActivity() {
        return _forecastWeatherReceiverActivity;
    }

    @Override
    public void SetChangeWallpaper(boolean changeWallpaper) {
        _changeWallpaper = changeWallpaper;
        if (_changeWallpaper) {
            changeWallpaper();
        }
    }

    @Override
    public boolean GetChangeWallpaper() {
        return _changeWallpaper;
    }

    @Override
    public void SetReloadEnabled(boolean reloadEnabled) {
        _reloadEnabled = reloadEnabled;
        if (_reloadEnabled && _networkController.IsNetworkAvailable()) {
            _reloadHandler.removeCallbacks(_reloadRunnable);
            _reloadHandler.postDelayed(_reloadRunnable, _reloadTimeout);
        }
    }

    @Override
    public boolean GetReloadEnabled() {
        return _reloadEnabled;
    }

    @Override
    public void SetReloadTimeout(int reloadTimeout) {
        if (reloadTimeout < MinTimeoutMs) {
            reloadTimeout = MinTimeoutMs;
        }
        if (reloadTimeout > MaxTimeoutMs) {
            reloadTimeout = MaxTimeoutMs;
        }

        _reloadTimeout = reloadTimeout;
        if (_reloadEnabled) {
            _reloadHandler.removeCallbacks(_reloadRunnable);
            _reloadHandler.postDelayed(_reloadRunnable, _reloadTimeout);
        }
    }

    @Override
    public int GetReloadTimeout() {
        return _reloadTimeout;
    }

    @Override
    public IWeatherModel GetCurrentWeather() {
        if (_currentWeather == null) {
            return new WeatherModel("Null", "Null", "Null", -273.15, -1, -1, Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), WeatherCondition.Null);
        }
        return _currentWeather;
    }

    @Override
    public IForecastModel GetForecastWeather() {
        return _forecastWeather;
    }

    @Override
    public IForecastModel SearchForecastModel(@NonNull String searchKey) {
        ArrayList<ForecastPartModel> foundEntryList = new ArrayList<>();

        for (int index = 0; index < _forecastWeather.GetList().size(); index++) {
            ForecastPartModel entry = _forecastWeather.GetList().get(index);

            if (searchKey.contentEquals("Today") || searchKey.contentEquals("Heute")) {
                if (entry.GetDateTime().get(Calendar.DAY_OF_MONTH) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                    foundEntryList.add(entry);
                }

            } else if (searchKey.contentEquals("Tomorrow") || searchKey.contains("Morgen")) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) + 1);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                if (entry.GetDateTime().get(Calendar.DAY_OF_MONTH) == dayOfMonth) {
                    foundEntryList.add(entry);
                }

            } else {
                if (entry.GetCondition().toString().contains(searchKey)
                        || entry.GetDateTime().toString().contains(searchKey)
                        || entry.GetDescription().contains(searchKey)
                        || entry.GetTemperatureString().contains(searchKey)
                        || entry.GetHumidityString().contains(searchKey)
                        || entry.GetPressureString().contains(searchKey)) {
                    foundEntryList.add(entry);
                }
            }
        }

        return new ForecastModel(_forecastWeather.GetCity(), foundEntryList);
    }

    @Override
    public void LoadCurrentWeather() {
        if (_openWeatherDownloader.DownloadCurrentWeather() != OpenWeatherDownloader.DownloadActionResult.Performing) {
            Logger.getInstance().Error(Tag, "Failure in LoadCurrentWeather!");
            sendFailedCurrentWeatherBroadcast("Not initialized or no city or apikey given!");
        }
    }

    @Override
    public void LoadForecastWeather() {
        if (_openWeatherDownloader.DownloadForecastWeather() != OpenWeatherDownloader.DownloadActionResult.Performing) {
            Logger.getInstance().Error(Tag, "Failure in LoadCurrentWeather!");
            sendFailedCurrentWeatherBroadcast("Not initialized or no city or apikey given!");
        }
    }

    @Override
    public Calendar GetLastUpdate() {
        return _lastUpdate;
    }

    private void displayCurrentWeatherNotification() {
        if (_currentWeather == null) {
            Logger.getInstance().Warning(Tag, "_currentWeather is null!");
            return;
        }

        WeatherCondition weatherCondition = _currentWeather.GetWeatherCondition();
        NotificationContentDto notificationContent = new NotificationContentDto(
                weatherCondition.GetDescription(),
                String.format(Locale.getDefault(), "%.1f %sC | %.2f %% | %.2f mBar", _currentWeather.GetTemperature(), ((char) 0x00B0), _currentWeather.GetHumidity(), _currentWeather.GetPressure()),
                weatherCondition.GetIcon(),
                weatherCondition.GetWallpaper());

        _notificationController.CreateNotification(NotificationController.NotificationIdCurrentWeather, _currentWeatherReceiverActivity, notificationContent);
    }

    private void displayForecastWeatherNotification() {
        if (_forecastWeather == null) {
            Logger.getInstance().Warning(Tag, "_forecastWeather is null!");
            return;
        }
        _notificationController.CreateNotification(NotificationController.NotificationIdForecastWeather, _forecastWeatherReceiverActivity, NotificationContentConverter.GetForecastWeather(_forecastWeather.GetList()));
    }

    private void changeWallpaper() {
        if (_currentWeather == null) {
            Logger.getInstance().Warning(Tag, "_currentWeather is null!");
            return;
        }

        try {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(_context.getApplicationContext());
            wallpaperManager.setResource(_currentWeather.GetWeatherCondition().GetWallpaper());
        } catch (IOException exception) {
            Logger.getInstance().Error(Tag, exception.getMessage());
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
