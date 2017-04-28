package guepardoapps.library.openweather.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import guepardoapps.library.openweather.R;
import guepardoapps.library.openweather.common.enums.ForecastDayTime;
import guepardoapps.library.openweather.common.enums.WeatherCondition;

import guepardoapps.library.toolset.common.Logger;
import guepardoapps.library.toolset.common.classes.NotificationContent;

public class ForecastModel implements Serializable {

	private static final long serialVersionUID = 5940897350070310574L;

	private static final String TAG = ForecastModel.class.getSimpleName();
	private Logger _logger;

	private String _city;
	private String _country;
	private List<ForecastWeatherModel> _list = new ArrayList<ForecastWeatherModel>();

	public ForecastModel(JSONObject json) {
		_logger = new Logger(TAG);
		_logger.Info(TAG + " created...");
		_logger.Info("json: " + json.toString());

		try {
			_city = json.getJSONObject("city").getString("name").toUpperCase(Locale.GERMANY);
			_country = json.getJSONObject("city").getString("country");

			_logger.Info("_city: " + _city);
			_logger.Info("_country: " + _country);

			JSONArray dataList = json.getJSONArray("list");
			_logger.Info(dataList.toString());

			String previousDate = "";
			int forecastIndex = 0;
			for (int index = 0; index < dataList.length(); index++) {
				String currentDate = dataList.getJSONObject(index).getString("dt_txt").split(" ")[0];
				ForecastWeatherModel model = null;

				if (index == 0) {
					model = new ForecastWeatherModel(currentDate);
					_logger.Info("add model: " + model.toString());
					_list.add(forecastIndex, model);
					forecastIndex++;
				}

				if (currentDate.contains(previousDate)) {
					model = new ForecastWeatherModel(dataList.getJSONObject(index));
					_logger.Info("add model: " + model.toString());
					_list.add(forecastIndex, model);
					forecastIndex++;
				} else {
					model = new ForecastWeatherModel(currentDate);
					_logger.Info("add model: " + model.toString());
					_list.add(forecastIndex, model);
					forecastIndex++;

					model = new ForecastWeatherModel(dataList.getJSONObject(index));
					_logger.Info("add model: " + model.toString());
					_list.add(forecastIndex, model);
					forecastIndex++;
				}

				previousDate = currentDate;
			}
		} catch (JSONException e) {
			_logger.Error(e.toString());
		}
	}

	public String GetCity() {
		return _city;
	}

	public String GetCountry() {
		return _country;
	}

	public List<ForecastWeatherModel> GetList() {
		return _list;
	}

	public NotificationContent GetNextWeather() {
		List<ForecastWeatherModel> nextWeather = new ArrayList<ForecastWeatherModel>();

		for (ForecastWeatherModel entry : _list) {
			if (entry.GetDayTime() == ForecastDayTime.GetByValue(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))) {
				_logger.Debug("entry: " + entry.toString());
				nextWeather.add(entry);
			}
		}

		if (nextWeather.size() > 0) {
			return new NotificationContent("Next Weather", nextWeather.get(0).GetWeatherDescription(),
					R.drawable.weather_dummy);
		}

		return null;
	}

	public NotificationContent GetTodayWeather() {
		List<ForecastWeatherModel> todayWeather = new ArrayList<ForecastWeatherModel>();

		for (ForecastWeatherModel entry : _list) {
			if (entry.GetDay() == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
				_logger.Debug("entry: " + entry.toString());
				todayWeather.add(entry);
			}
		}

		if (todayWeather.size() > 0) {
			String weatherDescriptionToday = "";
			for (ForecastWeatherModel entry : todayWeather) {
				weatherDescriptionToday += entry.GetWeatherDescription() + ";";
			}
			return new NotificationContent("Weather today", weatherDescriptionToday, R.drawable.weather_dummy);
		}

		return null;
	}

	public NotificationContent TellForecastWeather() {
		List<ForecastWeatherModel> todayWeather = new ArrayList<ForecastWeatherModel>();
		List<ForecastWeatherModel> tomorrowWeather = new ArrayList<ForecastWeatherModel>();

		boolean isWeekend = false;
		Calendar today = Calendar.getInstance();

		for (ForecastWeatherModel entry : _list) {
			if (entry.GetDay() == today.get(Calendar.DAY_OF_MONTH)) {
				_logger.Debug("entry for today: " + entry.toString());
				todayWeather.add(entry);
			} else {
				int tomorrow = today.get(Calendar.DAY_OF_MONTH) + 1;
				_logger.Debug(String.format("tomorrow is %s", tomorrow));
				if (tomorrow > today.getActualMaximum(Calendar.DAY_OF_MONTH)) {
					tomorrow = tomorrow - today.getActualMaximum(Calendar.DAY_OF_MONTH);
					_logger.Debug(String.format("tomorrow is too big %s > %s", tomorrow,
							today.getActualMaximum(Calendar.DAY_OF_MONTH)));
				}

				if (entry.GetDay() == tomorrow) {
					_logger.Debug("entry for tomorrow: " + entry.toString());
					tomorrowWeather.add(entry);
				}
			}
		}

		if (todayWeather.size() > 2) {
			int dayOfWeek = today.get(Calendar.DAY_OF_WEEK);
			_logger.Debug(String.format("DayOfWeek is %s", dayOfWeek));

			if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) {
				isWeekend = true;
			}
			_logger.Debug(String.format("today is weekend is %s", isWeekend));

			return createNotificationContent(todayWeather, true, isWeekend);
		} else if (tomorrowWeather.size() > 2) {
			int tomorrowDayOfWeek = today.get(Calendar.DAY_OF_WEEK) + 1;
			if (tomorrowDayOfWeek > 7) {
				tomorrowDayOfWeek = 1;
			}
			_logger.Debug(String.format("tomorrowDayOfWeek is %s", tomorrowDayOfWeek));

			if (tomorrowDayOfWeek == Calendar.SUNDAY || tomorrowDayOfWeek == Calendar.SATURDAY) {
				isWeekend = true;
			}
			_logger.Debug(String.format("tomorrow is weekend is %s", isWeekend));

			return createNotificationContent(tomorrowWeather, false, isWeekend);
		}

		return null;
	}

	private NotificationContent createNotificationContent(List<ForecastWeatherModel> weatherList, boolean today,
			boolean isWeekend) {

		List<WeatherConditionModel> conditionCount = new ArrayList<WeatherConditionModel>();

		WeatherConditionModel clearsky = new WeatherConditionModel(0, "Enjoy the sunny weather today and chill!",
				"Today will be sunny! Get out for lunch!", R.drawable.weather_clear);
		WeatherConditionModel rain = new WeatherConditionModel(0, "It's a rainy day! Chill at home ;)",
				"It will rain today! Take an umbrella or take your car to work.", R.drawable.weather_rain);
		WeatherConditionModel cloud = new WeatherConditionModel(0, "Sun is hiding today.",
				"No sun today. Not bad to work...", R.drawable.weather_cloud);
		WeatherConditionModel snow = new WeatherConditionModel(0, "Today will be a snowy day!",
				"Snow today. Think twice taking your bike!", R.drawable.weather_snow);
		WeatherConditionModel fog = new WeatherConditionModel(0, "You're not gonna see your hand today! :P",
				"Find your way to work today :P", R.drawable.weather_fog);
		WeatherConditionModel sleet = new WeatherConditionModel(0, "Today will be a freezy and slittering day!",
				"Take care outside today!", R.drawable.weather_sleet);

		String notificationForecast = "";
		int notificationForecastCount = 0;
		double notificationForecastMaxTemp = -273.15;
		int notifiactionIcon = 0;

		for (ForecastWeatherModel entry : weatherList) {
			if (entry.GetCondition() == WeatherCondition.CLEAR) {
				clearsky.AddCount(1);
			} else if (entry.GetCondition() == WeatherCondition.RAIN) {
				rain.AddCount(1);
			} else if (entry.GetCondition() == WeatherCondition.CLOUD) {
				cloud.AddCount(1);
			} else if (entry.GetCondition() == WeatherCondition.SNOW) {
				snow.AddCount(1);
			} else if (entry.GetCondition() == WeatherCondition.FOG) {
				fog.AddCount(1);
			} else if (entry.GetCondition() == WeatherCondition.SLEET) {
				sleet.AddCount(1);
			}
		}

		conditionCount.add(clearsky);
		conditionCount.add(rain);
		conditionCount.add(cloud);
		conditionCount.add(snow);
		conditionCount.add(fog);
		conditionCount.add(sleet);

		for (WeatherConditionModel model : conditionCount) {
			if (model.GetCount() > notificationForecastCount) {
				if (!today) {
					model.ChangeTippsToTommorrow();
				}
				if (isWeekend) {
					notificationForecast = model.GetWeekendTipp();
				} else {
					notificationForecast = model.GetWorkdayTipp();
				}
				notificationForecastCount = model.GetCount();
				notifiactionIcon = model.GetIcon();
			}
		}

		for (ForecastWeatherModel model : weatherList) {
			if (model.GetTempMaxDouble() > notificationForecastMaxTemp) {
				notificationForecastMaxTemp = model.GetTempMaxDouble();
			}
		}

		return new NotificationContent("Hey you!",
				notificationForecast + "\nIt's getting up to " + String.valueOf(notificationForecastMaxTemp) + "°C",
				notifiactionIcon);
	}

	@Override
	public String toString() {
		String toString = "[" + TAG + ": City: " + _city + ", Country: " + _country;
		for (ForecastWeatherModel entry : _list) {
			toString += ";" + entry.toString();
		}
		toString += "]";

		return toString;
	}
}