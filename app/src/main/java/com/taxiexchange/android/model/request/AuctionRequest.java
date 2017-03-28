package com.taxiexchange.android.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hieu.nguyennam on 3/22/2017.
 */

public class AuctionRequest {
    @SerializedName("price_offer")
    @Expose
    private double priceOffer;
    @SerializedName("bid_id")
    @Expose
    private String bidId;
    @SerializedName("driver_id")
    @Expose
    private String driverId;

    public AuctionRequest() {
    }

    public AuctionRequest(double priceOffer, String bidId, String driverId) {
        this.priceOffer = priceOffer;
        this.bidId = bidId;
        this.driverId = driverId;
    }

    public double getPriceOffer() {
        return priceOffer;
    }

    public void setPriceOffer(double priceOffer) {
        this.priceOffer = priceOffer;
    }

    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    @Override
    public String toString() {
        return "AuctionRequest{" +
                "priceOffer=" + priceOffer +
                ", bidId='" + bidId + '\'' +
                ", driverId='" + driverId + '\'' +
                '}';
    }
}
