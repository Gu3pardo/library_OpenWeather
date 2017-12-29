package guepardoapps.library.openweather.common.classes;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;

import guepardoapps.library.openweather.common.utils.Logger;

public class SerializableTime implements Serializable {
    private static final String TAG = SerializableTime.class.getSimpleName();

    private int _hour;
    private int _minute;
    private int _second;
    private int _millisecond;

    public SerializableTime(int hour, int minute, int second, int millisecond) {
        _hour = hour;
        _minute = minute;
        _second = second;
        _millisecond = millisecond;
    }

    public SerializableTime(@NonNull String time) {
        String[] timeArray = time.split("\\:");
        if (timeArray.length == 4) {
            try {
                _hour = Integer.parseInt(timeArray[0].replace(":", ""));
                _minute = Integer.parseInt(timeArray[1].replace(":", ""));
                _second = Integer.parseInt(timeArray[2].replace(":", ""));
                _millisecond = Integer.parseInt(timeArray[3].replace(":", ""));
            } catch (Exception exception) {
                Logger.getInstance().Error(TAG, exception.getMessage());
                setDefaultValues();
            }
        } else if (timeArray.length == 3) {
            try {
                _hour = Integer.parseInt(timeArray[0].replace(":", ""));
                _minute = Integer.parseInt(timeArray[1].replace(":", ""));
                _second = Integer.parseInt(timeArray[2].replace(":", ""));
                _millisecond = 0;
            } catch (Exception exception) {
                Logger.getInstance().Error(TAG, exception.getMessage());
                setDefaultValues();
            }
        } else if (timeArray.length == 2) {
            try {
                _hour = Integer.parseInt(timeArray[0].replace(":", ""));
                _minute = Integer.parseInt(timeArray[1].replace(":", ""));
                _second = 0;
                _millisecond = 0;
            } catch (Exception exception) {
                Logger.getInstance().Error(TAG, exception.getMessage());
                setDefaultValues();
            }
        } else if (timeArray.length == 1) {
            try {
                _hour = Integer.parseInt(timeArray[0].replace(":", ""));
                _minute = 0;
                _second = 0;
                _millisecond = 0;
            } catch (Exception exception) {
                Logger.getInstance().Error(TAG, exception.getMessage());
                setDefaultValues();
            }
        } else {
            Logger.getInstance().Warning(TAG, String.format(Locale.getDefault(), "Invalid data count %d!", timeArray.length));
            setDefaultValues();
        }
    }

    public SerializableTime() {
        setDefaultValues();
    }

    public int Hour() {
        return _hour;
    }

    public int Minute() {
        return _minute;
    }

    public int Second() {
        return _second;
    }

    public int Millisecond() {
        return _millisecond;
    }

    public int toMilliSecond() {
        int hourTo = _hour * 60 * 60 * 1000;
        int minuteTo = _minute * 60 * 1000;
        int secondTo = _second * 1000;
        int milliSecondTo = _millisecond;

        return hourTo + minuteTo + secondTo + milliSecondTo;
    }

    public String HH() {
        return String.format(Locale.getDefault(), "%02d", _hour);
    }

    public String MM() {
        return String.format(Locale.getDefault(), "%02d", _minute);
    }

    public String SS() {
        return String.format(Locale.getDefault(), "%02d", _second);
    }

    public String mm() {
        return String.format(Locale.getDefault(), "%04d", _millisecond);
    }

    public String HHMM() {
        return HH() + ":" + MM();
    }

    public String HHMMSS() {
        return HH() + ":" + MM() + ":" + SS();
    }

    public String HHMMSSmm() {
        return HH() + ":" + MM() + ":" + SS() + ":" + mm();
    }

    public boolean isAfter(@NonNull SerializableTime compareTime) {
        return compareTime.toMilliSecond() > toMilliSecond();
    }

    public boolean isBefore(@NonNull SerializableTime compareTime) {
        return compareTime.toMilliSecond() < toMilliSecond();
    }

    public boolean isAfterNow() {
        return toMilliSecond() > calculateMillisOfDay();
    }

    public boolean isBeforeNow() {
        return toMilliSecond() < calculateMillisOfDay();
    }

    @Override
    public String toString() {
        return HHMMSSmm();
    }

    private int calculateMillisOfDay() {
        Calendar now = Calendar.getInstance();

        int hourOfDay = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int second = now.get(Calendar.SECOND);
        int millisecond = now.get(Calendar.MILLISECOND);

        return hourOfDay * 60 * 60 * 1000 + minute * 60 * 1000 + second * 1000 + millisecond;
    }

    private void setDefaultValues() {
        int millisecond = calculateMillisOfDay();
        int second = millisecond / 1000;
        int minute = second / 60;
        _millisecond = millisecond % 1000;
        _second = second % 60;
        _minute = minute % 60;
        _hour = minute / 60;
    }
}
