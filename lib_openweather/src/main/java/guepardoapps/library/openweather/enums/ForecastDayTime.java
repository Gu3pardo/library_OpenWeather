package guepardoapps.library.openweather.enums;

import java.io.Serializable;

public enum ForecastDayTime implements Serializable {

    NULL(-1),
    MORNING(6),
    MIDDAY(12),
    AFTERWORK(16),
    EVENING(17),
    NIGHT(22);

    private int _value;

    ForecastDayTime(int value) {
        _value = value;
    }

    public int GetValue() {
        return _value;
    }

    @Override
    public String toString() {
        return String.valueOf(_value);
    }

    public static ForecastDayTime GetByValue(int value) {
        if (value >= NIGHT.GetValue() || value < MORNING.GetValue()) {
            return NIGHT;
        } else if (value >= MORNING.GetValue() && value < MIDDAY.GetValue()) {
            return MORNING;
        } else if (value >= MIDDAY.GetValue() && value < AFTERWORK.GetValue()) {
            return MIDDAY;
        } else if (value >= AFTERWORK.GetValue() && value < EVENING.GetValue()) {
            return AFTERWORK;
        } else if (value >= EVENING.GetValue() && value < NIGHT.GetValue()) {
            return EVENING;
        } else {
            return NULL;
        }
    }
}
