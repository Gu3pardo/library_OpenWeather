package guepardoapps.library.openweather.common.model;

import java.io.Serializable;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import guepardoapps.library.openweather.common.OWDefinitions;
import guepardoapps.library.openweather.common.enums.ForecastDayTime;
import guepardoapps.library.openweather.common.enums.ForecastListType;
import guepardoapps.library.openweather.common.enums.WeatherCondition;

import guepardoapps.library.toolset.common.Logger;
import guepardoapps.library.toolset.common.StringHelper;

public class ForecastWeatherModel implements Serializable {

	private static final long serialVersionUID = -7845325750001478724L;

	private static final String TAG = ForecastWeatherModel.class.getSimpleName();
	private Logger _logger;

	private String _tempMin;
	private String _tempMax;
	private double _tempMaxDouble;
	private String _pressure;
	private String _humidity;

	private String _weatherDescription;

	private String _date;
	private String _time;

	private ForecastListType _listType;
	private WeatherCondition _condition;

	public ForecastWeatherModel(JSONObject json) {
		_logger = new Logger(TAG);
		_logger.Info(TAG + " created " + ForecastListType.FORECAST.toString());
		_logger.Info("json: " + json.toString());

		_listType = ForecastListType.FORECAST;

		try {
			_tempMin = json.getJSONObject("main").getString("temp_min") + " °C";
			_tempMax = json.getJSONObject("main").getString("temp_max") + " °C";
			_tempMaxDouble = json.getJSONObject("main").getDouble("temp_max");
			_pressure = json.getJSONObject("main").getString("pressure") + "hPa";
			_humidity = json.getJSONObject("main").getString("humidity") + "%";

			_weatherDescription = json.getJSONArray("weather").getJSONObject(0).getString("description");

			String[] dateTime = json.getString("dt_txt").split(" ");
			_date = dateTime[0];
			_time = dateTime[1];
			try {
				if (StringHelper.GetStringCount(_time, ":") > 2) {
					int endIndex = StringHelper.GetCharPositions(_time, ':')[1];
					_time = _time.substring(0, endIndex - 1);
				}
			} catch (Exception ex) {
				_logger.Error(ex.toString());
			}

			_condition = WeatherCondition.GetByString(_weatherDescription);

		} catch (JSONException e) {
			_logger.Error(e.toString());
		}
	}

	public ForecastWeatherModel(String date) {
		_logger = new Logger(TAG);
		_logger.Info(ForecastWeatherModel.class.getName() + " created " + ForecastListType.DATE_DIVIDER.toString());

		_listType = ForecastListType.DATE_DIVIDER;

		_tempMin = "-273.15 °C";
		_tempMax = "-273.15 °C";
		_tempMaxDouble = -273.15;
		_pressure = "0hPa";
		_humidity = "0%";

		_weatherDescription = "";

		_date = date;
		_time = "";

		_condition = WeatherCondition.GetByString(_weatherDescription);
	}

	public String GetTempMin() {
		return _tempMin;
	}

	public String GetTempMax() {
		return _tempMax;
	}

	public double GetTempMaxDouble() {
		return _tempMaxDouble;
	}

	public String GetPressure() {
		return _pressure;
	}

	public String GetHumidity() {
		return _humidity;
	}

	public String GetWeatherDescription() {
		return _weatherDescription;
	}

	public String GetDate() {
		return _date;
	}

	public String GetTime() {
		return _time;
	}

	public ForecastDayTime GetDayTime() {
		int hour = Integer.parseInt(_time.substring(0, 2).replace(":", ""));

		if (hour > OWDefinitions.NIGHT_HOUR && hour <= OWDefinitions.MORNING_HOUR) {
			return ForecastDayTime.MORNING;
		} else if (hour > OWDefinitions.MORNING_HOUR && hour <= OWDefinitions.MIDDAY_HOUR) {
			return ForecastDayTime.MIDDAY;
		} else if (hour > OWDefinitions.MIDDAY_HOUR && hour <= OWDefinitions.EVENING_HOUR) {
			return ForecastDayTime.EVENING;
		} else if (hour > OWDefinitions.EVENING_HOUR && hour <= OWDefinitions.NIGHT_HOUR) {
			return ForecastDayTime.NIGHT;
		} else {
			_logger.Warn("Returning ForecastDayTime.NULL!");
			return ForecastDayTime.NULL;
		}
	}

	public Calendar GetCalendarDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(_date.substring(8)));
		return calendar;
	}

	public String GetText() {
		return _time + "\n" + _date + "\nTemperature: " + _tempMin + " - " + _tempMax + "\n" + _pressure + "\n"
				+ _humidity + "\n" + _weatherDescription;
	}

	public WeatherCondition GetCondition() {
		return _condition;
	}

	public ForecastListType GetForecastListType() {
		return _listType;
	}

	@Override
	public String toString() {
		return "[" + TAG + ": Time: " + _time + ", Date: " + _date + "; WeatherDescription: " + _weatherDescription
				+ "; Temperature Min: " + _tempMin + "; Temperature Max: " + _tempMax + "; Humidity: " + _humidity
				+ "; Pressure: " + _pressure + "; ForecastListType: " + _listType.toString() + "]";
	}
}