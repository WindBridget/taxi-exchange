package com.taxiexchange.android.activity.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.taxiexchange.android.R;
import com.taxiexchange.android.activity.adapters.ListAuctionAdapter;
import com.taxiexchange.android.config.Apps;
import com.taxiexchange.android.config.PreferenceManager;
import com.taxiexchange.android.model.response.ListAuctionResponse;
import com.taxiexchange.android.ulti.RecyclerViewListener;
import com.taxiexchange.android.ulti.TaxiExchangeTimeUtils;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hieu.nguyennam on 3/20/2017.
 */

public class ListAuctionFragment extends BaseFragment{
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private List<ListAuctionResponse> mListAuctionResponse = new ArrayList<>();
    private ListAuctionAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        initView();

        return view;
    }

    private void initView(){
        mListAuctionResponse = new ArrayList<>();
        String userMobile = PreferenceManager.getString(Apps.USER_MOBILE);
        if (userMobile != null){
            showView(userMobile);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.addOnItemTouchListener(new RecyclerViewListener(getActivity(), new RecyclerViewListener.RecyclerOnClick() {
            @Override
            public void onClick(View view, int position) {
                ListAuctionResponse item = mListAuctionResponse.get(position);

                Fragment fragment = new SendOfferFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable(Apps.ITEM_BID_ID, Parcels.wrap(item));
                fragment.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .add(R.id.content_main, fragment)
                        .addToBackStack("send_offer")
                        .commitAllowingStateLoss();
            }
        }));

        setHasOptionsMenu(true);
    }

    private void showView(String usesMobile){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://taxiexchange.firebaseio.com/");
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        database.getReference().child(usesMobile).child("offer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    Log.e(TAG, "Null");
                    progressDialog.dismiss();
                    return;
                }
                Log.e(TAG, dataSnapshot.getValue().toString());

                for(DataSnapshot auctionInfo : dataSnapshot.getChildren()){
                    String bidId = (String) auctionInfo.child("bid_id").getValue();
                    double ceilingPrice = (double) auctionInfo.child("ceiling_price").getValue();
                    long hasBill = (long) auctionInfo.child("co_hoa_don").getValue();
                    String departureTime = (String) auctionInfo.child("departure_time").getValue();
                    double directPrice = (double) auctionInfo.child("direct_price").getValue();
                    String dstLocation = (String) auctionInfo.child("dst_location").getValue();
                    String dueDate = (String) auctionInfo.child("due_date").getValue();
                    long isRoundTrip = (long) auctionInfo.child("khu_hoi").getValue();
                    String bidType = (String) auctionInfo.child("loai_bid").getValue();
                    String taxiType = (String) auctionInfo.child("loai_xe").getValue();
                    String note;
                    if(auctionInfo.child("note").getValue() instanceof Boolean)
                        note = String.valueOf((boolean) auctionInfo.child("note").getValue());
                    else
                        note = (String) auctionInfo.child("note").getValue();
                    long passengerQty = (long) auctionInfo.child("passenger_qty").getValue();
                    double price = (double) auctionInfo.child("price").getValue();
                    String srcLocation = (String) auctionInfo.child("src_location").getValue();
                    long departureTimeLong = TaxiExchangeTimeUtils.getTime(departureTime);

                    ListAuctionResponse listAuctionResponse = new ListAuctionResponse(bidId, ceilingPrice, hasBill, departureTime,
                            directPrice, dstLocation, dueDate, isRoundTrip, bidType, taxiType, note, passengerQty, price, srcLocation);
                    listAuctionResponse.setDepartureTimeLong(departureTimeLong);

                    mListAuctionResponse.add(listAuctionResponse);
                }
                Collections.sort(mListAuctionResponse);
                mAdapter = new ListAuctionAdapter(getActivity(), mListAuctionResponse);
                mRecyclerView.setAdapter(mAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
