package com.taxiexchange.android.model.response;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hieu.nguyennam on 3/22/2017.
 */

public class OfferList implements Comparable<OfferList>{
    @SerializedName("price")
    @Expose
    private double price;
    @SerializedName("ceiling_price")
    @Expose
    private double ceilingPrice;
    @SerializedName("bid_id")
    @Expose
    private String bidId;
    @SerializedName("departure_time")
    @Expose
    private String departureTime;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("src_location")
    @Expose
    private String srcLocation;
    @SerializedName("dst_location")
    @Expose
    private String dstLocation;
    @SerializedName("passenger_qty")
    @Expose
    private long passengerQty;
    @SerializedName("loai_xe")
    @Expose
    private String taxiType;
    @SerializedName("loai_bid")
    @Expose
    private String bidType;
    @SerializedName("co_hoa_don")
    @Expose
    private long hasBill;
    @SerializedName("khu_hoi")
    @Expose
    private long isRoundTrip;
    @SerializedName("due_date")
    @Expose
    private String dueDate;
    @SerializedName("price_offer")
    @Expose
    private double priceOffer;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCeilingPrice() {
        return ceilingPrice;
    }

    public void setCeilingPrice(double ceilingPrice) {
        this.ceilingPrice = ceilingPrice;
    }

    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSrcLocation() {
        return srcLocation;
    }

    public void setSrcLocation(String srcLocation) {
        this.srcLocation = srcLocation;
    }

    public String getDstLocation() {
        return dstLocation;
    }

    public void setDstLocation(String dstLocation) {
        this.dstLocation = dstLocation;
    }

    public long getPassengerQty() {
        return passengerQty;
    }

    public void setPassengerQty(long passengerQty) {
        this.passengerQty = passengerQty;
    }

    public String getTaxiType() {
        return taxiType;
    }

    public void setTaxiType(String taxiType) {
        this.taxiType = taxiType;
    }

    public String getBidType() {
        return bidType;
    }

    public void setBidType(String bidType) {
        this.bidType = bidType;
    }

    public long getHasBill() {
        return hasBill;
    }

    public void setHasBill(long hasBill) {
        this.hasBill = hasBill;
    }

    public long getIsRoundTrip() {
        return isRoundTrip;
    }

    public void setIsRoundTrip(long isRoundTrip) {
        this.isRoundTrip = isRoundTrip;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public double getPriceOffer() {
        return priceOffer;
    }

    public void setPriceOffer(double priceOffer) {
        this.priceOffer = priceOffer;
    }

    private long departureTimeLong;

    public long getDepartureTimeLong() {
        return departureTimeLong;
    }

    public void setDepartureTimeLong(long departureTimeLong) {
        this.departureTimeLong = departureTimeLong;
    }

    @Override
    public int compareTo(@NonNull OfferList o) {
        return (this.departureTimeLong < o.departureTimeLong) ? -1 : (this.departureTimeLong > o.departureTimeLong ? 1 : 0);
    }
}
