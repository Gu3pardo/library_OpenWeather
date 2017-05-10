package guepardoapps.library.openweather.converter;

import android.annotation.SuppressLint;

import guepardoapps.library.openweather.common.enums.WeatherCondition;

import guepardoapps.library.toolset.common.Logger;

@SuppressLint("DefaultLocale")
public class WeatherConverter {

	private static final String TAG = WeatherConverter.class.getSimpleName();
	private static Logger _logger;

	public static WeatherCondition GetWeatherCondition(String description) {
		_logger = new Logger(TAG);
		_logger.Debug("Description: " + description);

		WeatherCondition condition = null;
		if (description.contains(WeatherCondition.CLEAR.toString())
				|| description.toUpperCase().contains(WeatherCondition.CLEAR.toString())
				|| description.contains(WeatherCondition.CLEAR.toString().toUpperCase())) {
			condition = WeatherCondition.CLEAR;
		} else if (description.contains(WeatherCondition.SUN.toString())
				|| description.toUpperCase().contains(WeatherCondition.SUN.toString())
				|| description.contains(WeatherCondition.SUN.toString().toUpperCase())) {
			condition = WeatherCondition.CLEAR;
		} else if (description.contains(WeatherCondition.RAIN.toString())
				|| description.toUpperCase().contains(WeatherCondition.RAIN.toString())
				|| description.contains(WeatherCondition.RAIN.toString().toUpperCase())) {
			condition = WeatherCondition.RAIN;
		} else if (description.contains(WeatherCondition.DRIZZLE.toString())
				|| description.toUpperCase().contains(WeatherCondition.DRIZZLE.toString())
				|| description.contains(WeatherCondition.DRIZZLE.toString().toUpperCase())) {
			condition = WeatherCondition.RAIN;
		} else if (description.contains(WeatherCondition.CLOUD.toString())
				|| description.toUpperCase().contains(WeatherCondition.CLOUD.toString())
				|| description.contains(WeatherCondition.CLOUD.toString().toUpperCase())) {
			condition = WeatherCondition.CLOUD;
		} else if (description.contains(WeatherCondition.SNOW.toString())
				|| description.toUpperCase().contains(WeatherCondition.SNOW.toString())
				|| description.contains(WeatherCondition.SNOW.toString().toUpperCase())) {
			condition = WeatherCondition.SNOW;
		} else if (description.contains(WeatherCondition.FOG.toString())
				|| description.toUpperCase().contains(WeatherCondition.FOG.toString())
				|| description.contains(WeatherCondition.FOG.toString().toUpperCase())) {
			condition = WeatherCondition.FOG;
		} else if (description.contains(WeatherCondition.MIST.toString())
				|| description.toUpperCase().contains(WeatherCondition.MIST.toString())
				|| description.contains(WeatherCondition.MIST.toString().toUpperCase())) {
			condition = WeatherCondition.MIST;
		} else if (description.contains(WeatherCondition.HAZE.toString())
				|| description.toUpperCase().contains(WeatherCondition.HAZE.toString())
				|| description.contains(WeatherCondition.HAZE.toString().toUpperCase())) {
			condition = WeatherCondition.HAZE;
		} else if (description.contains(WeatherCondition.SLEET.toString())
				|| description.toUpperCase().contains(WeatherCondition.SLEET.toString())
				|| description.contains(WeatherCondition.SLEET.toString().toUpperCase())) {
			condition = WeatherCondition.SLEET;
		} else {
			_logger.Warn("Description: " + description);
			condition = WeatherCondition.NULL;
		}
		_logger.Debug("WeatherCondition: " + condition.toString());

		return condition;
	}
}
