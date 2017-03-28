package com.taxiexchange.android.activity.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taxiexchange.android.R;
import com.taxiexchange.android.activity.adapters.ListJoinedAdapter;
import com.taxiexchange.android.config.Apps;
import com.taxiexchange.android.config.PreferenceManager;
import com.taxiexchange.android.config.retrofit.RestClient;
import com.taxiexchange.android.config.retrofit.TaxiExchangeApi;
import com.taxiexchange.android.model.response.ListJoinedResponse;
import com.taxiexchange.android.model.response.OfferList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.id.list;

/**
 * Created by hieu.nguyennam on 3/20/2017.
 */

public class ListJoinedFragment extends BaseFragment{
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private List<OfferList> mOfferList = new ArrayList<>();
    private ListJoinedAdapter mAdapter;
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
        Call<ListJoinedResponse> listJoinedResponseCall = mApi.getJoinedList(token);
        listJoinedResponseCall.enqueue(new Callback<ListJoinedResponse>() {
            @Override
            public void onResponse(Call<ListJoinedResponse> call, Response<ListJoinedResponse> response) {
                Log.d(TAG, "List joined body = " + response.body());
                Log.d(TAG, "List joined code = " + response.code());
                if (response.body() == null) {
                    Log.d(TAG, "get List joined Success Stt = " + response.isSuccessful());
                    return;
                }else{
                    Log.d(TAG, "get List joined Success Stt = " + response.isSuccessful());
                    ListJoinedResponse listJoinedResponse = response.body();
                    Log.d(TAG, "get List joined Success = " + listJoinedResponse);
                    for(OfferList offerList : listJoinedResponse.getOfferList()){
                        Log.d(TAG, "get List joined item bidId = " + offerList.getBidId());
                        mOfferList.add(offerList);
                    }
//                    Collections.reverse(mOfferList);
                    mAdapter = new ListJoinedAdapter(getActivity(), mOfferList);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
            @Override
            public void onFailure(Call<ListJoinedResponse> call, Throwable t) {
                Log.d(TAG, "get list joined fail !");
            }
        });
    }
}
