package guepardoapps.library.openweather.models;

import java.io.Serializable;
import java.util.Calendar;

import guepardoapps.library.openweather.enums.ForecastDayTime;
import guepardoapps.library.openweather.enums.WeatherCondition;

@SuppressWarnings({"unused"})
public interface IForecastPartModel extends Serializable {
    enum ForecastListType {Forecast, DateDivider}

    String GetMain();

    String GetDescription();

    double GetTemperatureMin();

    double GetTemperatureMax();

    String GetTemperatureString();

    double GetHumidity();

    String GetHumidityString();

    double GetPressure();

    String GetPressureString();

    Calendar GetDateTime();

    WeatherCondition GetCondition();

    ForecastListType GetForecastListType();

    ForecastDayTime GetDayTime();
}
