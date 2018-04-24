package guepardoapps.library.openweather.models;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Locale;

import guepardoapps.library.openweather.converter.NotificationContentConverter;

public class ForecastModel implements IForecastModel {
    private static final String Tag = ForecastModel.class.getSimpleName();

    private City _city;
    private ArrayList<ForecastPartModel> _partModelList;

    public ForecastModel(@NonNull City city, @NonNull ArrayList<ForecastPartModel> partModelList) {
        _city = city;
        _partModelList = partModelList;
    }

    @Override
    public City GetCity() {
        return _city;
    }

    @Override
    public ArrayList<ForecastPartModel> GetList() {
        return _partModelList;
    }

    @Override
    public int GetWallpaper() {
        return NotificationContentConverter.GetMostNextWeatherCondition(_partModelList).GetWallpaper();
    }

    @Override
    public String toString() {
        StringBuilder partModelListString = new StringBuilder();
        for (ForecastPartModel entry : _partModelList) {
            partModelListString.append(",").append(entry.toString());
        }

        return String.format(Locale.getDefault(),
                "{\"Class\":\"%s\",\"City\":\"%s\",\"PartModelList\":{\"%s\"}}",
                Tag, _city, partModelListString);
    }
}