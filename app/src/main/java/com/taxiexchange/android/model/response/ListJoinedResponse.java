package com.taxiexchange.android.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hieu.nguyennam on 3/22/2017.
 */

public class ListJoinedResponse {
    @SerializedName("offer_list")
    @Expose
    private List<OfferList> offerList = null;

    public List<OfferList> getOfferList() {
        return offerList;
    }

    public void setOfferList(List<OfferList> offerList) {
        this.offerList = offerList;
    }

    @Override
    public String toString() {
        return "ListJoinedResponse{" +
                "offerList=" + offerList +
                '}';
    }
}
