package com.taxiexchange.android.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hieu.nguyennam on 3/16/2017.
 */

public class LoginRequest {
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("registration_id")
    @Expose
    private String registrationId;

    public LoginRequest(){}

    public LoginRequest(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }

    public LoginRequest(String mobile, String password, String registrationId){
        this.mobile = mobile;
        this.password = password;
        this.registrationId = registrationId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

}
