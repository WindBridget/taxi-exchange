package com.taxiexchange.android.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.taxiexchange.android.R;
import com.taxiexchange.android.activity.MainActivity;
import com.taxiexchange.android.activity.adapters.NothingSelectedSpinnerAdapter;
import com.taxiexchange.android.config.Apps;
import com.taxiexchange.android.config.PreferenceManager;
import com.taxiexchange.android.config.retrofit.RestClient;
import com.taxiexchange.android.config.retrofit.TaxiExchangeApi;
import com.taxiexchange.android.model.TimeQuestion;
import com.taxiexchange.android.model.request.AuctionRequest;
import com.taxiexchange.android.model.response.AuctionResponse;
import com.taxiexchange.android.model.response.ListAuctionResponse;
import com.taxiexchange.android.ulti.TaxiExchangeTimeUtils;

import org.parceler.Parcels;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hieu.nguyennam on 3/17/2017.
 */

public class WindowService extends Service {

    private final String TAG = getClass().getSimpleName();
    public static final String ACTION_UPDATE_NOTIFICATION = "ACTION_UPDATE_NOTIFICATION";
    private WindowManager mWindowManager;
    private MediaPlayer mMediaPlayer;
    private View mNotificationView;
    private ListAuctionResponse mListAuctionResponse;
    private String notificationTitle;
    private TaxiExchangeApi mApi;
    private boolean isRunning;

    @BindView(R.id.item_date_detail)
    TextView textDate;

    @BindView(R.id.item_time_detail)
    TextView textTime;

    @BindView(R.id.item_bid_id)
    TextView textBidId;

    @BindView(R.id.item_taxi_type)
    TextView textTaxiType;

    @BindView(R.id.item_total_people)
    TextView textTotalPeople;

    @BindView(R.id.item_from_location_detail)
    TextView textFromLocation;

    @BindView(R.id.item_round_trip)
    ImageView imageRoundTrip;

    @BindView(R.id.item_not_round_trip)
    ImageView imageNotRoundTrip;

    @BindView(R.id.item_to_location_detail)
    TextView textToLocation;

    @BindView(R.id.item_has_bill)
    TextView textHasBill;

    @BindView(R.id.item_ceiling_price_detail)
    TextView textCeilingPrice;

    @BindView(R.id.item_bid_type_direct)
    LinearLayout linearLayoutBidType;

    @BindView(R.id.item_bid_type)
    TextView textBidType;

    @BindView(R.id.item_direct_price_detail)
    TextView textDirectPrice;

    @BindView(R.id.item_note_detail)
    TextView textNote;

    @BindView(R.id.item_time_remaining)
    TextView textTimeRemaining;

    @BindView((R.id.bid_spinner))
    Spinner spinner;

    @BindView(R.id.item_time_remaining_status)
    TextView textTimeRemainingStatus;

    @BindView(R.id.btn_accept)
    Button btnAccept;

    @BindView(R.id.btn_direct_bid)
    Button btnDirectBid;

    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        try {
            AssetFileDescriptor descriptor = getAssets().openFd("notification.mp3");
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                mMediaPlayer.setLooping(false);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (Exception e) {

            Log.d(TAG, e.toString());
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "in start Service");
        isRunning = PreferenceManager.getBoolean(Apps.NOTIFICATION_BID_STATUS);
        Log.e(TAG, isRunning +"");
//        if(!isRunning){
            Log.e(TAG, "get start Service");
            getData(intent);
            initAPI();
            addNotificationView();
            PreferenceManager.writePreference(Apps.NOTIFICATION_BID_STATUS, true);
//        }else{
//            Log.e(TAG, "not start Service");
//        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_UPDATE_NOTIFICATION);
        registerReceiver(mNotificationReceiver, intentFilter);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNotificationReceiver);
        Log.e(TAG, "in detroy Service");;
        stop();
    }

    private void getData(Intent intent){
        Bundle bundle = intent.getExtras();
        mListAuctionResponse = Parcels.unwrap(bundle.getParcelable(Apps.NOTIFICATION_BID_ID));
    }

    private void initAPI() {
        mApi = RestClient.getClient().create(TaxiExchangeApi.class);
    }

    private void addNotificationView() {
        mNotificationView = LayoutInflater.from(this).inflate(R.layout.notification, null);
        ButterKnife.bind(this, mNotificationView);
        initSpinner();

        //row 1
        if (mListAuctionResponse.getDepartureTime() != null) {
            TimeQuestion time = TaxiExchangeTimeUtils.convertInputTime(mListAuctionResponse.getDepartureTime());
            if (time != null) {
                String strDate = time.getDate();
                String strTime = time.getTime();
                textDate.setText(strDate);
                textTime.setText(strTime);
            }
        }
        if (mListAuctionResponse.getBidId() != null) {
            textBidId.setText(" | " + mListAuctionResponse.getBidId());
        }
        //row 2
        if (mListAuctionResponse.getTaxiType() != null) {
            textTaxiType.setText(mListAuctionResponse.getTaxiType());
        }

        textTotalPeople.setText(String.valueOf(mListAuctionResponse.getPassengerQty()) + getString(R.string.txt_total_people));
        //row 3
        if (mListAuctionResponse.getSrcLocation() != null) {
            textFromLocation.setText(mListAuctionResponse.getSrcLocation());
        }

        if (mListAuctionResponse.getDstLocation() != null) {
            textToLocation.setText(mListAuctionResponse.getDstLocation());
        }
        if(mListAuctionResponse.getIsRoundTrip() == 1){
            imageRoundTrip.setVisibility(View.VISIBLE);
            imageNotRoundTrip.setVisibility(View.GONE);
        }
        else{
            imageRoundTrip.setVisibility(View.GONE);
            imageNotRoundTrip.setVisibility(View.VISIBLE);
        }
        //row 4
        if(mListAuctionResponse.getHasBill() == 1)
            textHasBill.setText(R.string.txt_has_bill);
        else
            textHasBill.setText(R.string.txt_dont_have_bill);

        textCeilingPrice.setText(String.format("%,d", (int)(mListAuctionResponse.getCeilingPrice()/1000)) + getString(R.string.txt_vnd));
        //row 5
        if (mListAuctionResponse.getBidType().equals("direct")) {
            linearLayoutBidType.setVisibility(View.VISIBLE);
            btnDirectBid.setVisibility(View.VISIBLE);
            textBidType.setText(R.string.txt_direct_offer);
            textDirectPrice.setText(String.format("%,d", (int) (mListAuctionResponse.getDirectPrice() / 1000)) + getString(R.string.txt_vnd));
        }
        //row 6
        if (mListAuctionResponse.getNote() != null && !mListAuctionResponse.getNote().equals("false")) {
            textNote.setVisibility(View.VISIBLE);
            textNote.setText(getString(R.string.txt_note) + mListAuctionResponse.getNote());
        }
        //row 7
        if (mListAuctionResponse.getDueDate() != null) {
            try {
                long timeSub = TaxiExchangeTimeUtils.getTimeRemaining(mListAuctionResponse.getDueDate());
                new CountDownTimer(timeSub , 1000){
                    @Override
                    public void onTick(long millisUntilFinished) {

                        long timeSub = millisUntilFinished / 1000;

                        int day = (int) (timeSub / (24 * 60 * 60));
                        timeSub -= day * (24 * 60 * 60);
                        int hour = (int) timeSub / (60 * 60);
                        timeSub -= hour * (60 * 60);
                        int minute = (int) timeSub / 60;
                        timeSub -= minute * 60;
                        int second = (int)timeSub;
                        textTimeRemaining.setText(day + getString(R.string.day) +
                                hour + getString(R.string.hour) +
                                minute + getString(R.string.minute) +
                                second + getString(R.string.second));
                    }
                    @Override
                    public void onFinish() {
                        textTimeRemaining.setText(R.string.txt_time_remaining_out);
                    }
                }.start();
            } catch (Exception e) {

                System.out.println(e.toString());
            }
        }
        //row 8 : spinner

        //row 9

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long timeSub = TaxiExchangeTimeUtils.getTimeRemaining(mListAuctionResponse.getDueDate());
                if(timeSub <=0){
                    textTimeRemainingStatus.setText(getString(R.string.txt_time_remaining_status));
                }else{
                    String bidId = mListAuctionResponse.getBidId();
                    String driverId = PreferenceManager.getString(Apps.USER_MOBILE);
                    double priceOffer;
                    priceOffer = Double.parseDouble(String.valueOf(spinner.getSelectedItem()).replace("K", "")) * 1000;

                    AuctionRequest auctionRequest = new AuctionRequest(priceOffer, bidId, driverId);

                    String token = PreferenceManager.getString(Apps.LOGIN_TOKEN);
                    Log.e(TAG, "create = " + auctionRequest + " " + token);
                    Call<AuctionResponse> auctionResponseCall = mApi.postAuction(token, auctionRequest);
                    auctionResponseCall.enqueue(new Callback<AuctionResponse>() {
                        @Override
                        public void onResponse(Call<AuctionResponse> call, Response<AuctionResponse> response) {
                            Log.d(TAG, "Post auction body = " + response.body());
                            Log.d(TAG, "Post auction code = " + response.code());
                            if (response.body() == null) {
                                Log.d(TAG, "Post auction Success Stt = " + response.isSuccessful());
                                return;
                            }else{
                                Log.d(TAG, "Post auction Success Stt = " + response.isSuccessful());
                                AuctionResponse auctionResponse = response.body();
                                Log.d(TAG, "Post auction Success = " + auctionResponse.getMessage());
                            }
                        }
                        @Override
                        public void onFailure(Call<AuctionResponse> call, Throwable t) {
                            Log.d(TAG, "Post auction fail !");
                        }
                    });
                    Toast.makeText(mNotificationView.getContext(), "Đấu giá đã gửi", Toast.LENGTH_SHORT).show();
                    removeAndStopService();
                }

            }
        });
        btnDirectBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                long timeSub = TaxiExchangeTimeUtils.getTimeRemaining(mListAuctionResponse.getDueDate());
                if(timeSub <=0){
                    textTimeRemainingStatus.setText(getString(R.string.txt_time_remaining_status));
                }else{
                    String bidId = mListAuctionResponse.getBidId();
                    String driverId = PreferenceManager.getString(Apps.USER_MOBILE);
                    double priceOffer;
                    priceOffer = mListAuctionResponse.getDirectPrice();
                    AuctionRequest auctionRequest = new AuctionRequest(priceOffer, bidId, driverId);
                    Log.e(TAG, "create = " + auctionRequest);
                    String token = PreferenceManager.getString(Apps.LOGIN_TOKEN);
                    Call<AuctionResponse> auctionResponseCall = mApi.postAuctionDirectly(token, auctionRequest);
                    auctionResponseCall.enqueue(new Callback<AuctionResponse>() {
                        @Override
                        public void onResponse(Call<AuctionResponse> call, Response<AuctionResponse> response) {
                            Log.d(TAG, "Post auction body = " + response.body());
                            Log.d(TAG, "Post auction code = " + response.code());
                            if (response.body() == null) {
                                Log.d(TAG, "Post auction Success Stt = " + response.isSuccessful());
                                return;
                            }else{
                                Log.d(TAG, "Post auction Success Stt = " + response.isSuccessful());
                                AuctionResponse auctionResponse = response.body();
                                Log.d(TAG, "Post auction Success = " + auctionResponse.getMessage());
                            }
                        }
                        @Override
                        public void onFailure(Call<AuctionResponse> call, Throwable t) {
                            Log.d(TAG, "Post auction fail !");
                        }
                    });
                    Toast.makeText(mNotificationView.getContext(), "Mua thẳng đã gửi", Toast.LENGTH_SHORT).show();
                    removeAndStopService();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeAndStopService();
            }
        });
        Log.d(TAG, "add view");
        addViewToWindow(mNotificationView);
        String notificationBody = "Từ: " + mListAuctionResponse.getSrcLocation() + " Đến: " + mListAuctionResponse.getDstLocation();
//        sendNotification(notificationBody);
        play();
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();
    }


    private void initSpinner(){
        List<String> spinnerList = new ArrayList();
        for(int i = (int) (mListAuctionResponse.getCeilingPrice()/1000); i > 0; i -= 10 ){
            spinnerList.add(String.format("%,d", i) + " K");
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter( mNotificationView.getContext(),
                android.R.layout.simple_spinner_item, spinnerList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt("Bid giá");
        spinner.getBackground().setColorFilter(Color.parseColor("#AA7744"), PorterDuff.Mode.SRC_ATOP);
        spinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        dataAdapter,
                        R.layout.contact_spinner_row_nothing_selected,
//                        R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        mNotificationView.getContext()));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Object item = parent.getItemAtPosition(position);
                if (item != null) {
                    btnAccept.setEnabled(true);
                    ((TextView) view).setTextColor(Color.BLACK);
                }
                else {
                    btnAccept.setEnabled(false);
                    ((TextView) view).setTextColor(Color.GRAY);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private BroadcastReceiver mNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(ACTION_UPDATE_NOTIFICATION)){
                Log.d(TAG, "ACTION_UPDATE_NOTIFICATION");
                if (mNotificationView != null && mNotificationView.getWindowToken() != null) {
                    Bundle bundle = intent.getExtras();
                    final ListAuctionResponse listAuctionResponse = Parcels.unwrap(bundle.getParcelable(Apps.NOTIFICATION_BID_ID));
                    if (listAuctionResponse == null) {
                        return;
                    }
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            if (listAuctionResponse.getDepartureTime() != null) {
                                TimeQuestion time = TaxiExchangeTimeUtils.convertInputTime(listAuctionResponse.getDepartureTime());
                                if (time != null) {
                                    String strDate = time.getDate();
                                    String strTime = time.getTime();
                                    textDate.setText(strDate);
                                    textTime.setText(strTime);
                                }
                            }
                            if (listAuctionResponse.getBidId() != null) {
                                textBidId.setText(" | " + listAuctionResponse.getBidId());
                            }
                            //row 2
                            if (listAuctionResponse.getTaxiType() != null) {
                                textTaxiType.setText(listAuctionResponse.getTaxiType());
                            }

                            textTotalPeople.setText(String.valueOf(listAuctionResponse.getPassengerQty()) + getString(R.string.txt_total_people));
                            //row 3
                            if (listAuctionResponse.getSrcLocation() != null) {
                                textFromLocation.setText(listAuctionResponse.getSrcLocation());
                            }

                            if (listAuctionResponse.getDstLocation() != null) {
                                textToLocation.setText(listAuctionResponse.getDstLocation());
                            }
                            if(listAuctionResponse.getIsRoundTrip() == 1){
                                imageRoundTrip.setVisibility(View.VISIBLE);
                                imageNotRoundTrip.setVisibility(View.GONE);
                            }
                            else{
                                imageRoundTrip.setVisibility(View.GONE);
                                imageNotRoundTrip.setVisibility(View.VISIBLE);
                            }
                            //row 4
                            if(listAuctionResponse.getHasBill() == 1)
                                textHasBill.setText(R.string.txt_has_bill);
                            else
                                textHasBill.setText(R.string.txt_dont_have_bill);

                            textCeilingPrice.setText(String.format("%,d", (int)(listAuctionResponse.getCeilingPrice()/1000)) + getString(R.string.txt_vnd));
                            //row 5
                            if (listAuctionResponse.getBidType().equals("direct")) {
                                linearLayoutBidType.setVisibility(View.VISIBLE);
                                btnDirectBid.setVisibility(View.VISIBLE);
                                textBidType.setText(R.string.txt_direct_offer);
                                textDirectPrice.setText(String.format("%,d", (int) (listAuctionResponse.getDirectPrice() / 1000)) + getString(R.string.txt_vnd));
                            }
                            //row 6
                            if (listAuctionResponse.getNote() != null && !listAuctionResponse.getNote().equals("false")) {
                                textNote.setVisibility(View.VISIBLE);
                                textNote.setText(getString(R.string.txt_note) + listAuctionResponse.getNote());
                            }
                            //row 7
                            if (listAuctionResponse.getDueDate() != null) {
                                try {
                                    long timeSub = TaxiExchangeTimeUtils.getTimeRemaining(listAuctionResponse.getDueDate());
                                    new CountDownTimer(timeSub , 1000){
                                        @Override
                                        public void onTick(long millisUntilFinished) {

                                            long timeSub = millisUntilFinished / 1000;

                                            int day = (int) (timeSub / (24 * 60 * 60));
                                            timeSub -= day * (24 * 60 * 60);
                                            int hour = (int) timeSub / (60 * 60);
                                            timeSub -= hour * (60 * 60);
                                            int minute = (int) timeSub / 60;
                                            timeSub -= minute * 60;
                                            int second = (int)timeSub;
                                            textTimeRemaining.setText(day + getString(R.string.day) +
                                                    hour + getString(R.string.hour) +
                                                    minute + getString(R.string.minute) +
                                                    second + getString(R.string.second));
                                        }
                                        @Override
                                        public void onFinish() {
                                            textTimeRemaining.setText(R.string.txt_time_remaining_out);
                                        }
                                    }.start();
                                } catch (Exception e) {

                                    System.out.println(e.toString());
                                }
                            }
                            //row 8 : spinner

                            //row 9

                            btnAccept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    long timeSub = TaxiExchangeTimeUtils.getTimeRemaining(listAuctionResponse.getDueDate());
                                    if(timeSub <=0){
                                        textTimeRemainingStatus.setText(getString(R.string.txt_time_remaining_status));
                                    }else{
                                        String bidId = listAuctionResponse.getBidId();
                                        String driverId = PreferenceManager.getString(Apps.USER_MOBILE);
                                        double priceOffer;
                                        priceOffer = Double.parseDouble(String.valueOf(spinner.getSelectedItem()).replace("K", "")) * 1000;

                                        AuctionRequest auctionRequest = new AuctionRequest(priceOffer, bidId, driverId);

                                        String token = PreferenceManager.getString(Apps.LOGIN_TOKEN);
                                        Log.e(TAG, "create = " + auctionRequest + " " + token);
                                        Call<AuctionResponse> auctionResponseCall = mApi.postAuction(token, auctionRequest);
                                        auctionResponseCall.enqueue(new Callback<AuctionResponse>() {
                                            @Override
                                            public void onResponse(Call<AuctionResponse> call, Response<AuctionResponse> response) {
                                                Log.d(TAG, "Post auction body = " + response.body());
                                                Log.d(TAG, "Post auction code = " + response.code());
                                                if (response.body() == null) {
                                                    Log.d(TAG, "Post auction Success Stt = " + response.isSuccessful());
                                                    return;
                                                }else{
                                                    Log.d(TAG, "Post auction Success Stt = " + response.isSuccessful());
                                                    AuctionResponse auctionResponse = response.body();
                                                    Log.d(TAG, "Post auction Success = " + auctionResponse.getMessage());
                                                }
                                            }
                                            @Override
                                            public void onFailure(Call<AuctionResponse> call, Throwable t) {
                                                Log.d(TAG, "Post auction fail !");
                                            }
                                        });
                                        Toast.makeText(mNotificationView.getContext(), "Đấu giá đã gửi", Toast.LENGTH_SHORT).show();
                                        removeAndStopService();
                                    }

                                }
                            });
                            btnDirectBid.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    long timeSub = TaxiExchangeTimeUtils.getTimeRemaining(listAuctionResponse.getDueDate());
                                    if(timeSub <=0){
                                        textTimeRemainingStatus.setText(getString(R.string.txt_time_remaining_status));
                                    }else{
                                        String bidId = listAuctionResponse.getBidId();
                                        String driverId = PreferenceManager.getString(Apps.USER_MOBILE);
                                        double priceOffer;
                                        priceOffer = listAuctionResponse.getDirectPrice();
                                        AuctionRequest auctionRequest = new AuctionRequest(priceOffer, bidId, driverId);
                                        Log.e(TAG, "create = " + auctionRequest);
                                        String token = PreferenceManager.getString(Apps.LOGIN_TOKEN);
                                        Call<AuctionResponse> auctionResponseCall = mApi.postAuctionDirectly(token, auctionRequest);
                                        auctionResponseCall.enqueue(new Callback<AuctionResponse>() {
                                            @Override
                                            public void onResponse(Call<AuctionResponse> call, Response<AuctionResponse> response) {
                                                Log.d(TAG, "Post auction body = " + response.body());
                                                Log.d(TAG, "Post auction code = " + response.code());
                                                if (response.body() == null) {
                                                    Log.d(TAG, "Post auction Success Stt = " + response.isSuccessful());
                                                    return;
                                                }else{
                                                    Log.d(TAG, "Post auction Success Stt = " + response.isSuccessful());
                                                    AuctionResponse auctionResponse = response.body();
                                                    Log.d(TAG, "Post auction Success = " + auctionResponse.getMessage());
                                                }
                                            }
                                            @Override
                                            public void onFailure(Call<AuctionResponse> call, Throwable t) {
                                                Log.d(TAG, "Post auction fail !");
                                            }
                                        });
                                        Toast.makeText(mNotificationView.getContext(), "Mua thẳng đã gửi", Toast.LENGTH_SHORT).show();
                                        removeAndStopService();
                                    }
                                }
                            });
                            btnCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    removeAndStopService();
                                }
                            });
                        }
                    });
                }
            }
        }
    };

    private void removeAndStopService() {
        if (mNotificationView != null && mNotificationView.getWindowToken() != null) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    getWindowManager().removeView(mNotificationView);
                    stopSelf();
                }
            });
        }
    }

    private WindowManager getWindowManager() {

        if (mWindowManager == null) {
            mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    private void addViewToWindow(final View view) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                getWindowManager().addView(view, getParams());
            }
        });
    }

    private WindowManager.LayoutParams getParams() {
        return new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON ,
                PixelFormat.TRANSLUCENT);
    }

    public void play() {
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });
        }
    }
    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
    }
    private void sendNotification(String notificationBody) {
        Log.e(TAG, "In sendNotification");
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
                .setContentText(notificationBody)
                .setAutoCancel(true)
                .setPriority(1)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Apps.NOTIFICATION_ID, notificationBuilder.build());
    }
}