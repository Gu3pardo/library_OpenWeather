package guepardoapps.library.openweather.controller;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import guepardoapps.library.openweather.common.classes.NotificationContent;
import guepardoapps.library.openweather.common.utils.Tools;

public class NotificationController {
    private Context _context;
    private NotificationManager _notificationManager;

    public NotificationController(@NonNull Context context) {
        _context = context;
        _notificationManager = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void CreateNotification(
            int id,
            Class<?> receiverActivity,
            @NonNull NotificationContent notificationContent) {
        Bitmap bitmap = BitmapFactory.decodeResource(_context.getResources(), notificationContent.GetBigIcon());
        bitmap = Tools.GetCircleBitmap(bitmap);

        Notification.Builder builder = new Notification.Builder(_context);
        builder
                .setSmallIcon(notificationContent.GetIcon())
                .setLargeIcon(bitmap)
                .setContentTitle(notificationContent.GetTitle())
                .setContentText(notificationContent.GetText())
                .setTicker(notificationContent.GetText());

        if (receiverActivity != null) {
            Intent intent = new Intent(_context, receiverActivity);
            PendingIntent pendingIntent = PendingIntent.getActivity(_context, id, intent, 0);
            builder.setContentIntent(pendingIntent);
        }

        Notification notification = builder.build();
        _notificationManager.notify(id, notification);
    }

    public void CloseNotification(int notificationId) {
        _notificationManager.cancel(notificationId);
    }
}
