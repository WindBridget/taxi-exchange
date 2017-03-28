package com.taxiexchange.android.activity.fragments;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.taxiexchange.android.R;
import com.taxiexchange.android.activity.adapters.ListAcceptedAdapter;
import com.taxiexchange.android.config.Apps;
import com.taxiexchange.android.config.PreferenceManager;
import com.taxiexchange.android.config.retrofit.RestClient;
import com.taxiexchange.android.config.retrofit.TaxiExchangeApi;
import com.taxiexchange.android.model.response.ListAcceptedResponse;
import com.taxiexchange.android.model.response.TripList;
import com.taxiexchange.android.ulti.TaxiExchangeTimeUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hieu.nguyennam on 3/20/2017.
 */

public class ListAcceptedFragment extends BaseFragment{
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private List<TripList> mTripList = new ArrayList<>();
    private ListAcceptedAdapter mAdapter;
    private TaxiExchangeApi mApi;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        initView();
        initAPI();
        showView();
        return view;
    }

    private void initAPI() {
        mApi = RestClient.getClient().create(TaxiExchangeApi.class);
    }

    private void initView(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        setHasOptionsMenu(true);
    }

    private void showView(){
        String token = PreferenceManager.getString(Apps.LOGIN_TOKEN);
        Call<ListAcceptedResponse> listAcceptedResponseCall = mApi.getAcceptedList(token);
        listAcceptedResponseCall.enqueue(new Callback<ListAcceptedResponse>() {
            @Override
            public void onResponse(Call<ListAcceptedResponse> call, Response<ListAcceptedResponse> response) {
                Log.d(TAG, "List accepted body = " + response.body());
                Log.d(TAG, "List accepted body = " + response.code());
                if (response.body() == null) {
                    Log.d(TAG, "get List accepted Success Stt = " + response.isSuccessful());
                    return;
                }else{
                    Log.d(TAG, "get List accepted Success Stt = " + response.isSuccessful());
                    ListAcceptedResponse listAcceptedResponse = response.body();
                    Log.d(TAG, "get List accepted Success = " + listAcceptedResponse);
                    for(TripList tripList : listAcceptedResponse.getTripList()){
                        Log.d(TAG, "get List accepted item bidId = " + tripList.getBidId());
                        tripList.setDepartureTimeLong(TaxiExchangeTimeUtils.getTime(tripList.getDepartureTime()));
                        mTripList.add(tripList);
                    }
                    Collections.sort(mTripList);
                    mAdapter = new ListAcceptedAdapter(getActivity(), mTripList);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onFailure(Call<ListAcceptedResponse> call, Throwable t) {
                Log.d(TAG, "get list accepted fail !");
            }
        });
    }
}
