package guepardoapps.library.openweather.converter;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import guepardoapps.library.openweather.R;
import guepardoapps.library.openweather.common.OWDefinitions;
import guepardoapps.library.openweather.common.classes.NotificationContent;
import guepardoapps.library.openweather.common.utils.Logger;
import guepardoapps.library.openweather.enums.ForecastDayTime;
import guepardoapps.library.openweather.enums.WeatherCondition;
import guepardoapps.library.openweather.models.ForecastPartModel;

public class NotificationContentConverter {
    private final static String TAG = NotificationContentConverter.class.getSimpleName();
    private Logger _logger;

    public NotificationContentConverter() {
        _logger = new Logger(TAG);
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
                _logger.Debug("entry: " + entry.toString());
                todayWeather.add(entry);
            }
        }

        if (todayWeather.size() > 0) {
            String weatherDescriptionToday = "";

            for (ForecastPartModel entry : todayWeather) {
                weatherDescriptionToday += entry.GetDescription() + ";";
            }

            return new NotificationContent(
                    "Weather today",
                    weatherDescriptionToday,
                    todayWeather.get(0).GetCondition().GetIcon(),
                    todayWeather.get(0).GetCondition().GetWallpaper());
        }

        return new NotificationContent(
                "Error",
                "GetTodayWeather failed!",
                R.drawable.weather_dummy,
                R.drawable.wallpaper_dummy);
    }

    public NotificationContent TellForecastWeather(@NonNull List<ForecastPartModel> weatherList) {
        _logger.Debug("TellForecastWeather");

        List<ForecastPartModel> todayWeather = new ArrayList<>();
        List<ForecastPartModel> tomorrowWeather = new ArrayList<>();

        boolean isWeekend = false;
        Calendar today = Calendar.getInstance();

        for (ForecastPartModel entry : weatherList) {
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

    private NotificationContent createNotificationContent(@NonNull List<ForecastPartModel> weatherList, boolean today, boolean isWeekend) {
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

        String notificationForecast = "";
        int notificationForecastCount = 0;
        double notificationForecastMaxTemp = -273.15;
        int notificationIcon = R.drawable.weather_dummy;
        int notificationBigIcon = R.drawable.wallpaper_dummy;

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

        for (WeatherCondition model : conditionCount) {
            if (model.GetCount() > notificationForecastCount) {
                if (!today) {
                    model.ChangeTipsToTomorrow();
                }

                if (isWeekend) {
                    notificationForecast = model.GetWeekendTip();
                } else {
                    Calendar now = Calendar.getInstance();
                    if (now.get(Calendar.HOUR_OF_DAY) > OWDefinitions.AFTERWORK_HOUR
                            && now.get(Calendar.HOUR_OF_DAY) < OWDefinitions.EVENING_HOUR) {
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

        for (ForecastPartModel model : weatherList) {
            if (model.GetTempMax() > notificationForecastMaxTemp) {
                notificationForecastMaxTemp = model.GetTempMax();
            }
        }

        return new NotificationContent(
                "Hey you!",
                String.format(Locale.getDefault(), "%s\nIt's getting up to %.1f °C", notificationForecast, notificationForecastMaxTemp),
                notificationIcon,
                notificationBigIcon);
    }
}