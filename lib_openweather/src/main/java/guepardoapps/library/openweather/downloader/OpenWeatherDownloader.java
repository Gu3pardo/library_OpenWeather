package guepardoapps.library.openweather.downloader;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import guepardoapps.library.openweather.common.OWAction;
import guepardoapps.library.openweather.common.OWBroadcasts;
import guepardoapps.library.openweather.common.OWBundles;
import guepardoapps.library.openweather.common.utils.Logger;
import guepardoapps.library.openweather.controller.BroadcastController;

public class OpenWeatherDownloader {
    public enum WeatherDownloadType {CurrentWeather, ForecastWeather}

    private static final String TAG = OpenWeatherDownloader.class.getSimpleName();

    private BroadcastController _broadcastController;

    private String _city;

    public OpenWeatherDownloader(
            @NonNull Context context,
            @NonNull String city) {
        _broadcastController = new BroadcastController(context);
        _city = city;
    }

    public String GetCity() {
        return _city;
    }

    public void SetCity(@NonNull String city) {
        _city = city;
    }

    public void DownloadCurrentWeatherJson() {
        if (_city == null || _city.length() == 0) {
            Logger.getInstance().Warning(TAG, "You have to set the city before calling the weather!");
            return;
        }

        String action = String.format(OWAction.CALL_CURRENT_WEATHER, _city);

        CallWeatherTask task = new CallWeatherTask();
        task.CurrentWeatherDownloadType = WeatherDownloadType.CurrentWeather;
        task.execute(action);
    }

    public void DownloadForecastWeatherJson() {
        if (_city == null || _city.length() == 0) {
            Logger.getInstance().Warning(TAG, "You have to set the city before calling the weather!");
            return;
        }

        String action = String.format(OWAction.CALL_FORECAST_WEATHER, _city);

        CallWeatherTask task = new CallWeatherTask();
        task.CurrentWeatherDownloadType = WeatherDownloadType.ForecastWeather;
        task.execute(action);
    }

    private class CallWeatherTask extends AsyncTask<String, Void, String> {
        WeatherDownloadType CurrentWeatherDownloadType;

        @Override
        protected String doInBackground(String... actions) {
            String response;
            StringBuilder json = new StringBuilder(2048);

            for (String action : actions) {
                try {
                    URL url = new URL(action);
                    URLConnection urlConnection = url.openConnection();

                    InputStream inputStream = urlConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    while ((response = bufferedReader.readLine()) != null) {
                        json.append(response).append("\n");
                    }

                    bufferedReader.close();
                    inputStreamReader.close();
                    inputStream.close();

                } catch (Exception exception) {
                    Logger.getInstance().Error(TAG, exception.getMessage());
                }
            }

            return json.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            _broadcastController.SendStringArrayBroadcast(
                    OWBroadcasts.WEATHER_DOWNLOAD_FINISHED,
                    new String[]{OWBundles.WEATHER_DOWNLOAD, OWBundles.DOWNLOAD_TYPE},
                    new String[]{result, CurrentWeatherDownloadType.toString()}
            );
        }
    }
}
