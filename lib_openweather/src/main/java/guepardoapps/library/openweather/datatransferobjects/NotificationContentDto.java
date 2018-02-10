package guepardoapps.library.openweather.datatransferobjects;

import android.support.annotation.NonNull;

import java.util.Locale;

public class NotificationContentDto implements INotificationContentDto {
    private static final String Tag = NotificationContentDto.class.getSimpleName();

    private String _title;
    private String _text;
    private int _icon;
    private int _bigIcon;

    public NotificationContentDto(@NonNull String title, @NonNull String text, int icon, int bigIcon) {
        _title = title;
        _text = text;
        _icon = icon;
        _bigIcon = bigIcon;
    }

    @Override
    public void SetTitle(@NonNull String title) {
        _title = title;
    }

    @Override
    public String GetTitle() {
        return _title;
    }

    @Override
    public void SetText(@NonNull String text) {
        _text = text;
    }

    @Override
    public String GetText() {
        return _text;
    }

    @Override
    public void SetIcon(int icon) {
        _icon = icon;
    }

    @Override
    public int GetIcon() {
        return _icon;
    }

    @Override
    public void SetBigIcon(int bigIcon) {
        _bigIcon = bigIcon;
    }

    @Override
    public int GetBigIcon() {
        return _bigIcon;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "{\"Class\":\"%s\",\"Title\":\"%s\",\"Text\":\"%s\",\"Icon\":%d,\"BigIcon\":%d}",
                Tag, _title, _text, _icon, _bigIcon);
    }
}
