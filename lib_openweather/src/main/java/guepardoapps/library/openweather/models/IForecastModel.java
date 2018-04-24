package guepardoapps.library.openweather.models;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings({"unused"})
public interface IForecastModel extends Serializable {
    City GetCity();

    ArrayList<ForecastPartModel> GetList();

    int GetWallpaper();
}
