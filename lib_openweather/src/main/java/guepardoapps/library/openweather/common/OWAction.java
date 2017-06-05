package guepardoapps.library.openweather.common;

public class OWAction {
	public static final String CALL_FORECAST_WEATHER = "http://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric&APPID=" + OWKeys.OPEN_WEATHER_KEY;
	public static final String CALL_CURRENT_WEATHER = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&APPID=" + OWKeys.OPEN_WEATHER_KEY;
}
