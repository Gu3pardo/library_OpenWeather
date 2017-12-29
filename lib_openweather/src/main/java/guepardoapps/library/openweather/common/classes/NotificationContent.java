package guepardoapps.library.openweather.common.classes;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Locale;

public class NotificationContent implements Serializable {
    private static final String TAG = NotificationContent.class.getSimpleName();

    private String _title;
    private String _text;
    private int _icon;
    private int _bigIcon;

    public NotificationContent(
            @NonNull String title,
            @NonNull String text,
            int icon,
            int bigIcon) {
        _title = title;
        _text = text;
        _icon = icon;
        _bigIcon = bigIcon;
    }

    public String GetTitle() {
        return _title;
    }

    public String GetText() {
        return _text;
    }

    public int GetIcon() {
        return _icon;
    }

    public int GetBigIcon() {
        return _bigIcon;
    }

    @Override
    public String toString() {
        return String.format(Locale.GERMAN, "{%s:{Title:%s};{Text:%s};{Icon:%d};{BigIcon:%d};}", TAG, _title, _text, _icon, _bigIcon);
    }
}
