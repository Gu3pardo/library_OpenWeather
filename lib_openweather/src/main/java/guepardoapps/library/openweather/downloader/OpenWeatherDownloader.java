package guepardoapps.library.openweather.downloader;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.io.Serializable;

import guepardoapps.library.openweather.controller.BroadcastController;
import guepardoapps.library.openweather.utils.Logger;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@SuppressWarnings({"WeakerAccess"})
public class OpenWeatherDownloader implements IOpenWeatherDownloader {
    private static final String Tag = OpenWeatherDownloader.class.getSimpleName();

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

    private static final String CallCurrentWeather = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&APPID=%s";
    private static final String CallForecastWeather = "http://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric&APPID=%s";

    private BroadcastController _broadcastController;

    private String _city;
    private String _apiKey;

    public OpenWeatherDownloader(@NonNull Context context, @NonNull String city, @NonNull String apiKey) {
        _broadcastController = new BroadcastController(context);
        _city = city;
        _apiKey = apiKey;
    }

    @Override
    public void SetCity(@NonNull String city) {
        _city = city;
    }

    @Override
    public String GetCity() {
        return _city;
    }

    @Override
    public void SetApiKey(@NonNull String apiKey) {
        _apiKey = apiKey;
    }

    @Override
    public String GetApiKey() {
        return _apiKey;
    }

    @Override
    public DownloadActionResult DownloadCurrentWeather() {
        if (_city == null || _city.length() == 0) {
            Logger.getInstance().Warning(Tag, "You have to set the city before calling the weather!");
            _broadcastController.SendSerializableBroadcast(
                    DownloadFinishedBroadcast,
                    DownloadFinishedBundle,
                    new DownloadFinishedBroadcastContent(
                            false,
                            WeatherDownloadType.CurrentWeather,
                            "Please set the city before calling the weather!"));
            return DownloadActionResult.InvalidCity;
        }

        if (_apiKey == null || _apiKey.length() == 0) {
            Logger.getInstance().Error(Tag, "Please enter a valid  key");
            _broadcastController.SendSerializableBroadcast(
                    DownloadFinishedBroadcast,
                    DownloadFinishedBundle,
                    new DownloadFinishedBroadcastContent(
                            false,
                            WeatherDownloadType.CurrentWeather,
                            "Please enter a valid key"));
            return DownloadActionResult.InvalidApiKey;
        }

        CallWeatherTask task = new CallWeatherTask();
        task.CurrentWeatherDownloadType = WeatherDownloadType.CurrentWeather;
        task.execute(String.format(CallCurrentWeather, _city, _apiKey));

        return DownloadActionResult.Performing;
    }

    @Override
    public DownloadActionResult DownloadForecastWeather() {
        if (_city == null || _city.length() == 0) {
            Logger.getInstance().Warning(Tag, "Please set the city before calling the weather!");
            _broadcastController.SendSerializableBroadcast(
                    DownloadFinishedBroadcast,
                    DownloadFinishedBundle,
                    new DownloadFinishedBroadcastContent(
                            false,
                            WeatherDownloadType.ForecastWeather,
                            "Please set the city before calling the weather!"));
            return DownloadActionResult.InvalidCity;
        }

        if (_apiKey == null || _apiKey.length() == 0 || _apiKey.equals("ENTER_YOUR_KEY_HERE")) {
            Logger.getInstance().Error(Tag, "Please enter a valid  key");
            _broadcastController.SendSerializableBroadcast(
                    DownloadFinishedBroadcast,
                    DownloadFinishedBundle,
                    new DownloadFinishedBroadcastContent(
                            false,
                            WeatherDownloadType.ForecastWeather,
                            "Please enter a valid key"));
            return DownloadActionResult.InvalidApiKey;
        }

        CallWeatherTask task = new CallWeatherTask();
        task.CurrentWeatherDownloadType = WeatherDownloadType.ForecastWeather;
        task.execute(String.format(CallForecastWeather, _city, _apiKey));

        return DownloadActionResult.Performing;
    }

    private class CallWeatherTask extends AsyncTask<String, Void, String> {
        WeatherDownloadType CurrentWeatherDownloadType;

        @Override
        protected String doInBackground(String... requestUrls) {
            for (String requestUrl : requestUrls) {
                String result = "";
                boolean success = false;
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder().url(requestUrl).build();
                    Response response = okHttpClient.newCall(request).execute();
                    ResponseBody responseBody = response.body();

                    if (responseBody != null) {
                        result = responseBody.string();
                        success = true;
                        Logger.getInstance().Debug(Tag, result);
                    } else {
                        Logger.getInstance().Error(Tag, "ResponseBody is null!");
                    }
                } catch (Exception exception) {
                    Logger.getInstance().Error(Tag, exception.toString());
                } finally {
                    _broadcastController.SendSerializableBroadcast(
                            DownloadFinishedBroadcast,
                            DownloadFinishedBundle,
                            new DownloadFinishedBroadcastContent(
                                    success,
                                    CurrentWeatherDownloadType,
                                    result));
                }
            }
            return "";
        }
    }
}
