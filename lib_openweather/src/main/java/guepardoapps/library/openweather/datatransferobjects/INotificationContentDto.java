package guepardoapps.library.openweather.datatransferobjects;

import android.support.annotation.NonNull;

import java.util.UUID;

public interface INotificationContentDto {
    void SetTitle(@NonNull String title);

    String GetTitle();

    void SetText(@NonNull String text);

    String GetText();

    void SetIcon(int icon);

    int GetIcon();

    void SetBigIcon(int bigIcon);

    int GetBigIcon();
}
