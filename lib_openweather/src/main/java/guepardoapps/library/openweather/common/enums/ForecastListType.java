package guepardoapps.library.openweather.common.enums;

import java.io.Serializable;

public enum ForecastListType implements Serializable {
	
	FORECAST("forecast"),
	DATE_DIVIDER("date_divider");

	private String _value;

	ForecastListType(String value) {
		_value = value;
	}

	@Override
	public String toString() {
		return _value;
	}
}
