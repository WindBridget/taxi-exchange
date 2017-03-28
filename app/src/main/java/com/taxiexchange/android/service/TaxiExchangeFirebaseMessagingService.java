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
    }

}
