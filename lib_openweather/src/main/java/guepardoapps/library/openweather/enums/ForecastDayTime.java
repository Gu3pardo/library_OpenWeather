package guepardoapps.library.openweather.enums;

import java.io.Serializable;

public enum ForecastDayTime implements Serializable {

    Null(-1),
    Morning(6),
    Midday(12),
    AfterWork(16),
    Evening(17),
    Night(22);

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
        if (value >= Night.GetValue() || value < Morning.GetValue()) {
            return Night;
        } else if (value >= Morning.GetValue() && value < Midday.GetValue()) {
            return Morning;
        } else if (value >= Midday.GetValue() && value < AfterWork.GetValue()) {
            return Midday;
        } else if (value >= AfterWork.GetValue() && value < Evening.GetValue()) {
            return AfterWork;
        } else if (value >= Evening.GetValue() && value < Night.GetValue()) {
            return Evening;
        } else {
            return Null;
        }
    }
}
