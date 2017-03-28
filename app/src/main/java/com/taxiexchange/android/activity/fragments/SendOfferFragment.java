package com.taxiexchange.android.activity.fragments;


import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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

public class SendOfferFragment extends BaseFragment {
    private final String TAG = getClass().getSimpleName();

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

    ListAuctionResponse mListAuctionResponse;
    private TaxiExchangeApi mApi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.send_offer, container, false);
        ButterKnife.bind(this, view);
        getData();
        initSpinner();
        initAPI();
        initViews();
        return view;
    }

    private void getData() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        try {
            mListAuctionResponse = Parcels.unwrap(bundle.getParcelable(Apps.ITEM_BID_ID));
            Log.d(TAG, "id create = " + mListAuctionResponse.getBidId());
        } catch (NullPointerException e) {
            Log.d(TAG, e.toString());
        }
    }
    private void initSpinner(){
        List<String> spinnerList = new ArrayList();
        for(int i = (int) (mListAuctionResponse.getCeilingPrice()/1000); i > 0; i -= 10 ){
            spinnerList.add(String.format("%,d", i) + " K");
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item, spinnerList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt("Bid giá");
        spinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        dataAdapter,
                        R.layout.contact_spinner_row_nothing_selected,
//                        R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        getActivity()));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Object item = parent.getItemAtPosition(position);
                if (item != null)
                    btnAccept.setEnabled(true);
                else
                    btnAccept.setEnabled(false);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initAPI() {
        mApi = RestClient.getClient().create(TaxiExchangeApi.class);
    }

    private void initViews(){
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
                    Log.e(TAG, "create = " + auctionRequest);
                    String token = PreferenceManager.getString(Apps.LOGIN_TOKEN);
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
                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Post offer ...");
                    progressDialog.show();
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    // On complete call either onLoginSuccess or onLoginFailed
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                    progressDialog.dismiss();
                                }
                            }, 5000);
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
                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Post offer directly...");
                    progressDialog.show();
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    // On complete call either onLoginSuccess or onLoginFailed
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                    progressDialog.dismiss();
                                }
                            }, 5000);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack ("send_offer", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
    }
}
