package com.taxiexchange.android.activity.adapters;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taxiexchange.android.R;
import com.taxiexchange.android.model.TimeQuestion;
import com.taxiexchange.android.model.response.OfferList;
import com.taxiexchange.android.ulti.TaxiExchangeTimeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hieu.nguyennam on 3/20/2017.
 */

public class ListJoinedAdapter extends RecyclerView.Adapter<ListJoinedAdapter.ListJoinedHolder>{

    private final String TAG = getClass().getSimpleName();
    private Context mContext;
    private List<OfferList> mOfferList;

    public ListJoinedAdapter(Context context, List<OfferList> offerList) {
        mContext = context;
        mOfferList = offerList;
    }

    @Override
    public ListJoinedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListJoinedHolder(LayoutInflater
                .from(mContext)
                .inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(ListJoinedHolder holder, int position) {
        OfferList item = mOfferList.get(getItemViewType(position));
        holder.bind(item);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mOfferList.size();
    }

    class ListJoinedHolder extends RecyclerView.ViewHolder {

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

//        @BindView(R.id.item_bid_type_direct)
//        LinearLayout linearLayoutBidType;
//
//        @BindView(R.id.item_bid_type)
//        TextView textBidType;
//
//        @BindView(R.id.item_direct_price_detail)
//        TextView textDirectPrice;

        @BindView(R.id.item_price_offer)
        LinearLayout linearLayoutPriceOffer;

        @BindView(R.id.item_price_offer_detail)
        TextView textPriceOffer;

        @BindView(R.id.item_note_detail)
        TextView textNote;

        @BindView(R.id.item_time_remaining)
        TextView textTimeRemaining;


        public ListJoinedHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(OfferList item) {
            //row 1
            if (item.getDepartureTime() != null) {
                TimeQuestion time = TaxiExchangeTimeUtils.convertInputTime(item.getDepartureTime());
                if (time != null) {
                    String strDate = time.getDate();
                    String strTime = time.getTime();
                    textDate.setText(strDate);
                    textTime.setText(strTime);
                }
            }
            if (item.getBidId() != null) {
                textBidId.setText(" | " + item.getBidId());
            }
            //row 2
            if (item.getTaxiType() != null) {
                textTaxiType.setText(item.getTaxiType());
            }
            textTotalPeople.setText(String.valueOf(item.getPassengerQty()) + mContext.getString(R.string.txt_total_people));
            //row 3
            if (item.getSrcLocation() != null) {
                textFromLocation.setText(item.getSrcLocation());
            }
            if (item.getDstLocation() != null) {
                textToLocation.setText(item.getDstLocation());
            }
            if(item.getIsRoundTrip() == 1){
                imageRoundTrip.setVisibility(View.VISIBLE);
                imageNotRoundTrip.setVisibility(View.GONE);
            }
            else{
                imageRoundTrip.setVisibility(View.GONE);
                imageNotRoundTrip.setVisibility(View.VISIBLE);
            }
            //row 4
            if(item.getHasBill() == 1)
                textHasBill.setText(R.string.txt_has_bill);
            else
                textHasBill.setText(R.string.txt_dont_have_bill);

            textCeilingPrice.setText(String.format("%,d", (int)(item.getCeilingPrice()/1000)) + mContext.getString(R.string.txt_vnd));
            //row 5
            if (item.getPriceOffer() != 0.0d ) {
                linearLayoutPriceOffer.setVisibility(View.VISIBLE);
                textPriceOffer.setText(String.format("%,d", (int) (item.getPriceOffer() / 1000)) + mContext.getString(R.string.txt_vnd));
            }
            //row 6
            if (item.getNote() != null && !item.getNote().equals("false")) {
                textNote.setVisibility(View.VISIBLE);
                textNote.setText(mContext.getString(R.string.txt_note) + item.getNote());
            }
            //row 7
            if (item.getDueDate() != null) {
                long timeSub = TaxiExchangeTimeUtils.getTimeRemaining(item.getDueDate());
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
                        textTimeRemaining.setText(day + mContext.getString(R.string.day) +
                                hour + mContext.getString(R.string.hour) +
                                minute + mContext.getString(R.string.minute) +
                                second + mContext.getString(R.string.second));
                    }
                    @Override
                    public void onFinish() {
                        textTimeRemaining.setText(R.string.txt_time_remaining_out);
                    }
                }.start();
            }
        }
    }
}
