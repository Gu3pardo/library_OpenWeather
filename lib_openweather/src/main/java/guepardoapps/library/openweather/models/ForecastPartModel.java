package guepardoapps.library.openweather.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;

import guepardoapps.library.openweather.enums.ForecastDayTime;
import guepardoapps.library.openweather.enums.WeatherCondition;

public class ForecastPartModel implements Serializable {
    public enum ForecastListType {FORECAST, DATE_DIVIDER}

    //private static final String TAG = ForecastPartModel.class.getSimpleName();

    private String _description;

    private double _tempMin;
    private double _tempMax;
    private double _pressure;
    private double _humidity;

    private String _date;
    private String _time;

    private WeatherCondition _condition;

    private ForecastListType _listType;

    public ForecastPartModel(
            @NonNull String description,
            double tempMin,
            double tempMax,
            double pressure,
            double humidity,
            @NonNull String date,
            @NonNull String time,
            @NonNull WeatherCondition condition) {
        _description = description;

        _tempMin = tempMin;
        _tempMax = tempMax;
        _humidity = humidity;
        _pressure = pressure;

        _date = date;
        _time = time;

        _condition = condition;

        _listType = ForecastListType.FORECAST;
    }

    public ForecastPartModel(@NonNull String date) {
        _description = "";

        _tempMin = 0;
        _tempMax = 0;
        _humidity = 0;
        _pressure = 0;

        _date = date;
        _time = "";

        _condition = WeatherCondition.NULL;

        _listType = ForecastListType.DATE_DIVIDER;
    }

    public String GetDescription() {
        return _description;
    }

    public double GetTempMin() {
        return _tempMin;
    }

    public double GetTempMax() {
        return _tempMax;
    }

    public String GetTemperatureString() {
        return String.format(Locale.getDefault(), "%.2f - %.2f °C", _tempMin, _tempMax);
    }

    public double GetHumidity() {
        return _humidity;
    }

    public String GetHumidityString() {
        return String.format(Locale.getDefault(), "%.2f%%", _humidity);
    }

    public double GetPressure() {
        return _pressure;
    }

    public String GetPressureString() {
        return String.format(Locale.getDefault(), "%.2f%%", _pressure);
    }

    public String GetDate() {
        return _date;
    }

    public String GetTime() {
        return _time;
    }

    public WeatherCondition GetCondition() {
        return _condition;
    }

    public ForecastListType GetForecastListType() {
        return _listType;
    }

    public ForecastDayTime GetDayTime() {
        int hour = Integer.parseInt(_time.substring(0, 2).replace(":", ""));
        return ForecastDayTime.GetByValue(hour);
    }

    public Calendar GetCalendarDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(_date.substring(8)));
        return calendar;
    }
}