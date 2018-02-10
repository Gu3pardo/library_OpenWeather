package guepardoapps.library.openweather.models;

import android.support.annotation.NonNull;

import java.util.Locale;

public class City implements ICity {
    private static final String Tag = City.class.getSimpleName();

    private int _id;
    private String _name;
    private String _country;
    private int _population;

    private double _latitude;
    private double _longitude;

    public City(int id, @NonNull String name, @NonNull String country, int population, double latitude, double longitude) {
        _id = id;
        _name = name;
        _country = country;
        _population = population;
        _latitude = latitude;
        _longitude = longitude;
    }

    @Override
    public int GetId() {
        return _id;
    }

    @Override
    public String GetName() {
        return _name;
    }

    @Override
    public String GetCountry() {
        return _country;
    }

    @Override
    public int GetPopulation() {
        return _population;
    }

    @Override
    public double GetLatitude() {
        return _latitude;
    }

    @Override
    public double GetLongitude() {
        return _longitude;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "{\"Class\":\"%s\",\"Id\":%d,\"Name\":\"%s\",\"Country\":\"%s\",\"Population\":%d,\"Latitude\":%.2f,\"Longitude\":%.2f}",
                Tag, _id, _name, _country, _population, _latitude, _longitude);
    }
}
