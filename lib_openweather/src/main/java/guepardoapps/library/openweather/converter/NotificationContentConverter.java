package guepardoapps.library.openweather.converter;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import guepardoapps.library.openweather.R;
import guepardoapps.library.openweather.datatransferobjects.NotificationContentDto;
import guepardoapps.library.openweather.enums.ForecastDayTime;
import guepardoapps.library.openweather.enums.WeatherCondition;
import guepardoapps.library.openweather.models.ForecastPartModel;

/**
 * NotificationContentConverter to use to create NotificationContentDto
 */
@SuppressWarnings({"unused"})
public class NotificationContentConverter implements Serializable {

    /**
     * @param weatherList List which is used to get the today weather
     * @return returns NotificationContentDto
     */
    public static NotificationContentDto GetTodayWeather(@NonNull ArrayList<ForecastPartModel> weatherList) {
        ArrayList<ForecastPartModel> todayWeather = new ArrayList<>();

        for (ForecastPartModel entry : weatherList) {
            if (entry.GetDateTime().get(Calendar.DAY_OF_MONTH) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                todayWeather.add(entry);
            }
        }

        if (todayWeather.size() > 0) {
            StringBuilder weatherDescriptionToday = new StringBuilder();

            for (ForecastPartModel entry : todayWeather) {
                weatherDescriptionToday.append(entry.GetDescription()).append(";");
            }

            return new NotificationContentDto("Weather today", weatherDescriptionToday.toString(), todayWeather.get(0).GetCondition().GetIcon(), todayWeather.get(0).GetCondition().GetWallpaper());
        }

        return new NotificationContentDto("Error", "GetTodayWeather failed!", R.drawable.weather_dummy, R.drawable.weather_wallpaper_dummy);
    }

    /**
     * @param weatherList List which is used to get the next weather
     * @return returns NotificationContentDto
     */
    public static NotificationContentDto GetNextWeather(@NonNull ArrayList<ForecastPartModel> weatherList) {
        ArrayList<ForecastPartModel> nextWeather = new ArrayList<>();

        for (ForecastPartModel entry : weatherList) {
            if (entry.GetDayTime() == ForecastDayTime.GetByValue(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))) {
                nextWeather.add(entry);
            }
        }

        if (nextWeather.size() > 0) {
            return new NotificationContentDto("Next Weather", nextWeather.get(0).GetDescription(), nextWeather.get(0).GetCondition().GetIcon(), nextWeather.get(0).GetCondition().GetWallpaper());
        }

        return new NotificationContentDto("Error", "GetNextWeather failed!", R.drawable.weather_dummy, R.drawable.weather_wallpaper_dummy);
    }

    /**
     * @param weatherList List which is used to get the forecast weather
     * @return returns NotificationContentDto
     */
    public static NotificationContentDto GetForecastWeather(@NonNull ArrayList<ForecastPartModel> weatherList) {
        ArrayList<ForecastPartModel> todayWeather = new ArrayList<>();
        ArrayList<ForecastPartModel> tomorrowWeather = new ArrayList<>();

        boolean isWeekend = false;
        Calendar today = Calendar.getInstance();

        for (ForecastPartModel entry : weatherList) {
            if (entry.GetDateTime().get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
                todayWeather.add(entry);
            } else {
                Calendar tomorrow = Calendar.getInstance();
                tomorrow.set(Calendar.DAY_OF_MONTH, today.get(Calendar.DAY_OF_MONTH) + 1);

                if (tomorrow.get(Calendar.DAY_OF_MONTH) > today.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    tomorrow.set(Calendar.DAY_OF_MONTH, tomorrow.get(Calendar.DAY_OF_MONTH) - today.getActualMaximum(Calendar.DAY_OF_MONTH));
                }

                if (entry.GetDateTime().get(Calendar.DAY_OF_MONTH) == tomorrow.get(Calendar.DAY_OF_MONTH)) {
                    tomorrowWeather.add(entry);
                }
            }
        }

        if (todayWeather.size() > 2) {
            int dayOfWeek = today.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) {
                isWeekend = true;
            }

            return createNotificationContentDto(todayWeather, true, isWeekend);
        } else if (tomorrowWeather.size() > 2) {
            int tomorrowDayOfWeek = today.get(Calendar.DAY_OF_WEEK) + 1;
            if (tomorrowDayOfWeek > 7) {
                tomorrowDayOfWeek = 1;
            }

            if (tomorrowDayOfWeek == Calendar.SUNDAY || tomorrowDayOfWeek == Calendar.SATURDAY) {
                isWeekend = true;
            }

            return createNotificationContentDto(tomorrowWeather, false, isWeekend);
        }

        return new NotificationContentDto("Error", "GetForecastWeather failed!", R.drawable.weather_dummy, R.drawable.weather_wallpaper_dummy);
    }

    /**
     * @param weatherList List which is used to get the most next weather condition
     * @return returns NotificationContentDto
     */
    public static WeatherCondition GetMostNextWeatherCondition(@NonNull ArrayList<ForecastPartModel> weatherList) {
        return getMostWeatherCondition(weatherList);
    }

    /**
     * @param weatherList List which is used to get the most next weather condition
     * @param today       flag to create NotificationContentDto for today
     * @param isWeekend   flag to create NotificationContentDto for weekend
     * @return returns NotificationContentDto
     */
    private static NotificationContentDto createNotificationContentDto(@NonNull ArrayList<ForecastPartModel> weatherList, boolean today, boolean isWeekend) {
        WeatherCondition weatherCondition = getMostWeatherCondition(weatherList);

        String notificationForecast;
        double notificationForecastMaxTemp = -273.15;

        if (isWeekend) {
            notificationForecast = weatherCondition.GetWeekendTip();
        } else {
            switch (ForecastDayTime.GetByValue(Calendar.HOUR_OF_DAY)) {
                case AfterWork:
                case Evening:
                case Night:
                    if (today) {
                        notificationForecast = weatherCondition.GetWorkdayAfterWorkTip();
                    } else {
                        notificationForecast = weatherCondition.GetWorkdayAfterWorkTipTomorrow();
                    }
                    break;
                case Morning:
                case Midday:
                case Null:
                default:
                    if (today) {
                        notificationForecast = weatherCondition.GetWorkdayTip();
                    } else {
                        notificationForecast = weatherCondition.GetWorkdayTipTomorrow();
                    }
                    break;
            }
        }

        for (ForecastPartModel model : weatherList) {
            if (model.GetTemperatureMax() > notificationForecastMaxTemp) {
                notificationForecastMaxTemp = model.GetTemperatureMax();
            }
        }

        return new NotificationContentDto(String.format(Locale.getDefault(), "It's getting up to %.1f %sC", notificationForecastMaxTemp, ((char) 0x00B0)), notificationForecast, weatherCondition.GetIcon(), weatherCondition.GetWallpaper());
    }

    /**
     * @param weatherList List which is used to get the most next weather condition in private method
     * @return returns NotificationContentDto
     */
    private static WeatherCondition getMostWeatherCondition(@NonNull ArrayList<ForecastPartModel> weatherList) {
        ArrayList<WeatherCondition> conditionCount = new ArrayList<>();

        WeatherCondition clear = WeatherCondition.Clear;
        WeatherCondition cloud = WeatherCondition.Cloud;
        WeatherCondition drizzle = WeatherCondition.Drizzle;
        WeatherCondition fog = WeatherCondition.Fog;
        WeatherCondition haze = WeatherCondition.Haze;
        WeatherCondition mist = WeatherCondition.Mist;
        WeatherCondition rain = WeatherCondition.Rain;
        WeatherCondition sleet = WeatherCondition.Sleet;
        WeatherCondition snow = WeatherCondition.Snow;
        WeatherCondition squalls = WeatherCondition.Squalls;
        WeatherCondition sun = WeatherCondition.Sun;
        WeatherCondition thunderstorm = WeatherCondition.Thunderstorm;

        for (ForecastPartModel entry : weatherList) {
            if (entry.GetCondition() == WeatherCondition.Clear) {
                clear.IncreaseCount();
            } else if (entry.GetCondition() == WeatherCondition.Cloud) {
                cloud.IncreaseCount();
            } else if (entry.GetCondition() == WeatherCondition.Drizzle) {
                drizzle.IncreaseCount();
            } else if (entry.GetCondition() == WeatherCondition.Fog) {
                fog.IncreaseCount();
            } else if (entry.GetCondition() == WeatherCondition.Haze) {
                haze.IncreaseCount();
            } else if (entry.GetCondition() == WeatherCondition.Mist) {
                mist.IncreaseCount();
            } else if (entry.GetCondition() == WeatherCondition.Rain) {
                rain.IncreaseCount();
            } else if (entry.GetCondition() == WeatherCondition.Sleet) {
                sleet.IncreaseCount();
            } else if (entry.GetCondition() == WeatherCondition.Snow) {
                snow.IncreaseCount();
            } else if (entry.GetCondition() == WeatherCondition.Squalls) {
                squalls.IncreaseCount();
            } else if (entry.GetCondition() == WeatherCondition.Sun) {
                sun.IncreaseCount();
            } else if (entry.GetCondition() == WeatherCondition.Thunderstorm) {
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
        conditionCount.add(squalls);
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
