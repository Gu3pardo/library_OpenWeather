package guepardoapps.library.openweather.common.model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import guepardoapps.library.openweather.common.OWDefinitions;
import guepardoapps.library.openweather.common.OWEnables;
import guepardoapps.library.openweather.R;
import guepardoapps.library.openweather.common.enums.ForecastDayTime;
import guepardoapps.library.openweather.common.enums.WeatherCondition;

import guepardoapps.library.toolset.common.Logger;
import guepardoapps.library.toolset.common.classes.NotificationContent;

public class ForecastModel implements Serializable {

    private static final long serialVersionUID = 5940897350070310574L;

    private static final String TAG = ForecastModel.class.getSimpleName();
    private Logger _logger;

    private String _city;
    private String _country;
    private List<ForecastWeatherModel> _list = new ArrayList<>();

    public ForecastModel(@NonNull JSONObject json) {
        _logger = new Logger(TAG, OWEnables.LOGGING);
        _logger.Info(TAG + " created...");
        _logger.Info("json: " + json.toString());

        try {
            _city = json.getJSONObject("city").getString("name").toUpperCase(Locale.getDefault());
            _country = json.getJSONObject("city").getString("country");

            _logger.Info("_city: " + _city);
            _logger.Info("_country: " + _country);

            JSONArray dataList = json.getJSONArray("list");
            _logger.Info(dataList.toString());

            String previousDate = "";
            int forecastIndex = 0;
            for (int index = 0; index < dataList.length(); index++) {
                String currentDate = dataList.getJSONObject(index).getString("dt_txt").split(" ")[0];
                ForecastWeatherModel model;

                if (index == 0) {
                    model = new ForecastWeatherModel(currentDate);
                    _logger.Info("add model: " + model.toString());
                    _list.add(forecastIndex, model);
                    forecastIndex++;
                }

                if (currentDate.contains(previousDate)) {
                    model = new ForecastWeatherModel(dataList.getJSONObject(index));
                    _logger.Info("add model: " + model.toString());
                    _list.add(forecastIndex, model);
                    forecastIndex++;
                } else {
                    model = new ForecastWeatherModel(currentDate);
                    _logger.Info("add model: " + model.toString());
                    _list.add(forecastIndex, model);
                    forecastIndex++;

                    model = new ForecastWeatherModel(dataList.getJSONObject(index));
                    _logger.Info("add model: " + model.toString());
                    _list.add(forecastIndex, model);
                    forecastIndex++;
                }

                previousDate = currentDate;
            }
        } catch (JSONException e) {
            _logger.Error(e.toString());
        }
    }

    public String GetCity() {
        return _city;
    }

    public String GetCountry() {
        return _country;
    }

    public List<ForecastWeatherModel> GetList() {
        return _list;
    }

    public NotificationContent GetNextWeather() {
        List<ForecastWeatherModel> nextWeather = new ArrayList<>();

        for (ForecastWeatherModel entry : _list) {
            if (entry.GetDayTime() == ForecastDayTime.GetByValue(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))) {
                _logger.Debug("entry: " + entry.toString());
                nextWeather.add(entry);
            }
        }

        if (nextWeather.size() > 0) {
            return new NotificationContent(
                    "Next Weather",
                    nextWeather.get(0).GetWeatherDescription(),
                    R.drawable.weather_dummy,
                    R.drawable.wallpaper_dummy);
        }

        return null;
    }

    public NotificationContent GetTodayWeather() {
        List<ForecastWeatherModel> todayWeather = new ArrayList<>();

        for (ForecastWeatherModel entry : _list) {
            if (entry.GetCalendarDay().get(Calendar.DAY_OF_MONTH) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                _logger.Debug("entry: " + entry.toString());
                todayWeather.add(entry);
            }
        }

        if (todayWeather.size() > 0) {
            String weatherDescriptionToday = "";
            for (ForecastWeatherModel entry : todayWeather) {
                weatherDescriptionToday += entry.GetWeatherDescription() + ";";
            }
            return new NotificationContent(
                    "Weather today",
                    weatherDescriptionToday,
                    R.drawable.weather_dummy,
                    R.drawable.wallpaper_dummy);
        }

        return new NotificationContent(
                "Error",
                "GetTodayWeather failed!",
                R.drawable.weather_dummy,
                R.drawable.wallpaper_dummy);
    }

    public NotificationContent TellForecastWeather() {
        _logger.Debug("TellForecastWeather");

        List<ForecastWeatherModel> todayWeather = new ArrayList<>();
        List<ForecastWeatherModel> tomorrowWeather = new ArrayList<>();

        boolean isWeekend = false;
        Calendar today = Calendar.getInstance();

        for (ForecastWeatherModel entry : _list) {
            if (entry.GetCalendarDay().get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
                _logger.Debug("entry for today: " + entry.toString());
                todayWeather.add(entry);
            } else {
                Calendar tomorrow = Calendar.getInstance();
                tomorrow.set(Calendar.DAY_OF_MONTH, today.get(Calendar.DAY_OF_MONTH) + 1);
                _logger.Debug(String.format(Locale.getDefault(), "tomorrow is %s", tomorrow));

                if (tomorrow.get(Calendar.DAY_OF_MONTH) > today.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    tomorrow.set(Calendar.DAY_OF_MONTH, tomorrow.get(Calendar.DAY_OF_MONTH) - today.getActualMaximum(Calendar.DAY_OF_MONTH));
                    _logger.Debug(String.format(Locale.getDefault(), "tomorrow is too big %s > %s", tomorrow, today.getActualMaximum(Calendar.DAY_OF_MONTH)));
                }

                if (entry.GetCalendarDay().get(Calendar.DAY_OF_MONTH) == tomorrow.get(Calendar.DAY_OF_MONTH)) {
                    _logger.Debug("entry for tomorrow: " + entry.toString());
                    tomorrowWeather.add(entry);
                }
            }
        }

        if (todayWeather.size() > 2) {
            int dayOfWeek = today.get(Calendar.DAY_OF_WEEK);
            _logger.Debug(String.format("DayOfWeek is %s", dayOfWeek));

            if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) {
                isWeekend = true;
            }
            _logger.Debug(String.format(Locale.getDefault(), "today is weekend is %s", isWeekend));

            return createNotificationContent(todayWeather, true, isWeekend);
        } else if (tomorrowWeather.size() > 2) {
            int tomorrowDayOfWeek = today.get(Calendar.DAY_OF_WEEK) + 1;
            if (tomorrowDayOfWeek > 7) {
                tomorrowDayOfWeek = 1;
            }
            _logger.Debug(String.format("tomorrowDayOfWeek is %s", tomorrowDayOfWeek));

            if (tomorrowDayOfWeek == Calendar.SUNDAY || tomorrowDayOfWeek == Calendar.SATURDAY) {
                isWeekend = true;
            }
            _logger.Debug(String.format("tomorrow is weekend is %s", isWeekend));

            return createNotificationContent(tomorrowWeather, false, isWeekend);
        }

        return new NotificationContent("Error", "TellForecastWeather failed!", R.drawable.weather_dummy, R.drawable.wallpaper_dummy);
    }

    private NotificationContent createNotificationContent(List<ForecastWeatherModel> weatherList, boolean today,
                                                          boolean isWeekend) {

        List<WeatherConditionModel> conditionCount = new ArrayList<>();

        WeatherConditionModel clear = new WeatherConditionModel(
                0,
                "Go to the park and enjoy the clear weather today!",
                "Today will be clear! Get out for lunch!",
                "Enjoy your day after work! Today will be clear!",
                R.drawable.weather_clear,
                R.drawable.wallpaper_clear);
        WeatherConditionModel cloud = new WeatherConditionModel(
                0,
                "Sun is hiding today.",
                "No sun today. Not bad to work...",
                "No sun after work today, cloudy!",
                R.drawable.weather_cloud,
                R.drawable.wallpaper_cloud);
        WeatherConditionModel drizzle = new WeatherConditionModel(
                0,
                "It's a cold and rainy day!",
                "There will be drizzle today!",
                "There will be drizzle after work today!",
                R.drawable.weather_rain,
                R.drawable.wallpaper_drizzle);
        WeatherConditionModel fog = new WeatherConditionModel(
                0,
                "You're not gonna see your hand today! :P",
                "Find your way to work today :P",
                "Find your way to home from work today :P",
                R.drawable.weather_fog,
                R.drawable.wallpaper_fog);
        WeatherConditionModel haze = new WeatherConditionModel(
                0,
                "Will be haze today!",
                "Search your way, master!",
                "Haze on your way from work today!",
                R.drawable.weather_haze,
                R.drawable.wallpaper_haze);
        WeatherConditionModel mist = new WeatherConditionModel(
                0,
                "Will be misty today!",
                "Watch out today!",
                "Mist on your way from work today!",
                R.drawable.weather_haze,
                R.drawable.wallpaper_mist);
        WeatherConditionModel rain = new WeatherConditionModel(
                0,
                "It's a rainy day! Chill at home ;)",
                "It will rain today! Take an umbrella or take your car to work.",
                "It will rain after work today!",
                R.drawable.weather_rain,
                R.drawable.wallpaper_rain);
        WeatherConditionModel sleet = new WeatherConditionModel(
                0,
                "Today will be a freezy and slittering day!",
                "Take care outside today!",
                "Slittering way home from work today!",
                R.drawable.weather_sleet,
                R.drawable.wallpaper_sleet);
        WeatherConditionModel snow = new WeatherConditionModel(
                0,
                "Today will be a snowy day!",
                "Snow today. Think twice taking your bike!",
                "Today will be a snowy way back from work!",
                R.drawable.weather_snow,
                R.drawable.wallpaper_snow);
        WeatherConditionModel sun = new WeatherConditionModel(
                0,
                "Enjoy the sunny weather today and chill!",
                "Today will be sunny! Get out for lunch!",
                "Enjoy your afterwork today! Will be sunny!",
                R.drawable.weather_clear,
                R.drawable.wallpaper_sun);
        WeatherConditionModel thunderstorm = new WeatherConditionModel(
                0,
                "Thunder is coming today!",
                "Prepare for a thunderstorm today!",
                "Today afternoon will be a thunderstorm!",
                R.drawable.weather_thunderstorm,
                R.drawable.wallpaper_thunderstorm);

        String notificationForecast = "";
        int notificationForecastCount = 0;
        double notificationForecastMaxTemp = -273.15;
        int notificationIcon = R.drawable.weather_dummy;
        int notificationBigIcon = R.drawable.wallpaper_dummy;

        for (ForecastWeatherModel entry : weatherList) {
            if (entry.GetCondition() == WeatherCondition.CLEAR) {
                clear.IncreaseCount();
            } else if (entry.GetCondition() == WeatherCondition.CLOUD) {
                cloud.IncreaseCount();
            } else if (entry.GetCondition() == WeatherCondition.DRIZZLE) {
                drizzle.IncreaseCount();
            } else if (entry.GetCondition() == WeatherCondition.FOG) {
                fog.IncreaseCount();
            } else if (entry.GetCondition() == WeatherCondition.HAZE) {
                haze.IncreaseCount();
            } else if (entry.GetCondition() == WeatherCondition.MIST) {
                mist.IncreaseCount();
            } else if (entry.GetCondition() == WeatherCondition.RAIN) {
                rain.IncreaseCount();
            } else if (entry.GetCondition() == WeatherCondition.SLEET) {
                sleet.IncreaseCount();
            } else if (entry.GetCondition() == WeatherCondition.SNOW) {
                snow.IncreaseCount();
            } else if (entry.GetCondition() == WeatherCondition.SUN) {
                sun.IncreaseCount();
            } else if (entry.GetCondition() == WeatherCondition.THUNDERSTORM) {
                thunderstorm.IncreaseCount();
            }
        }

        conditionCount.add(clear);
        conditionCount.add(cloud);
        conditionCount.add(drizzle);
        conditionCount.add(fog);
        conditionCount.add(haze);
        conditionCount.add(mist);
        conditionCount.add(rain);
        conditionCount.add(sleet);
        conditionCount.add(snow);
        conditionCount.add(sun);
        conditionCount.add(thunderstorm);

        for (WeatherConditionModel model : conditionCount) {
            if (model.GetCount() > notificationForecastCount) {
                if (!today) {
                    model.ChangeTipsToTomorrow();
                }

                if (isWeekend) {
                    notificationForecast = model.GetWeekendTip();
                } else {
                    Calendar now = Calendar.getInstance();
                    if (now.get(Calendar.HOUR_OF_DAY) > OWDefinitions.AFTERWORK_HOUR) {
                        notificationForecast = model.GetWorkdayAfterWorkTip();
                    } else {
                        notificationForecast = model.GetWorkdayTip();
                    }
                }

                notificationForecastCount = model.GetCount();
                notificationIcon = model.GetIcon();
                notificationBigIcon = model.GetWallpaper();
            }
        }

        for (ForecastWeatherModel model : weatherList) {
            if (model.GetTempMaxDouble() > notificationForecastMaxTemp) {
                notificationForecastMaxTemp = model.GetTempMaxDouble();
            }
        }

        return new NotificationContent(
                "Hey you!",
                String.format(Locale.getDefault(), "%s\nIt's getting up to %.1f °C", notificationForecast, notificationForecastMaxTemp),
                notificationIcon,
                notificationBigIcon);
    }

    @Override
    public String toString() {
        String toString = "[" + TAG + ": City: " + _city + ", Country: " + _country;
        for (ForecastWeatherModel entry : _list) {
            toString += ";" + entry.toString();
        }
        toString += "]";

        return toString;
    }
}