package guepardoapps.library.openweather.models;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Locale;

import guepardoapps.library.openweather.enums.ForecastDayTime;
import guepardoapps.library.openweather.enums.WeatherCondition;

public class ForecastPartModel implements IForecastPartModel {
    private static final String Tag = ForecastPartModel.class.getSimpleName();

    private String _main;
    private String _description;
    private double _temperatureMin;
    private double _temperatureMax;
    private double _pressure;
    private double _humidity;
    private Calendar _dateTime;
    private WeatherCondition _weatherCondition;
    private ForecastListType _forecastListType;

    public ForecastPartModel(@NonNull String main, @NonNull String description, double temperatureMin, double temperatureMax, double pressure, double humidity, @NonNull Calendar dateTime, @NonNull WeatherCondition weatherCondition) {
        _main = main;
        _description = description;
        _temperatureMin = temperatureMin;
        _temperatureMax = temperatureMax;
        _humidity = humidity;
        _pressure = pressure;
        _dateTime = dateTime;
        _weatherCondition = weatherCondition;
        _forecastListType = ForecastListType.Forecast;
    }

    public ForecastPartModel(@NonNull Calendar dateTime) {
        this("", "", 0, 0, 0, 0, dateTime, WeatherCondition.Null);
        _forecastListType = ForecastListType.DateDivider;
    }

    @Override
    public String GetMain() {
        return _main;
    }

    @Override
    public String GetDescription() {
        return _description;
    }

    @Override
    public double GetTemperatureMin() {
        return _temperatureMin;
    }

    @Override
    public double GetTemperatureMax() {
        return _temperatureMax;
    }

    @Override
    public String GetTemperatureString() {
        return String.format(Locale.getDefault(), "%.2f - %.2f %sC", _temperatureMin, _temperatureMax, ((char) 0x00B0));
    }

    @Override
    public double GetHumidity() {
        return _humidity;
    }

    @Override
    public String GetHumidityString() {
        return String.format(Locale.getDefault(), "%.2f%%", _humidity);
    }

    @Override
    public double GetPressure() {
        return _pressure;
    }

    @Override
    public String GetPressureString() {
        return String.format(Locale.getDefault(), "%.2f%%", _pressure);
    }

    @Override
    public Calendar GetDateTime() {
        return _dateTime;
    }

    @Override
    public WeatherCondition GetCondition() {
        return _weatherCondition;
    }

    @Override
    public ForecastListType GetForecastListType() {
        return _forecastListType;
    }

    @Override
    public ForecastDayTime GetDayTime() {
        return ForecastDayTime.GetByValue(_dateTime.get(Calendar.HOUR_OF_DAY));
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "{\"Class\":\"%s\",\"Main\":\"%s\",\"Description\":\"%s\",\"TemperatureMin\":%.2f,\"TemperatureMax\":%.2f,\"Humidity\":%.2f,\"Pressure\":%.2f,\"DateTime\":\"%s\",\"WeatherCondition\":\"%s\",\"ForecastListType\":\"%s\"}",
                Tag, _main, _description, _temperatureMin, _temperatureMax, _humidity, _pressure, _dateTime, _weatherCondition, _forecastListType);
    }
}