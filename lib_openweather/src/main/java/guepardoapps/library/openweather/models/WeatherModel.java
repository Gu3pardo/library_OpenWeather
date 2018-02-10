package guepardoapps.library.openweather.models;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Locale;

import guepardoapps.library.openweather.enums.WeatherCondition;

public class WeatherModel implements IWeatherModel {
    private static final String Tag = WeatherModel.class.getSimpleName();

    private String _city;
    private String _country;
    private String _description;
    private double _temperature;
    private double _humidity;
    private double _pressure;
    private Calendar _sunrise;
    private Calendar _sunset;
    private Calendar _lastUpdate;
    private WeatherCondition _weatherCondition;

    public WeatherModel(@NonNull String city, @NonNull String country, @NonNull String description, double temperature, double humidity, double pressure, @NonNull Calendar sunrise, @NonNull Calendar sunset, @NonNull Calendar lastUpdate, @NonNull WeatherCondition weatherCondition) {
        _city = city;
        _country = country;
        _description = description;
        _temperature = temperature;
        _humidity = humidity;
        _pressure = pressure;
        _sunrise = sunrise;
        _sunset = sunset;
        _lastUpdate = lastUpdate;
        _weatherCondition = weatherCondition;
    }

    @Override
    public String GetCity() {
        return _city;
    }

    @Override
    public String GetCountry() {
        return _country;
    }

    @Override
    public String GetDescription() {
        return _description;
    }

    @Override
    public double GetTemperature() {
        return _temperature;
    }

    @Override
    public double GetHumidity() {
        return _humidity;
    }

    @Override
    public double GetPressure() {
        return _pressure;
    }

    @Override
    public Calendar GetSunriseTime() {
        return _sunrise;
    }

    @Override
    public Calendar GetSunsetTime() {
        return _sunset;
    }

    @Override
    public Calendar GetLastUpdate() {
        return _lastUpdate;
    }

    @Override
    public WeatherCondition GetWeatherCondition() {
        return _weatherCondition;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "{\"Class\":\"%s\",\"City\":\"%s\",\"Country\":\"%s\",\"Description\":\"%s\",\"Temperature\":%.2f,\"Humidity\":%.2f,\"Pressure\":%.2f,\"Sunrise\":\"%s\",\"Sunset\":\"%s\",\"LastUpdate\":\"%s\",\"WeatherCondition\":\"%s\"}",
                Tag, _city, _country, _description, _temperature, _humidity, _pressure, _sunrise, _sunset, _lastUpdate, _weatherCondition);
    }
}