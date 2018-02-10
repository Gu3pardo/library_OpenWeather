package guepardoapps.library.openweather.models;

import java.io.Serializable;
import java.util.ArrayList;

public interface IForecastModel extends Serializable {
    City GetCity();

    ArrayList<ForecastPartModel> GetList();

    int GetWallpaper();
}
