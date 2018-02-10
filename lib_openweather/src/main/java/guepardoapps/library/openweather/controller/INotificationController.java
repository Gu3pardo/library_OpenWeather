package guepardoapps.library.openweather.controller;

import android.support.annotation.NonNull;

import guepardoapps.library.openweather.datatransferobjects.NotificationContentDto;

public interface INotificationController {
    int NotificationIdCurrentWeather = 311295;
    int NotificationIdForecastWeather = 311292;

    void CreateNotification(int notificationId, Class<?> receiverActivity, @NonNull NotificationContentDto notificationContent);

    void CloseNotification(int notificationId);
}
