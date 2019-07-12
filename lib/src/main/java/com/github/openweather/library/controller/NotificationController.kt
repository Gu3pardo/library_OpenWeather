package com.github.openweather.library.controller

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.annotation.NonNull
import com.github.openweather.library.extensions.circleBitmap
import com.github.openweather.library.models.NotificationContent

internal class NotificationController(@NonNull private val context: Context) : INotificationController {

    private val channelId: String = "com.github.openweather.library"

    private val channelName: String = "OpenWeather"

    private val channelDescription: String = "Notifications for open weather library"

    private var notificationManager: NotificationManager? = null

    init {
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
    }

    @Suppress("DEPRECATION")
    override fun create(notificationContent: NotificationContent) {
        val intent = Intent(context, notificationContent.receiver)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        var bitmap = BitmapFactory.decodeResource(context.resources, notificationContent.largeIconId)
        bitmap = bitmap.circleBitmap(bitmap.height, bitmap.width, Color.BLACK)
        val wearableExtender = Notification.WearableExtender().setHintHideIcon(true).setBackground(bitmap)

        val notification: Notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = Notification.Builder(context, channelId)
                    .setContentTitle(notificationContent.title)
                    .setContentText(notificationContent.text)
                    .setSmallIcon(notificationContent.iconId)
                    .setLargeIcon(bitmap)
                    .setChannelId(channelId)
                    .setContentIntent(pendingIntent)
                    .extend(wearableExtender)
                    .setAutoCancel(notificationContent.cancelable)
                    .build()
        } else {
            notification = Notification.Builder(context)
                    .setContentTitle(notificationContent.title)
                    .setContentText(notificationContent.text)
                    .setSmallIcon(notificationContent.iconId)
                    .setLargeIcon(bitmap)
                    .setContentIntent(pendingIntent)
                    .extend(wearableExtender)
                    .setAutoCancel(notificationContent.cancelable)
                    .build()
        }

        notificationManager?.notify(notificationContent.id, notification)
    }

    override fun close(id: Int) {
        notificationManager?.cancel(id)
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() = notificationManager?.createNotificationChannel(
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW).apply {
                description = channelDescription
                enableLights(false)
                enableVibration(false)
            })
}