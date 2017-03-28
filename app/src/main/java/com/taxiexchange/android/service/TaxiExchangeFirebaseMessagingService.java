package com.taxiexchange.android.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.taxiexchange.android.R;
import com.taxiexchange.android.activity.MainActivity;
import com.taxiexchange.android.config.Apps;
import com.taxiexchange.android.config.PreferenceManager;

/**
 * Created by hieu.nguyennam on 3/14/2017.
 */
public class TaxiExchangeFirebaseMessagingService  extends FirebaseMessagingService {
    private static final String TAG = TaxiExchangeFirebaseMessagingService.class.getSimpleName();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData());

        }
//        sendNotification();
    }

    private void sendNotification() {
        Log.e(TAG, "In sendNotification");
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setAction(Long.toString(System.currentTimeMillis()));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
      /*  Uri notificationSound
                = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://"
                + getApplication().getPackageName()
                + "/"
                + R.raw.notification);*/
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_t_icon)
                .setContentTitle("Bạn vừa nhận được cuốc đấu giá từ Xelienket")
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setContentText("Từ-> đến. GIá tiền")
                .setAutoCancel(true)
                .setPriority(1)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Apps.NOTIFICATION_ID, notificationBuilder.build());
    }
}
