package guepardoapps.library.openweather.models;

import java.io.Serializable;

@SuppressWarnings({"unused"})
public interface ICity extends Serializable {
    int GetId();

    String GetName();

    String GetCountry();

    int GetPopulation();

    double GetLatitude();

    double GetLongitude();
}
