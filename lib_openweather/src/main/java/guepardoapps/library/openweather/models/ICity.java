package guepardoapps.library.openweather.models;

import java.io.Serializable;

public interface ICity extends Serializable {
    int GetId();

    String GetName();

    String GetCountry();

    int GetPopulation();

    double GetLatitude();

    double GetLongitude();
}
