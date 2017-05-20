package guepardoapps.library.openweather.common.model;

import java.io.Serializable;

import guepardoapps.library.toolset.common.Logger;

public class WeatherConditionModel implements Serializable {

    private static final long serialVersionUID = 5283741583914709641L;

    private static final String TAG = WeatherConditionModel.class.getSimpleName();

    private int _count;
    private String _weekendTip;
    private String _workdayTip;
    private int _icon;

    public WeatherConditionModel(int count, String weekendTip, String workdayTip, int icon) {
        Logger logger = new Logger(TAG);
        logger.Debug(TAG + " created...");

        _count = count;
        _weekendTip = weekendTip;
        _workdayTip = workdayTip;
        _icon = icon;

        logger.Debug("_count: " + String.valueOf(_count));
        logger.Debug("_weekendTip: " + _weekendTip);
        logger.Debug("_workdayTip: " + _workdayTip);
        logger.Debug("_icon: " + String.valueOf(_icon));
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

    public int GetIcon() {
        return _icon;
    }

    public void ChangeTipsToTomorrow() {
        _weekendTip = _weekendTip.replace("Today", "Tomorrow").replace("today", "tomorrow").replace("It's",
                "Tomorrow is");
        _workdayTip = _workdayTip.replace("Today", "Tomorrow").replace("today", "tomorrow").replace("It's",
                "Tomorrow is");
    }

    public void IncreaseCount() {
        _count++;
    }

    public void AddCount(int addValue) {
        _count += addValue;
    }

    @Override
    public String toString() {
        return "[WeatherConditionModel: Count: " + String.valueOf(_count) + ", WorkdayTip: " + _workdayTip
                + ", WeekendTip: " + _weekendTip + "]";
    }
}