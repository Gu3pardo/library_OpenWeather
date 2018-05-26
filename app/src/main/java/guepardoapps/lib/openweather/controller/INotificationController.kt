package guepardoapps.lib.openweather.controller

import android.support.annotation.NonNull
import guepardoapps.lib.openweather.models.INotificationContent

interface INotificationController {
    fun create(@NonNull notificationContent: INotificationContent)

    fun close()
}