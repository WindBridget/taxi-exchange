package com.taxiexchange.android.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.taxiexchange.android.model.request.LoginRequest;

/**
 * Created by hieu.nguyennam on 3/16/2017.
 */

public class LoginResponse {
    @SerializedName("data")
    @Expose
    private LoginRequest data;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("token")
    @Expose
    private String token;

    public LoginRequest getData() {
        return data;
    }

    public void setData(LoginRequest data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
