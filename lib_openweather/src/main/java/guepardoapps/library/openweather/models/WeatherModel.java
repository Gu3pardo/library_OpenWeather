package guepardoapps.library.openweather.models;

import android.support.annotation.NonNull;

import java.io.Serializable;

import guepardoapps.library.openweather.common.classes.SerializableTime;
import guepardoapps.library.openweather.common.utils.Logger;
import guepardoapps.library.openweather.enums.WeatherCondition;

public class WeatherModel implements Serializable {

    private static final String TAG = WeatherModel.class.getSimpleName();
    private Logger _logger;

    private String _city;
    private String _country;

    private String _description;

    private double _temperature;
    private double _humidity;
    private double _pressure;

    private SerializableTime _sunrise;
    private SerializableTime _sunset;

    private SerializableTime _lastUpdate;

    private WeatherCondition _condition;

    public WeatherModel(
            @NonNull String city,
            @NonNull String country,
            @NonNull String description,
            double temperature,
            double humidity,
            double pressure,
            @NonNull SerializableTime sunrise,
            @NonNull SerializableTime sunset,
            @NonNull SerializableTime lastUpdate,
            @NonNull WeatherCondition condition) {
        _logger = new Logger(TAG);

        _city = city;
        _country = country;
        _description = description;

        _temperature = temperature;
        _humidity = humidity;
        _pressure = pressure;

        _sunrise = sunrise;
        _sunset = sunset;

        _lastUpdate = lastUpdate;

        _condition = condition;
    }

    public String GetCity() {
        return _city;
    }

    public String GetCountry() {
        return _country;
    }

    public String GetDescription() {
        return _description;
    }

    public double GetTemperature() {
        return _temperature;
    }

    public double GetHumidity() {
        return _humidity;
    }

    public double GetPressure() {
        return _pressure;
    }

    public SerializableTime GetSunriseTime() {
        return _sunrise;
    }

    public SerializableTime GetSunsetTime() {
        return _sunset;
    }

    public SerializableTime GetLastUpdate() {
        return _lastUpdate;
    }

    public WeatherCondition GetCondition() {
        return _condition;
    }
}