package guepardoapps.lib.openweather.controller

import android.support.annotation.NonNull
import guepardoapps.lib.openweather.models.NotificationContent

internal interface INotificationController {
    fun create(@NonNull notificationContent: NotificationContent)
    fun close(id: Int)
}