package guepardoapps.library.openweather.common.enums;

import java.io.Serializable;

import guepardoapps.library.openweather.R;

public enum WeatherCondition implements Serializable {

    NULL("null", R.drawable.wallpaper_dummy, R.drawable.weather_dummy),
    CLEAR("clear", R.drawable.wallpaper_clear, R.drawable.weather_clear),
    RAIN("rain", R.drawable.wallpaper_rain, R.drawable.weather_rain),
    DRIZZLE("drizzle", R.drawable.wallpaper_drizzle, R.drawable.weather_rain /*TODO*/),
    CLOUD("cloud", R.drawable.wallpaper_cloud, R.drawable.weather_cloud),
    SNOW("snow", R.drawable.wallpaper_snow, R.drawable.weather_snow),
    FOG("fog", R.drawable.wallpaper_fog, R.drawable.weather_fog),
    MIST("mist", R.drawable.wallpaper_mist, R.drawable.weather_fog /*TODO*/),
    SUN("sun", R.drawable.wallpaper_sun, R.drawable.weather_clear /*TODO*/),
    HAZE("haze", R.drawable.wallpaper_haze, R.drawable.weather_haze),
    SLEET("sleet", R.drawable.wallpaper_sleet, R.drawable.weather_sleet),
    THUNDERSTORM("thunderstorm", R.drawable.wallpaper_thunderstorm, R.drawable.weather_thunderstorm);

    private String _value;
    private int _wallpaper;
    private int _icon;

    WeatherCondition(String value, int wallpaper, int icon) {
        _value = value;
        _wallpaper = wallpaper;
        _icon = icon;
    }

    public int GetWallpaper() {
        return _wallpaper;
    }

    public int GetIcon() {
        return _icon;
    }

    @Override
    public String toString() {
        return _value;
    }

    public static WeatherCondition GetByString(String value) {
        for (WeatherCondition entry : WeatherCondition.values()) {
            if (entry.toString().contains(value)
                    || entry.toString().toUpperCase().contains(value)
                    || entry.toString().toLowerCase().contains(value)
                    || value.contains(entry.toString())
                    || value.toUpperCase().contains(entry.toString())
                    || value.toLowerCase().contains(entry.toString())) {
                return entry;
            }
        }

        return WeatherCondition.NULL;
    }

    public static WeatherCondition GetByIcon(int icon) {
        for (WeatherCondition entry : WeatherCondition.values()) {
            if (entry._icon == icon) {
                return entry;
            }
        }

        return WeatherCondition.NULL;
    }
}
