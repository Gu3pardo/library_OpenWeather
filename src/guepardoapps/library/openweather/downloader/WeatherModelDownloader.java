package guepardoapps.library.openweather.downloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import guepardoapps.library.openweather.common.OWAction;
import guepardoapps.library.openweather.common.OWBroadcasts;
import guepardoapps.library.openweather.common.OWBundles;
import guepardoapps.library.openweather.common.model.WeatherModel;

import guepardoapps.library.toolset.common.Logger;
import guepardoapps.library.toolset.common.classes.SerializableTime;

public class WeatherModelDownloader {

	private static final String TAG = WeatherModelDownloader.class.getSimpleName();
	private Logger _logger;

	private Context _context;

	private WeatherModel _weatherModel = null;
	private String _city;

	public WeatherModelDownloader(Context context, String city) {
		_logger = new Logger(TAG);
		_logger.Debug(TAG + " created...");

		_context = context;
		_city = city;

		_logger.Debug("_city: " + _city);
	}

	public void GetJson() {
		_logger.Debug("GetJson");
		CallWeatherTask task = new CallWeatherTask();
		task.execute(new String[] { "" });
	}

	private class CallWeatherTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... actions) {
			String response = "";
			for (String action : actions) {
				_logger.Debug("Action: " + action);

				try {
					URL url;
					if (_city != null) {
						url = new URL(String.format(OWAction.CALL_CURRENT_WEATHER, _city));
					} else {
						url = new URL(String.format(OWAction.CALL_CURRENT_WEATHER, "muenchen"));
					}
					_logger.Debug("Url: " + String.valueOf(url));

					URLConnection urlConnection = url.openConnection();
					_logger.Debug("Created urlConnection...");

					InputStream inputStream = urlConnection.getInputStream();
					_logger.Debug("Created inputStream...");

					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
					_logger.Debug("Created bufferedReader...");

					StringBuffer json = new StringBuffer(1024);
					while ((response = bufferedReader.readLine()) != null) {
						json.append(response).append("\n");
					}
					bufferedReader.close();

					JSONObject data = new JSONObject(json.toString());
					_logger.Debug("Data: " + data.toString());

					if (data.getInt("cod") != 200) {
						_logger.Error("Error!");
					} else {
						Calendar calendar = Calendar.getInstance();
						int hour = calendar.get(Calendar.HOUR_OF_DAY);
						int minute = calendar.get(Calendar.MINUTE);
						int second = calendar.get(Calendar.SECOND);
						SerializableTime time = new SerializableTime(hour, minute, second, 0);

						_weatherModel = new WeatherModel(data, time);
					}

				} catch (IOException e) {
					_logger.Error(e.toString());
				} catch (JSONException e) {
					_logger.Error(e.toString());
				}
			}

			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			_logger.Debug("Sending broadcast...");

			if (_weatherModel != null) {
				_logger.Debug("WeatherModel: " + _weatherModel.toString());
			}

			Intent broadcastIntent = new Intent(OWBroadcasts.CURRENT_WEATHER_JSON_FINISHED);
			broadcastIntent.putExtra(OWBundles.EXTRA_WEATHER_MODEL, _weatherModel);
			_context.sendBroadcast(broadcastIntent);
		}
	}
}
