package com.taxiexchange.android.broadcast;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.taxiexchange.android.R;
import com.taxiexchange.android.activity.MainActivity;
import com.taxiexchange.android.config.Apps;
import com.taxiexchange.android.config.PreferenceManager;
import com.taxiexchange.android.model.response.ListAuctionResponse;
import com.taxiexchange.android.service.WindowService;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;


/**
 * Created by hieu.nguyennam on 3/17/2017.
 */

public class FirebaseDataReceiver extends WakefulBroadcastReceiver {

    private final String TAG = getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            for (String key : intent.getExtras().keySet()) {
                String value = intent.getExtras().getString(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }

//        try {
//            JSONObject data = new JSONObject(intent.getExtras().getString("data"));
        String bidId  = intent.getExtras().getString("bid_id");
        double ceilingPrice = Double.parseDouble(intent.getExtras().getString("ceiling_price"));
        long hasBill = Long.parseLong(intent.getExtras().getString("co_hoa_don"));
        String departureTime = intent.getExtras().getString("departure_time");
        double directPrice = Double.parseDouble(intent.getExtras().getString("direct_price"));
        String dstLocation = intent.getExtras().getString("dst_location");
        String dueDate = intent.getExtras().getString("due_date");
        long isRoundTrip = Long.parseLong(intent.getExtras().getString("khu_hoi"));
        String bidType = intent.getExtras().getString("loai_bid");
        String taxiType = intent.getExtras().getString("loai_xe");
        String note = intent.getExtras().getString("note");
        long passengerQty = Long.parseLong(intent.getExtras().getString("passenger_qty"));
        double price = Double.parseDouble(intent.getExtras().getString("price"));
        String srcLocation = intent.getExtras().getString("src_location");
        ListAuctionResponse listAuctionResponse = new ListAuctionResponse(bidId, ceilingPrice, hasBill, departureTime,
                directPrice, dstLocation, dueDate, isRoundTrip, bidType, taxiType, note, passengerQty, price, srcLocation);
        Log.e(TAG, listAuctionResponse.toString());
        Log.e(TAG, isServiceRunning(WindowService.class, context) + "");

        boolean sign = PreferenceManager.getBoolean(Apps.IS_SIGN);
        if(sign){
            if(!isServiceRunning(WindowService.class, context)){
                Intent intentService = new Intent(context, WindowService.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Apps.NOTIFICATION_BID_ID, Parcels.wrap(listAuctionResponse));
                intentService.putExtras(bundle);
                context.startService(intentService);
            }
            else {
                PreferenceManager.writePreference(Apps.NOTIFICATION_BID_STATUS, true);
                Intent updateIntent = new Intent(WindowService.ACTION_UPDATE_NOTIFICATION);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Apps.NOTIFICATION_BID_ID, Parcels.wrap(listAuctionResponse));
                updateIntent.putExtras(bundle);
                context.sendBroadcast(updateIntent);
            }

        }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.e(TAG, "JSON parse fail" );
//        }

//        ComponentName comp = new ComponentName(context.getPackageName(),
//                WindowService.class.getName());
//        startWakefulService(context, (intent.setComponent(comp)));
//        setResultCode(Activity.RESULT_OK);


    }

    private boolean isServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
