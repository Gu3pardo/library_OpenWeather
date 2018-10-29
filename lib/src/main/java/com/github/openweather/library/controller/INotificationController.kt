package com.github.openweather.library.controller

import androidx.annotation.NonNull
import com.github.openweather.library.models.NotificationContent

internal interface INotificationController {
    fun create(@NonNull notificationContent: NotificationContent)
    fun close(id: Int)
}