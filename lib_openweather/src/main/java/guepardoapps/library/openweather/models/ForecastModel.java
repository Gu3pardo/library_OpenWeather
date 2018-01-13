package guepardoapps.library.openweather.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import guepardoapps.library.openweather.converter.NotificationContentConverter;

@SuppressWarnings({"unused"})
public class ForecastModel implements Serializable {
    private static final String TAG = ForecastModel.class.getSimpleName();

    private String _city;
    private String _country;
    private List<ForecastPartModel> _list = new ArrayList<>();

    public ForecastModel(
            @NonNull String city,
            @NonNull String country,
            @NonNull List<ForecastPartModel> list) {
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
        return NotificationContentConverter.getInstance().MostNextWeatherCondition(_list).GetWallpaper();
    }

    @Override
    public String toString() {
        StringBuilder toString = new StringBuilder("[" + TAG + ": City: " + _city + ", Country: " + _country);

        for (ForecastPartModel entry : _list) {
            toString.append(";").append(entry.toString());
        }

        toString.append("]");
        return toString.toString();
    }
}