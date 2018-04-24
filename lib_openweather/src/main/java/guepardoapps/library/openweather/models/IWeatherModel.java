package guepardoapps.library.openweather.models;

import java.io.Serializable;
import java.util.Calendar;

import guepardoapps.library.openweather.enums.WeatherCondition;

@SuppressWarnings({"unused"})
public interface IWeatherModel extends Serializable {
    String GetCity();

    String GetCountry();

    String GetDescription();

    double GetTemperature();

    double GetHumidity();

    double GetPressure();

    Calendar GetSunriseTime();

    Calendar GetSunsetTime();

    Calendar GetLastUpdate();

    WeatherCondition GetWeatherCondition();
}
