package guepardoapps.library.openweather.enums;

import java.io.Serializable;

import guepardoapps.library.openweather.common.OWDefinitions;

public enum ForecastDayTime implements Serializable {

	NULL(-1), 
	MORNING(OWDefinitions.MORNING_HOUR), 
	MIDDAY(OWDefinitions.MIDDAY_HOUR), 
	EVENING(OWDefinitions.EVENING_HOUR), 
	NIGHT(OWDefinitions.NIGHT_HOUR);

	private int _value;

	ForecastDayTime(int value) {
		_value = value;
	}

	public int GetValue() {
		return _value;
	}

	@Override
	public String toString() {
		return String.valueOf(_value);
	}

	public static ForecastDayTime GetByValue(int value) {
		if (value > OWDefinitions.NIGHT_HOUR && value <= OWDefinitions.MORNING_HOUR) {
			return MORNING;
		} else if (value > OWDefinitions.MORNING_HOUR && value <= OWDefinitions.MIDDAY_HOUR) {
			return MIDDAY;
		} else if (value > OWDefinitions.MIDDAY_HOUR && value <= OWDefinitions.EVENING_HOUR) {
			return EVENING;
		} else if (value > OWDefinitions.EVENING_HOUR && value <= OWDefinitions.NIGHT_HOUR) {
			return NIGHT;
		} else {
			return NULL;
		}
	}
}
