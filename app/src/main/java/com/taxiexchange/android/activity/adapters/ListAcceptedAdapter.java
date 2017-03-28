package com.taxiexchange.android.activity.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.taxiexchange.android.R;
import com.taxiexchange.android.model.TimeQuestion;
import com.taxiexchange.android.model.response.TripList;
import com.taxiexchange.android.ulti.TaxiExchangeTimeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hieu.nguyennam on 3/20/2017.
 */

public class ListAcceptedAdapter extends RecyclerView.Adapter<ListAcceptedAdapter.ListAcceptedHolder> {

    private final String TAG = getClass().getSimpleName();
    private Context mContext;
    private List<TripList> mTripList;

    public ListAcceptedAdapter(Context context, List<TripList> tripList) {
        mContext = context;
        mTripList = tripList;
    }

    @Override
    public ListAcceptedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListAcceptedHolder(LayoutInflater
                .from(mContext)
                .inflate(R.layout.item_accepted, parent, false));
    }

    @Override
    public void onBindViewHolder(ListAcceptedHolder holder, int position) {
        TripList item = mTripList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mTripList.size();
    }

    class ListAcceptedHolder extends RecyclerView.ViewHolder {

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

        @BindView(R.id.item_contact_mobile)
        TextView textContactMobile;

        @BindView(R.id.item_contact_name)
        TextView textContactName;

        @BindView(R.id.item_note_detail)
        TextView textNote;

        @BindView(R.id.item_has_bill)
        TextView textHasBill;

        @BindView(R.id.item_price_detail)
        TextView textPrice;

        @BindView(R.id.item_offer_price_detail)
        TextView textOfferPrice;


        public ListAcceptedHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            textContactMobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone = textContactMobile.getText().toString();
                    Log.e(TAG, phone);
                    callPhone(v.getContext(), phone);
                    Toast.makeText(v.getContext(), "in CALL", Toast.LENGTH_SHORT).show();
                }
            });
        }

        void bind(TripList item) {
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
            //row 2
            if (item.getTaxiType() != null) {
                textTaxiType.setText(item.getTaxiType());
            }

            textTotalPeople.setText(String.valueOf(item.getPassengerQty()) + mContext.getString(R.string.txt_total_people));

            if (item.getBidId() != null) {
                textBidId.setText(" | " + item.getBidId());
            }
            //row 3
            if (item.getSrcLocation() != null) {
                textFromLocation.setText(item.getSrcLocation());
            }
            if (item.getDstLocation() != null) {
                textToLocation.setText(item.getDstLocation());
            }
            if (item.getIsRoundTrip() == 1) {
                imageRoundTrip.setVisibility(View.VISIBLE);
                imageNotRoundTrip.setVisibility(View.GONE);
            } else {
                imageRoundTrip.setVisibility(View.GONE);
                imageNotRoundTrip.setVisibility(View.VISIBLE);
            }
            //row 4
            if (item.getContactMobile() != null) {
                textContactMobile.setText(item.getContactMobile());

            }
            if (item.getContactName() != null) {
                textContactName.setText(item.getContactName());
            }
            //row 5
            if (item.getNote() != null && !item.getNote().equals("false")) {
                textNote.setVisibility(View.VISIBLE);
                textNote.setText(mContext.getString(R.string.txt_note) + item.getNote());
            }
            //row 6
            if(item.getHasBill() == 1)
                textHasBill.setText(R.string.txt_has_bill);
            else
                textHasBill.setText(R.string.txt_has_bill);

            textPrice.setText(String.format("%,d", (int)(item.getPrice()/1000)) + mContext.getString(R.string.txt_vnd));
            textOfferPrice.setText("/" + String.format("%,d", (int)(item.getPrice()/1000 - item.getPriceOffer()/1000)) + mContext.getString(R.string.txt_vnd));
        }

    }
    private void callPhone(Context context, String phone) throws SecurityException {
        Log.e(TAG, "phone: " + phone);
        if (phone == null || phone.isEmpty()) {
            return;
        }
        try {
            phone.replaceAll("\\-", "").replaceAll(" ", "");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        String uri = "tel: " + phone;
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse(uri));
            context.startActivity(intent);
        } catch (Exception e) {
        }
    }
}
