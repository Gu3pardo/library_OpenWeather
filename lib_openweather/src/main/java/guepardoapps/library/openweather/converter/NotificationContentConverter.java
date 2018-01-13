package guepardoapps.library.openweather.converter;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import guepardoapps.library.openweather.R;
import guepardoapps.library.openweather.common.classes.NotificationContent;
import guepardoapps.library.openweather.enums.ForecastDayTime;
import guepardoapps.library.openweather.enums.WeatherCondition;
import guepardoapps.library.openweather.models.ForecastPartModel;

@SuppressWarnings({"unused"})
public class NotificationContentConverter implements Serializable {
    //private final static String TAG = NotificationContentConverter.class.getSimpleName();

    private static final NotificationContentConverter SINGLETON = new NotificationContentConverter();

    public static NotificationContentConverter getInstance() {
        return SINGLETON;
    }

    private NotificationContentConverter() {
    }

    public NotificationContent GetNextWeather(@NonNull List<ForecastPartModel> weatherList) {
        List<ForecastPartModel> nextWeather = new ArrayList<>();

        for (ForecastPartModel entry : weatherList) {
            if (entry.GetDayTime() == ForecastDayTime.GetByValue(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))) {
                nextWeather.add(entry);
            }
        }

        if (nextWeather.size() > 0) {
            return new NotificationContent(
                    "Next Weather",
                    nextWeather.get(0).GetDescription(),
                    nextWeather.get(0).GetCondition().GetIcon(),
                    nextWeather.get(0).GetCondition().GetWallpaper());
        }

        return null;
    }

    public NotificationContent GetTodayWeather(@NonNull List<ForecastPartModel> weatherList) {
        List<ForecastPartModel> todayWeather = new ArrayList<>();

        for (ForecastPartModel entry : weatherList) {
            if (entry.GetCalendarDay().get(Calendar.DAY_OF_MONTH) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                todayWeather.add(entry);
            }
        }

        if (todayWeather.size() > 0) {
            StringBuilder weatherDescriptionToday = new StringBuilder();

            for (ForecastPartModel entry : todayWeather) {
                weatherDescriptionToday.append(entry.GetDescription()).append(";");
            }

            return new NotificationContent(
                    "Weather today",
                    weatherDescriptionToday.toString(),
                    todayWeather.get(0).GetCondition().GetIcon(),
                    todayWeather.get(0).GetCondition().GetWallpaper());
        }

        return new NotificationContent(
                "Error",
                "GetTodayWeather failed!",
                R.drawable.weather_dummy,
                R.drawable.weather_wallpaper_dummy);
    }

    public NotificationContent TellForecastWeather(@NonNull List<ForecastPartModel> weatherList) {
        List<ForecastPartModel> todayWeather = new ArrayList<>();
        List<ForecastPartModel> tomorrowWeather = new ArrayList<>();

        boolean isWeekend = false;
        Calendar today = Calendar.getInstance();

        for (ForecastPartModel entry : weatherList) {
            if (entry.GetCalendarDay().get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
                todayWeather.add(entry);
            } else {
                Calendar tomorrow = Calendar.getInstance();
                tomorrow.set(Calendar.DAY_OF_MONTH, today.get(Calendar.DAY_OF_MONTH) + 1);

                if (tomorrow.get(Calendar.DAY_OF_MONTH) > today.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    tomorrow.set(Calendar.DAY_OF_MONTH, tomorrow.get(Calendar.DAY_OF_MONTH) - today.getActualMaximum(Calendar.DAY_OF_MONTH));
                }

                if (entry.GetCalendarDay().get(Calendar.DAY_OF_MONTH) == tomorrow.get(Calendar.DAY_OF_MONTH)) {
                    tomorrowWeather.add(entry);
                }
            }
        }

        if (todayWeather.size() > 2) {
            int dayOfWeek = today.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) {
                isWeekend = true;
            }

            return createNotificationContent(todayWeather, true, isWeekend);
        } else if (tomorrowWeather.size() > 2) {
            int tomorrowDayOfWeek = today.get(Calendar.DAY_OF_WEEK) + 1;
            if (tomorrowDayOfWeek > 7) {
                tomorrowDayOfWeek = 1;
            }

            if (tomorrowDayOfWeek == Calendar.SUNDAY || tomorrowDayOfWeek == Calendar.SATURDAY) {
                isWeekend = true;
            }

            return createNotificationContent(tomorrowWeather, false, isWeekend);
        }

        return new NotificationContent("Error", "TellForecastWeather failed!", R.drawable.weather_dummy, R.drawable.weather_wallpaper_dummy);
    }

    public WeatherCondition MostNextWeatherCondition(@NonNull List<ForecastPartModel> weatherList) {
        return getMostWeatherCondition(weatherList);
    }

    private NotificationContent createNotificationContent(@NonNull List<ForecastPartModel> weatherList, boolean today, boolean isWeekend) {
        WeatherCondition weatherCondition = getMostWeatherCondition(weatherList);

        String notificationForecast;
        double notificationForecastMaxTemp = -273.15;

        if (!today) {
            weatherCondition.ChangeTipsToTomorrow();
        }
        if (isWeekend) {
            notificationForecast = weatherCondition.GetWeekendTip();
        } else {
            Calendar now = Calendar.getInstance();
            switch (ForecastDayTime.GetByValue(Calendar.HOUR_OF_DAY)) {
                case AFTERWORK:
                case EVENING:
                case NIGHT:
                    notificationForecast = weatherCondition.GetWorkdayAfterWorkTip();
                    break;
                case MORNING:
                case MIDDAY:
                case NULL:
                default:
                    notificationForecast = weatherCondition.GetWorkdayTip();
                    break;
            }
        }

        for (ForecastPartModel model : weatherList) {
            if (model.GetTempMax() > notificationForecastMaxTemp) {
                notificationForecastMaxTemp = model.GetTempMax();
            }
        }

        return new NotificationContent(
                String.format(Locale.getDefault(), "It's getting up to %.1f °C", notificationForecastMaxTemp),
                notificationForecast,
                weatherCondition.GetIcon(),
                weatherCondition.GetWallpaper());
    }

    private WeatherCondition getMostWeatherCondition(@NonNull List<ForecastPartModel> weatherList) {
        List<WeatherCondition> conditionCount = new ArrayList<>();
        WeatherCondition clear = WeatherCondition.CLEAR;
        WeatherCondition cloud = WeatherCondition.CLOUD;
        WeatherCondition drizzle = WeatherCondition.DRIZZLE;
        WeatherCondition fog = WeatherCondition.FOG;
        WeatherCondition haze = WeatherCondition.HAZE;
        WeatherCondition mist = WeatherCondition.MIST;
        WeatherCondition rain = WeatherCondition.RAIN;
        WeatherCondition sleet = WeatherCondition.SLEET;
        WeatherCondition snow = WeatherCondition.SNOW;
        WeatherCondition sun = WeatherCondition.SUN;
        WeatherCondition thunderstorm = WeatherCondition.THUNDERSTORM;

        for (ForecastPartModel entry : weatherList) {
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

        int mostCount = 0;
        WeatherCondition mostWeatherCondition = null;

        for (WeatherCondition weatherCondition : conditionCount) {
            if (weatherCondition.GetCount() > mostCount) {
                mostCount = weatherCondition.GetCount();
                mostWeatherCondition = weatherCondition;
            }
        }

        return mostWeatherCondition;
    }
}
