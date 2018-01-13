package guepardoapps.library.openweather.converter;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import guepardoapps.library.openweather.common.classes.SerializableTime;
import guepardoapps.library.openweather.common.utils.Logger;
import guepardoapps.library.openweather.enums.WeatherCondition;
import guepardoapps.library.openweather.models.ForecastModel;
import guepardoapps.library.openweather.models.ForecastPartModel;
import guepardoapps.library.openweather.models.WeatherModel;

@SuppressWarnings({"deprecation"})
public class JsonToWeatherConverter {
    private final static String TAG = JsonToWeatherConverter.class.getSimpleName();

    private static final JsonToWeatherConverter SINGLETON = new JsonToWeatherConverter();

    public static JsonToWeatherConverter getInstance() {
        return SINGLETON;
    }

    private JsonToWeatherConverter() {
    }

    public WeatherModel ConvertFromJsonToWeatherModel(@NonNull String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            if (json.getInt("cod") != 200) {
                Logger.getInstance().Error(TAG, "Error!");
                return null;
            }

            String city = json.getString("name").toUpperCase(Locale.getDefault());
            String country = json.getJSONObject("sys").getString("country");

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");

            String description = details.getString("description").toUpperCase(Locale.getDefault());
            WeatherCondition condition = WeatherCondition.GetByDescription(description);

            double temperature = main.getDouble("temp");
            double humidity = main.getDouble("humidity");
            double pressure = main.getDouble("pressure");

            JSONObject sys = json.getJSONObject("sys");

            long sunriseLong = sys.getLong("sunrise") * 1000;
            Date sunriseDate = new Date(sunriseLong);
            Time sunriseTime = new Time(sunriseDate.getTime());
            SerializableTime sunriseSerializableTime = new SerializableTime(sunriseTime.getHours(), sunriseTime.getMinutes(), sunriseTime.getSeconds(), 0);

            long sunsetLong = sys.getLong("sunset") * 1000;
            Date sunsetDate = new Date(sunsetLong);
            Time sunsetTime = new Time(sunsetDate.getTime());
            SerializableTime sunsetSerializableTime = new SerializableTime(sunsetTime.getHours(), sunsetTime.getMinutes(), sunsetTime.getSeconds(), 0);

            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);
            SerializableTime lastUpdate = new SerializableTime(hour, minute, second, 0);

            return new WeatherModel(
                    city, country, description,
                    temperature, humidity, pressure,
                    sunriseSerializableTime, sunsetSerializableTime, lastUpdate,
                    condition);

        } catch (Exception exception) {
            Logger.getInstance().Error(TAG, exception.toString());
            return null;
        }
    }


    public ForecastModel ConvertFromJsonToForecastModel(@NonNull String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            if (json.getInt("cod") != 200) {
                Logger.getInstance().Error(TAG, "Error!");
                return null;
            }

            String city = json.getJSONObject("city").getString("name").toUpperCase(Locale.getDefault());
            String country = json.getJSONObject("city").getString("country");

            String previousDate = "";
            int forecastIndex = 0;
            List<ForecastPartModel> list = new ArrayList<>();
            JSONArray dataList = json.getJSONArray("list");

            for (int index = 0; index < dataList.length(); index++) {
                String currentDate = dataList.getJSONObject(index).getString("dt_txt").split(" ")[0];

                if (index == 0) {
                    list.add(forecastIndex, new ForecastPartModel(currentDate));
                    forecastIndex++;
                }

                if (currentDate.contains(previousDate)) {
                    list.add(forecastIndex, convertFromJsonToForecastPartModel(dataList.getJSONObject(index)));
                    forecastIndex++;
                } else {
                    list.add(forecastIndex, new ForecastPartModel(currentDate));
                    forecastIndex++;

                    list.add(forecastIndex, convertFromJsonToForecastPartModel(dataList.getJSONObject(index)));
                    forecastIndex++;
                }

                previousDate = currentDate;
            }

            return new ForecastModel(city, country, list);

        } catch (Exception exception) {
            Logger.getInstance().Error(TAG, exception.toString());
            return null;
        }
    }

    private ForecastPartModel convertFromJsonToForecastPartModel(@NonNull JSONObject json) {
        try {
            String description = json.getJSONArray("weather").getJSONObject(0).getString("description");

            double tempMin = json.getJSONObject("main").getDouble("temp_max");
            double tempMax = json.getJSONObject("main").getDouble("temp_max");
            double pressure = json.getJSONObject("main").getDouble("pressure");
            double humidity = json.getJSONObject("main").getDouble("humidity");

            String[] dateTime = json.getString("dt_txt").split(" ");
            String date = dateTime[0];
            String time = dateTime[1];

            WeatherCondition weatherCondition = WeatherCondition.GetByDescription(description);

            return new ForecastPartModel(
                    description,
                    tempMin, tempMax,
                    pressure, humidity,
                    date, time,
                    weatherCondition);

        } catch (JSONException exception) {
            Logger.getInstance().Error(TAG, exception.toString());
            return null;
        }
    }
}
