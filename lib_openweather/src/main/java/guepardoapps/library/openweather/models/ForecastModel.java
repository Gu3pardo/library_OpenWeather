package guepardoapps.library.openweather.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import guepardoapps.library.openweather.common.utils.Logger;
import guepardoapps.library.openweather.converter.NotificationContentConverter;

public class ForecastModel implements Serializable {

    private static final String TAG = ForecastModel.class.getSimpleName();
    private Logger _logger;

    private NotificationContentConverter _notificationContentConverter;

    private String _city;
    private String _country;
    private List<ForecastPartModel> _list = new ArrayList<>();

    public ForecastModel(
            @NonNull String city,
            @NonNull String country,
            @NonNull List<ForecastPartModel> list) {
        _logger = new Logger(TAG);

        _notificationContentConverter = new NotificationContentConverter();

        _city = city;
        _country = country;
        _list = list;
    }

    public String GetCity() {
        return _city;
    }

    public String GetCountry() {
        return _country;
    }

    public List<ForecastPartModel> GetList() {
        return _list;
    }

    public int GetWallpaper() {
        return _notificationContentConverter.MostNextWeatherCondition(_list).GetWallpaper();
    }

    @Override
    public String toString() {
        String toString = "[" + TAG + ": City: " + _city + ", Country: " + _country;

        for (ForecastPartModel entry : _list) {
            toString += ";" + entry.toString();
        }

        toString += "]";
        return toString;
    }
}