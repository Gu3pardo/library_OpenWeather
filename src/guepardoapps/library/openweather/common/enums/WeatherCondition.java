package guepardoapps.library.openweather.common.enums;

import java.io.Serializable;

public enum WeatherCondition implements Serializable {

	NULL("null"), 
	CLEAR("clear"), 
	RAIN("rain"), 
	DRIZZLE("drizzle"), 
	CLOUD("cloud"), 
	SNOW("snow"), 
	FOG("fog"), 
	MIST("mist"), 
	SUN("sun"), 
	HAZE("haze"), 
	SLEET("sleet");

	private String _value;

	private WeatherCondition(String value) {
		_value = value;
	}

	@Override
	public String toString() {
		return _value;
	}

	public static WeatherCondition GetByString(String value) {
		for (WeatherCondition entry : WeatherCondition.values()) {
			if (entry.toString().contains(value)) {
				return entry;
			}
		}

		return WeatherCondition.NULL;
	}
}
