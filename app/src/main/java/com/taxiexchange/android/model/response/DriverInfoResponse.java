package com.taxiexchange.android.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hieu.nguyennam on 3/22/2017.
 */

public class DriverInfoResponse {
    @SerializedName("amount")
    @Expose
    private double amount;
    @SerializedName("car")
    @Expose
    private String car;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("name")
    @Expose
    private String name;


    public DriverInfoResponse() {}

    public DriverInfoResponse(double amount, String car, String mobile, String name) {
        this.amount = amount;
        this.car = car;
        this.mobile = mobile;
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DriverInfoResponse{" +
                "amount=" + amount +
                ", car='" + car + '\'' +
                ", mobile='" + mobile + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
