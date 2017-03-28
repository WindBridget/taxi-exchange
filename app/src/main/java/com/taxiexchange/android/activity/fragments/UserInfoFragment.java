package com.taxiexchange.android.activity.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.taxiexchange.android.R;
import com.taxiexchange.android.activity.LoginActivity;
import com.taxiexchange.android.config.Apps;

import com.taxiexchange.android.config.PreferenceManager;
import com.taxiexchange.android.config.retrofit.TaxiExchangeApi;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hieu.nguyennam on 3/21/2017.
 */

public class UserInfoFragment extends BaseFragment{

    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.user_txt_name)
    TextView mTextUserName;
    @BindView(R.id.user_txt_phone)
    TextView mTextUserPhone;
    @BindView(R.id.user_txt_car)
    TextView mTextUserCar;
    @BindView(R.id.user_txt_money)
    TextView mTextUserMoney;
    @BindView(R.id.user_exit)
    LinearLayout mUserExit;
    @BindView(R.id.user_pw_change)
    LinearLayout mUserPasswordChange;


    private TaxiExchangeApi mApi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }
    private void initViews(){
        mTextUserName.setText(PreferenceManager.getString(Apps.USER_INFO_NAME));
        mTextUserPhone.setText(PreferenceManager.getString(Apps.USER_INFO_MOBILE));
        mTextUserCar.setText(PreferenceManager.getString(Apps.USER_INFO_CAR));
        mTextUserMoney.setText(PreferenceManager.getString(Apps.USER_INFO_AMOUNT) + " K");
        mUserExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        mUserPasswordChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }
    private void signOut() {
        writePreference(Apps.IS_SIGN, false);
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }
    private void changePassword() {
        Toast.makeText(getActivity(), "This feature has not developed yet !", Toast.LENGTH_SHORT).show();
    }
}
