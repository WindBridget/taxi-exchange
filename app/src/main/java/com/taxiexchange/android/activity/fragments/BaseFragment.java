package com.taxiexchange.android.activity.fragments;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.taxiexchange.android.config.PreferenceManager;


/**
 * Created by Nhahv on 10/18/2016.
 * <></>
 */

public class BaseFragment extends Fragment implements View.OnClickListener {

    protected final String TAG = getClass().getSimpleName();

    protected void showMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void showMessage(int id) {
        Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();
    }

    protected String getString(String key) {
        return PreferenceManager.getString(key);
    }

    protected int getInt(String key) {
        return PreferenceManager.getInt(key);
    }

    protected long getLong(String key) {
        return PreferenceManager.getLong(key);
    }

    protected boolean getBoolean(String key) {
        return PreferenceManager.getBoolean(key);
    }

    @Override
    public void onClick(View v) {

    }

    protected void writePreference(String key, int value) {
        PreferenceManager.writePreference(key, value);
    }

    protected void writePreference(String key, String value) {
        PreferenceManager.writePreference(key, value);
    }

    protected void writePreference(String key, long value) {
        PreferenceManager.writePreference(key, value);
    }

    protected  void writePreference(String key, boolean value) {
        PreferenceManager.writePreference(key, value);
    }
    public void refresh() {

    }

}
