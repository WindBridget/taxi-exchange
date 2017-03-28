package com.taxiexchange.android.model.response;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.taxiexchange.android.ulti.TaxiExchangeTimeUtils;

import org.parceler.Parcel;

/**
 * Created by hieu.nguyennam on 3/20/2017.
 */

@Parcel
public class ListAuctionResponse implements Comparable<ListAuctionResponse>{
    @SerializedName("bid_id")
    @Expose
    private String bidId;
    @SerializedName("ceiling_price")
    @Expose
    private double ceilingPrice;
    @SerializedName("co_hoa_don")
    @Expose
    private long hasBill;
    @SerializedName("departure_time")
    @Expose
    private String departureTime;
    @SerializedName("direct_price")
    @Expose
    private double directPrice;
    @SerializedName("dst_location")
    @Expose
    private String dstLocation;
    @SerializedName("due_date")
    @Expose
    private String dueDate;
    @SerializedName("khu_hoi")
    @Expose
    private long isRoundTrip;
    @SerializedName("loai_bid")
    @Expose
    private String bidType;
    @SerializedName("loai_xe")
    @Expose
    private String taxiType;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("passenger_qty")
    @Expose
    private long passengerQty;
    @SerializedName("price")
    @Expose
    private double price;
    @SerializedName("src_location")
    @Expose
    private String srcLocation;

    public ListAuctionResponse() {
    }

    public ListAuctionResponse(String bidId, double ceilingPrice, long hasBill, String departureTime,
                               double directPrice, String dstLocation, String dueDate, long isRoundTrip,
                               String bidType, String taxiType, String note, long passengerQty, double price, String srcLocation) {
        this.bidId = bidId;
        this.ceilingPrice = ceilingPrice;
        this.hasBill = hasBill;
        this.departureTime = departureTime;
        this.directPrice = directPrice;
        this.dstLocation = dstLocation;
        this.dueDate = dueDate;
        this.isRoundTrip = isRoundTrip;
        this.bidType = bidType;
        this.taxiType = taxiType;
        this.note = note;
        this.passengerQty = passengerQty;
        this.price = price;
        this.srcLocation = srcLocation;
    }

    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
    }

    public double getCeilingPrice() {
        return ceilingPrice;
    }

    public void setCeilingPrice(double ceilingPrice) {
        this.ceilingPrice = ceilingPrice;
    }

    public long getHasBill() {
        return hasBill;
    }

    public void setHasBill(long hasBill) {
        this.hasBill = hasBill;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public double getDirectPrice() {
        return directPrice;
    }

    public void setDirectPrice(double directPrice) {
        this.directPrice = directPrice;
    }

    public String getDstLocation() {
        return dstLocation;
    }

    public void setDstLocation(String dstLocation) {
        this.dstLocation = dstLocation;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public long getIsRoundTrip() {
        return isRoundTrip;
    }

    public void setIsRoundTrip(long isRoundTrip) {
        this.isRoundTrip = isRoundTrip;
    }

    public String getBidType() {
        return bidType;
    }

    public void setBidType(String bidType) {
        this.bidType = bidType;
    }

    public String getTaxiType() {
        return taxiType;
    }

    public void setTaxiType(String taxiType) {
        this.taxiType = taxiType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getPassengerQty() {
        return passengerQty;
    }

    public void setPassengerQty(long passengerQty) {
        this.passengerQty = passengerQty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSrcLocation() {
        return srcLocation;
    }

    public void setSrcLocation(String srcLocation) {
        this.srcLocation = srcLocation;
    }



    private long departureTimeLong;

    public long getDepartureTimeLong() {
        return departureTimeLong;
    }

    public void setDepartureTimeLong(long departureTimeLong) {
        this.departureTimeLong = departureTimeLong;
    }
    @Override
    public String toString() {
        return "ListAuctionResponse{" +
                "bidId='" + bidId + '\'' +
                ", ceilingPrice=" + ceilingPrice +
                ", hasBill=" + hasBill +
                ", departureTime='" + departureTime + '\'' +
                ", directPrice=" + directPrice +
                ", dstLocation='" + dstLocation + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", isRoundTrip=" + isRoundTrip +
                ", bidType='" + bidType + '\'' +
                ", taxiType='" + taxiType + '\'' +
                ", note='" + note + '\'' +
                ", passengerQty=" + passengerQty +
                ", price=" + price +
                ", srcLocation='" + srcLocation + '\'' +
                '}';
    }


    @Override
    public int compareTo(@NonNull ListAuctionResponse o) {
        return (this.departureTimeLong < o.departureTimeLong) ? -1 : (this.departureTimeLong > o.departureTimeLong ? 1 : 0);
    }
}
