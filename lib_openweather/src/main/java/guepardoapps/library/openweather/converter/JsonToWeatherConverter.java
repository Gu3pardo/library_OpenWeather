package guepardoapps.library.openweather.converter;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import guepardoapps.library.openweather.enums.WeatherCondition;
import guepardoapps.library.openweather.models.City;
import guepardoapps.library.openweather.models.ForecastModel;
import guepardoapps.library.openweather.models.ForecastPartModel;
import guepardoapps.library.openweather.models.WeatherModel;
import guepardoapps.library.openweather.utils.Logger;

public class JsonToWeatherConverter {
    private final static String Tag = JsonToWeatherConverter.class.getSimpleName();

    public static WeatherModel ConvertFromJsonToCurrentModel(@NonNull String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            if (json.getInt("cod") != 200) {
                Logger.getInstance().Error(Tag, "Error!");
                return null;
            }

            String city = json.getString("name").toUpperCase(Locale.getDefault());
            String country = json.getJSONObject("sys").getString("country");

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");

            String description = details.getString("description").toUpperCase(Locale.getDefault());
            WeatherCondition weatherCondition = WeatherCondition.GetByDescription(description);

            double temperature = main.getDouble("temp");
            double humidity = main.getDouble("humidity");
            double pressure = main.getDouble("pressure");

            JSONObject sys = json.getJSONObject("sys");

            long sunriseLong = sys.getLong("sunrise") * 1000;
            Calendar sunriseCalendar = Calendar.getInstance();
            sunriseCalendar.setTimeInMillis(sunriseLong);

            long sunsetLong = sys.getLong("sunset") * 1000;
            Calendar sunsetCalendar = Calendar.getInstance();
            sunsetCalendar.setTimeInMillis(sunsetLong);

            return new WeatherModel(city, country, description, temperature, humidity, pressure, sunriseCalendar, sunsetCalendar, Calendar.getInstance(), weatherCondition);
        } catch (Exception exception) {
            Logger.getInstance().Error(Tag, exception.toString());
            return null;
        }
    }

    public static ForecastModel ConvertFromJsonToForecastModel(@NonNull String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);

            int result = json.getInt("cod");
            if (result != 200) {
                Logger.getInstance().Error(Tag, String.format(Locale.getDefault(), "invalid result %d", result));
                return null;
            }

            JSONObject cityJsonObject = json.getJSONObject("city");
            int cityId = cityJsonObject.getInt("id");
            String cityName = cityJsonObject.getString("name");
            String country = cityJsonObject.getString("country");
            int cityPopulation = cityJsonObject.getInt("population");

            JSONObject cityCoordJsonObject = json.getJSONObject("coord");
            double cityLat = cityCoordJsonObject.getDouble("lat");
            double cityLon = cityCoordJsonObject.getDouble("lon");

            City city = new City(cityId, cityName, country, cityPopulation, cityLat, cityLon);
            ArrayList<ForecastPartModel> list = new ArrayList<>();

            JSONArray dataListJSONArray = json.getJSONArray("list");
            String previousDateString = "";

            for (int index = 0; index < dataListJSONArray.length(); index++) {
                JSONObject dataJsonObject = dataListJSONArray.getJSONObject(index);

                long dateTimeLong = dataJsonObject.getLong("dt");
                Calendar dateTime = Calendar.getInstance();
                dateTime.setTimeInMillis(dateTimeLong);

                String currentDateString = dataJsonObject.getString("dt_txt").split(" ")[0];

                if (index == 0) {
                    list.add(new ForecastPartModel(dateTime));
                }

                if (currentDateString.contains(previousDateString)) {
                    list.add(convertFromJsonToForecastPartModel(dataJsonObject));
                } else {
                    list.add(new ForecastPartModel(dateTime));
                    list.add(convertFromJsonToForecastPartModel(dataJsonObject));
                }

                previousDateString = currentDateString;
            }

            return new ForecastModel(city, list);

        } catch (Exception exception) {
            Logger.getInstance().Error(Tag, exception.toString());
            return null;
        }
    }

    private static ForecastPartModel convertFromJsonToForecastPartModel(@NonNull JSONObject jsonObject) {
        try {
            JSONObject jsonObjectWeather = jsonObject.getJSONArray("weather").getJSONObject(0);

            //int id = jsonObjectWeather.getInt("id");
            String main = jsonObjectWeather.getString("main");
            String description = jsonObjectWeather.getString("description");
            //String icon = jsonObjectWeather.getString("icon");

            double tempMin = jsonObject.getJSONObject("main").getDouble("temp_max");
            double tempMax = jsonObject.getJSONObject("main").getDouble("temp_max");
            double pressure = jsonObject.getJSONObject("main").getDouble("pressure");
            double humidity = jsonObject.getJSONObject("main").getDouble("humidity");

            long dateTimeLong = jsonObject.getLong("dt");
            Calendar dateTime = Calendar.getInstance();
            dateTime.setTimeInMillis(dateTimeLong);

            WeatherCondition weatherCondition = WeatherCondition.GetByDescription(description);

            return new ForecastPartModel(main, description, tempMin, tempMax, pressure, humidity, dateTime, weatherCondition);

        } catch (JSONException exception) {
            Logger.getInstance().Error(Tag, exception.toString());
            return null;
        }
    }
}
