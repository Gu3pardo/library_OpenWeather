package guepardoapps.library.openweather.enums;

import android.support.annotation.NonNull;

import java.io.Serializable;

import guepardoapps.library.openweather.R;

@SuppressWarnings({"unused"})
public enum WeatherCondition implements Serializable {

    NULL(0, "null", R.drawable.weather_wallpaper_dummy, R.drawable.weather_dummy, "", "", "", 0),
    CLEAR(1, "clear", R.drawable.weather_wallpaper_clear, R.drawable.weather_clear,
            "Go to the park or river and enjoy the clear weather today!",
            "Today will be clear! Get out for lunch!",
            "Enjoy your day after work! Today will be clear!",
            0),
    CLOUD(2, "cloud", R.drawable.weather_wallpaper_cloud, R.drawable.weather_cloud,
            "Sun is hiding today.",
            "No sun today. Not bad to work...",
            "No sun after work today, cloudy!",
            0),
    DRIZZLE(3, "drizzle", R.drawable.weather_wallpaper_drizzle, R.drawable.weather_rain /*TODO*/,
            "It's a cold and rainy day!",
            "There will be drizzle today!",
            "There will be drizzle after work today!",
            0),
    FOG(4, "fog", R.drawable.weather_wallpaper_fog, R.drawable.weather_fog,
            "You're not gonna see your hand today! :P",
            "Find your way to work today :P",
            "Find your way to home from work today :P",
            0),
    HAZE(5, "haze", R.drawable.weather_wallpaper_haze, R.drawable.weather_haze,
            "Will be haze today!",
            "Search your way, master!",
            "Haze on your way from work today!",
            0),
    MIST(6, "mist", R.drawable.weather_wallpaper_mist, R.drawable.weather_fog /*TODO*/,
            "Will be misty today!",
            "Watch out today!",
            "Mist on your way from work today!",
            0),
    RAIN(7, "rain", R.drawable.weather_wallpaper_rain, R.drawable.weather_rain,
            "It's a rainy day! Chill at home ;)",
            "It will rain today! Take an umbrella or take your car to work.",
            "It will rain after work today!",
            0),
    SLEET(8, "sleet", R.drawable.weather_wallpaper_sleet, R.drawable.weather_sleet,
            "Today will be a freezy and slittering day!",
            "Take care outside today!",
            "Slittering way home from work today!",
            0),
    SNOW(9, "snow", R.drawable.weather_wallpaper_snow, R.drawable.weather_snow,
            "Today will be a snowy day!",
            "Snow today. Think twice taking your bike!",
            "Today will be a snowy way back from work!",
            0),
    SUN(10, "sun", R.drawable.weather_wallpaper_sun, R.drawable.weather_clear /*TODO*/,
            "Enjoy the sunny weather today and chill!",
            "Today will be sunny! Get out for lunch!",
            "Enjoy your afterwork today! Will be sunny!",
            0),
    THUNDERSTORM(11, "thunderstorm", R.drawable.weather_wallpaper_thunderstorm, R.drawable.weather_thunderstorm,
            "Thunder is coming today!",
            "Prepare for a thunderstorm today!",
            "Today afternoon will be a thunderstorm!",
            0);

    private int _id;

    private String _description;
    private int _wallpaper;
    private int _icon;

    private String _weekendTip;
    private String _workdayTip;
    private String _workdayAfterWorkTip;

    private int _count;

    WeatherCondition(int id, @NonNull String description, int wallpaper, int icon, @NonNull String weekendTip, @NonNull String workdayTip, @NonNull String workdayAfterWorkTip, int count) {
        _id = id;

        _description = description;
        _wallpaper = wallpaper;
        _icon = icon;

        _weekendTip = weekendTip;
        _workdayTip = workdayTip;
        _workdayAfterWorkTip = workdayAfterWorkTip;

        _count = count;
    }

    public int GetId() {
        return _id;
    }

    public String GetDescription() {
        return _description;
    }

    public int GetWallpaper() {
        return _wallpaper;
    }

    public int GetIcon() {
        return _icon;
    }

    public String GetWeekendTip() {
        return _weekendTip;
    }

    public String GetWorkdayTip() {
        return _workdayTip;
    }

    public String GetWorkdayAfterWorkTip() {
        return _workdayAfterWorkTip;
    }

    public int GetCount() {
        return _count;
    }

    public void IncreaseCount() {
        _count++;
    }

    public void ChangeTipsToTomorrow() {
        _weekendTip = _weekendTip.replace("Today", "Tomorrow").replace("today", "tomorrow").replace("It's", "Tomorrow is");
        _workdayTip = _workdayTip.replace("Today", "Tomorrow").replace("today", "tomorrow").replace("It's", "Tomorrow is");
        _workdayAfterWorkTip = _workdayAfterWorkTip.replace("Today", "Tomorrow").replace("today", "tomorrow").replace("It's", "Tomorrow is");
    }

    @Override
    public String toString() {
        return _description;
    }

    public static WeatherCondition GetById(int id) {
        for (WeatherCondition entry : WeatherCondition.values()) {
            if (entry._id == id) {
                return entry;
            }
        }

        return WeatherCondition.NULL;
    }

    public static WeatherCondition GetByDescription(@NonNull String description) {
        for (WeatherCondition entry : WeatherCondition.values()) {
            if (entry._description.contains(description)
                    || entry.toString().toUpperCase().contains(description)
                    || entry.toString().toLowerCase().contains(description)
                    || description.contains(entry.toString())
                    || description.toUpperCase().contains(entry.toString())
                    || description.toLowerCase().contains(entry.toString())) {
                return entry;
            }
        }

        return WeatherCondition.NULL;
    }
}
