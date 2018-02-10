package guepardoapps.library.openweather.controller;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import java.util.Locale;

import guepardoapps.library.openweather.datatransferobjects.NotificationContentDto;
import guepardoapps.library.openweather.utils.BitmapHelper;
import guepardoapps.library.openweather.utils.Logger;

public class NotificationController implements INotificationController {
    private static final String Tag = NotificationController.class.getSimpleName();

    private Context _context;
    private NotificationManager _notificationManager;

    public NotificationController(@NonNull Context context) {
        _context = context;
        _notificationManager = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void CreateNotification(int notificationId, Class<?> receiverActivity, @NonNull NotificationContentDto notificationContent) {
        Logger.getInstance().Debug(Tag, String.format(Locale.getDefault(), "CreateNotification with id %d and notificationContent %s", notificationId, notificationContent));

        Bitmap bitmap = BitmapFactory.decodeResource(_context.getResources(), notificationContent.GetBigIcon());
        bitmap = BitmapHelper.GetCircleBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth());

        Notification.Builder builder = new Notification.Builder(_context);
        builder
                .setSmallIcon(notificationContent.GetIcon())
                .setLargeIcon(bitmap)
                .setContentTitle(notificationContent.GetTitle())
                .setContentText(notificationContent.GetText())
                .setTicker(notificationContent.GetText());

        if (receiverActivity != null) {
            Intent intent = new Intent(_context, receiverActivity);
            PendingIntent pendingIntent = PendingIntent.getActivity(_context, notificationId, intent, 0);
            builder.setContentIntent(pendingIntent);
        }

        Notification notification = builder.build();
        _notificationManager.notify(notificationId, notification);
    }

    @Override
    public void CloseNotification(int notificationId) {
        _notificationManager.cancel(notificationId);
    }
}
