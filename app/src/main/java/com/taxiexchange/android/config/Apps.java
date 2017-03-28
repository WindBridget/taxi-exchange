package com.taxiexchange.android.config;

import android.annotation.SuppressLint;
import android.provider.Settings;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

/**
 * Created by hieu.nguyennam on 3/14/2017.
 */
public class Apps extends MultiDexApplication{

    private static Apps mInstances;
    private final String TAG = getClass().getSimpleName();

    public static final String DEVICE_ID = "DEVICE_ID";
    public static final String TOKEN_ID = "TOKEN_ID";
    public static final String USER_MOBILE = "USER_MOBILE";
    public static final String IS_SIGN = "IS_SIGN";
    public static final String LOGIN_TOKEN = "LOGIN_TOKEN";
    public static final String ITEM_BID_ID = "ITEM_BID_ID";
    public static final String NOTIFICATION_BID_ID = "NOTIFICATION_BID_ID";
    public static final String NOTIFICATION_BID_STATUS = "NOTIFICATION_BID_STATUS";
    public static final String NOTIFICATION_TITLE = "NOTIFICATION_TITLE";
    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String USER_INFO_STATE = "user_info_state";
    public static final String USER_INFO_NAME = "user_info_name";
    public static final String USER_INFO_MOBILE = "user_info_mobile";
    public static final String USER_INFO_CAR = "user_info_car";
    public static final String USER_INFO_AMOUNT = "user_info_amount";

    public static final String AGO = " ago";
    public static final String YESTERDAY = "Yesterday";
    public static final String HOUR = " hour";
    public static final String MINUTE = " minute";
    public static final String DAYS = " days";

    @Override
    public void onCreate() {
        super.onCreate();
        mInstances = this;
        Log.e(TAG, "on app create");
        PreferenceManager.getInstance();
        getDeviceId();
    }

    private void getDeviceId() {
        @SuppressLint("HardwareIds")
        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        PreferenceManager.writePreference(DEVICE_ID, android_id);
        Log.e(TAG, "deviceId = " + android_id);
    }

    public static synchronized Apps getInstances() {
        return mInstances;
    }
}
