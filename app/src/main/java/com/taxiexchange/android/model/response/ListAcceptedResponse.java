package com.taxiexchange.android.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hieu.nguyennam on 3/22/2017.
 */

public class ListAcceptedResponse {
    @SerializedName("trip_list")
    @Expose
    private List<TripList> tripList = null;

    public List<TripList> getTripList() {
        return tripList;
    }

    public void setTripList(List<TripList> tripList) {
        this.tripList = tripList;
    }

    @Override
    public String toString() {
        return "ListAcceptedResponse{" +
                "tripList=" + tripList +
                '}';
    }
}
