package guepardoapps.library.openweather.common.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

import guepardoapps.library.openweather.common.tools.Logger;

public class WeatherConditionModel implements Serializable {

    private static final String TAG = WeatherConditionModel.class.getSimpleName();

    private int _count;
    private String _weekendTip;
    private String _workdayTip;
    private String _workdayAfterWorkTip;
    private int _icon;
    private int _wallpaper;

    public WeatherConditionModel(
            int count,
            @NonNull String weekendTip,
            @NonNull String workdayTip,
            @NonNull String workdayAfterWorkTip,
            int icon,
            int wallpaper) {
        Logger logger = new Logger(TAG);
        logger.Debug(TAG + " created...");

        _count = count;
        _weekendTip = weekendTip;
        _workdayTip = workdayTip;
        _workdayAfterWorkTip = workdayAfterWorkTip;
        _icon = icon;
        _wallpaper = wallpaper;

        logger.Debug("_count: " + String.valueOf(_count));
        logger.Debug("_weekendTip: " + _weekendTip);
        logger.Debug("_workdayTip: " + _workdayTip);
        logger.Debug("_workdayAfterWorkTip: " + _workdayAfterWorkTip);
        logger.Debug("_icon: " + String.valueOf(_icon));
        logger.Debug("_wallpaper: " + String.valueOf(_wallpaper));
    }

    public int GetCount() {
        return _count;
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

    public int GetIcon() {
        return _icon;
    }

    public int GetWallpaper() {
        return _wallpaper;
    }

    public void ChangeTipsToTomorrow() {
        _weekendTip = _weekendTip.replace("Today", "Tomorrow").replace("today", "tomorrow").replace("It's", "Tomorrow is");
        _workdayTip = _workdayTip.replace("Today", "Tomorrow").replace("today", "tomorrow").replace("It's", "Tomorrow is");
        _workdayAfterWorkTip = _workdayAfterWorkTip.replace("Today", "Tomorrow").replace("today", "tomorrow").replace("It's", "Tomorrow is");
    }

    public void IncreaseCount() {
        _count++;
    }

    public void AddCount(int addValue) {
        _count += addValue;
    }

    @Override
    public String toString() {
        return "[" + TAG
                + ": Count: " + String.valueOf(_count)
                + ", WeekendTip: " + _weekendTip
                + ", WorkdayTip: " + _workdayTip
                + ", WorkdayAfterWorkTip: " + _workdayAfterWorkTip
                + "]";
    }
}