package guepardoapps.library.openweather.downloader;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.io.Serializable;

import guepardoapps.library.openweather.common.utils.Logger;
import guepardoapps.library.openweather.controller.BroadcastController;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@SuppressWarnings({"unused", "WeakerAccess"})
public class OpenWeatherDownloader {
    public enum WeatherDownloadType implements Serializable {CurrentWeather, ForecastWeather}

    public static class DownloadFinishedBroadcastContent implements Serializable {
        public boolean Success;
        public WeatherDownloadType CurrentWeatherDownloadType;
        public String Result;

        public DownloadFinishedBroadcastContent(boolean success, @NonNull WeatherDownloadType currentWeatherDownloadType, @NonNull String result) {
            Success = success;
            CurrentWeatherDownloadType = currentWeatherDownloadType;
            Result = result;
        }
    }

    public enum DownloadActionResult {INVALID_CITY, INVALID_API_KEY, PERFORMING}

    public static final String DownloadFinishedBroadcast = "guepardoapps.library.openweather.downloader.broadcast.finished";
    public static final String DownloadFinishedBundle = "DownloadFinishedBundle ";

    private static final String CALL_FORECAST_WEATHER = "http://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric&APPID=%s";
    private static final String CALL_CURRENT_WEATHER = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&APPID=%s";

    private static final String TAG = OpenWeatherDownloader.class.getSimpleName();

    private BroadcastController _broadcastController;
    private OkHttpClient _okHttpClient;

    private String _city;
    private String _apiKey = "";

    public OpenWeatherDownloader(
            @NonNull Context context,
            @NonNull String city,
            @NonNull String apiKey) {
        _broadcastController = new BroadcastController(context);
        _okHttpClient = new OkHttpClient();
        _city = city;
        _apiKey = apiKey;
    }

    public void SetCity(@NonNull String city) {
        _city = city;
    }

    public String GetCity() {
        return _city;
    }

    public void SetApiKey(@NonNull String apiKey) {
        _apiKey = apiKey;
    }

    public String GetApiKey() {
        return _apiKey;
    }

    public DownloadActionResult DownloadCurrentWeatherJson() {
        if (_city == null || _city.length() == 0) {
            Logger.getInstance().Warning(TAG, "You have to set the city before calling the weather!");
            _broadcastController.SendSerializableBroadcast(
                    DownloadFinishedBroadcast,
                    DownloadFinishedBundle,
                    new DownloadFinishedBroadcastContent(false, WeatherDownloadType.CurrentWeather, "Please set the city before calling the weather!")
            );
            return DownloadActionResult.INVALID_CITY;
        }

        if (_apiKey == null || _apiKey.length() == 0 || _apiKey.equals("") || _apiKey.equals("ENTER_YOUR_KEY_HERE")) {
            Logger.getInstance().Error(TAG, "Please enter a valid  key");
            _broadcastController.SendSerializableBroadcast(
                    DownloadFinishedBroadcast,
                    DownloadFinishedBundle,
                    new DownloadFinishedBroadcastContent(false, WeatherDownloadType.CurrentWeather, "Please enter a valid key")
            );
            return DownloadActionResult.INVALID_API_KEY;
        }

        String action = String.format(CALL_CURRENT_WEATHER, _city, _apiKey);

        CallWeatherTask task = new CallWeatherTask();
        task.CurrentWeatherDownloadType = WeatherDownloadType.CurrentWeather;


        return DownloadActionResult.PERFORMING;
    }

    public DownloadActionResult DownloadForecastWeatherJson() {
        if (_city == null || _city.length() == 0) {
            Logger.getInstance().Warning(TAG, "Please set the city before calling the weather!");
            _broadcastController.SendSerializableBroadcast(
                    DownloadFinishedBroadcast,
                    DownloadFinishedBundle,
                    new DownloadFinishedBroadcastContent(false, WeatherDownloadType.ForecastWeather, "Please set the city before calling the weather!")
            );
            return DownloadActionResult.INVALID_CITY;
        }

        if (_apiKey == null || _apiKey.length() == 0 || _apiKey.equals("") || _apiKey.equals("ENTER_YOUR_KEY_HERE")) {
            Logger.getInstance().Error(TAG, "Please enter a valid  key");
            _broadcastController.SendSerializableBroadcast(
                    DownloadFinishedBroadcast,
                    DownloadFinishedBundle,
                    new DownloadFinishedBroadcastContent(false, WeatherDownloadType.ForecastWeather, "Please enter a valid key")
            );
            return DownloadActionResult.INVALID_API_KEY;
        }

        String action = String.format(CALL_FORECAST_WEATHER, _city, _apiKey);

        CallWeatherTask task = new CallWeatherTask();
        task.CurrentWeatherDownloadType = WeatherDownloadType.ForecastWeather;
        task.execute(action);

        return DownloadActionResult.PERFORMING;
    }

    private class CallWeatherTask extends AsyncTask<String, Void, String> {
        WeatherDownloadType CurrentWeatherDownloadType;

        @Override
        protected String doInBackground(String... requestUrls) {
            for (String requestUrl : requestUrls) {
                String result = "";
                boolean success = false;
                try {
                    Request request = new Request.Builder().url(requestUrl).build();
                    Response response = _okHttpClient.newCall(request).execute();
                    ResponseBody responseBody = response.body();

                    if (responseBody != null) {
                        result = responseBody.string();
                        success = true;
                        Logger.getInstance().Debug(TAG, result);
                    } else {
                        Logger.getInstance().Error(TAG, "ResponseBody is null!");
                    }
                } catch (Exception exception) {
                    Logger.getInstance().Error(TAG, exception.toString());
                } finally {
                    _broadcastController.SendSerializableBroadcast(
                            DownloadFinishedBroadcast,
                            DownloadFinishedBundle,
                            new DownloadFinishedBroadcastContent(success, CurrentWeatherDownloadType, result)
                    );
                }
            }
            return "";
        }
    }
}
