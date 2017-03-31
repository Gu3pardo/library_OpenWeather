package guepardoapps.library.openweather.common.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import guepardoapps.library.toolset.common.Logger;
import guepardoapps.library.toolset.common.classes.SerializableTime;

public class WeatherModel implements Serializable {

	private static final long serialVersionUID = 5604464899374651609L;

	private static final String TAG = WeatherModel.class.getSimpleName();
	private Logger _logger;

	private String _city;
	private String _country;

	private double _temperature;
	private String _temperatureString;

	private String _description;
	private String _humidity;
	private String _pressure;

	private SerializableTime _sunriseTime;
	private SerializableTime _sunsetTime;

	private SerializableTime _lastUpdate;

	@SuppressWarnings("deprecation")
	public WeatherModel(JSONObject json, SerializableTime lastUpdate) {
		_logger = new Logger(TAG);
		_logger.Debug(TAG + " created...");
		_logger.Debug("json: " + json.toString());
		_logger.Debug("lastUpdate: " + lastUpdate.toString());

		try {
			_city = json.getString("name").toUpperCase(Locale.GERMANY);
			_country = json.getJSONObject("sys").getString("country");

			JSONObject details = json.getJSONArray("weather").getJSONObject(0);
			JSONObject main = json.getJSONObject("main");

			_description = details.getString("description").toUpperCase(Locale.GERMANY);

			_temperature = main.getDouble("temp");
			_temperatureString = String.format("%.2f", _temperature) + "°C";

			_humidity = main.getString("humidity") + "%";
			_pressure = main.getString("pressure") + "hPa";

			JSONObject sys = json.getJSONObject("sys");
			_logger.Debug(sys.toString());

			long sunriseLong = sys.getLong("sunrise") * 1000;
			_logger.Debug("sunriseLong is " + String.valueOf(sunriseLong));
			Date sunriseDate = new Date(sunriseLong);
			_logger.Debug("sunriseDate is " + sunriseDate.toString());
			Time sunrisetTime = new Time(sunriseDate.getTime());
			_logger.Debug("sunsetTime is " + sunrisetTime.toString());
			_sunriseTime = new SerializableTime(sunrisetTime.getHours(), sunrisetTime.getMinutes(),
					sunrisetTime.getSeconds(), 0);

			long sunsetLong = sys.getLong("sunset") * 1000;
			_logger.Debug("sunsetLong is " + String.valueOf(sunsetLong));
			Date sunsetDate = new Date(sunsetLong);
			_logger.Debug("sunsetDate is " + sunsetDate.toString());
			Time sunsetTime = new Time(sunsetDate.getTime());
			_logger.Debug("sunsetTime is " + sunsetTime.toString());
			_sunsetTime = new SerializableTime(sunsetTime.getHours(), sunsetTime.getMinutes(), sunsetTime.getSeconds(),
					0);
		} catch (JSONException e) {
			_logger.Error(e.toString());
		}

		_lastUpdate = lastUpdate;
		_logger.Debug(String.format("Created new %s: %s", TAG, toString()));
	}

	public String GetCity() {
		return _city;
	}

	public String GetCountry() {
		return _country;
	}

	public String GetDescription() {
		return _description;
	}

	public double GetTemperature() {
		return _temperature;
	}

	public String GetTemperatureString() {
		return _temperatureString;
	}

	public String GetHumidity() {
		return _humidity;
	}

	public String GetPressure() {
		return _pressure;
	}

	public SerializableTime GetSunriseTime() {
		return _sunriseTime;
	}

	public SerializableTime GetSunsetTime() {
		return _sunsetTime;
	}

	public SerializableTime GetLastUpdate() {
		return _lastUpdate;
	}

	public String GetBasicText() {
		return _city + "\nTemperature: " + _temperatureString + "\n" + _description;
	}

	public String GetExtendedText() {
		return _city + ", " + _country + "\nTemperature: " + _temperatureString + "\n" + _description + "\nHumidity: "
				+ _humidity + "\nPressure: " + _pressure;
	}

	@Override
	public String toString() {
		return "[WeatherModel: City: " + _city + ", Country: " + _country + "; Description: " + _description
				+ "; Temperature: " + _temperatureString + "; Humidity: " + _humidity + "; Pressure: " + _pressure
				+ "; SunriseTime: " + _sunriseTime.toString() + "; SunsetTime: " + _sunsetTime.toString()
				+ "; LastUpdate: " + _lastUpdate.toString() + "]";
	}
}