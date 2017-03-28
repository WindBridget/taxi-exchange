package com.taxiexchange.android.config.retrofit;

import com.taxiexchange.android.model.request.AuctionRequest;
import com.taxiexchange.android.model.request.LoginRequest;
import com.taxiexchange.android.model.response.AuctionResponse;
import com.taxiexchange.android.model.response.ListAcceptedResponse;
import com.taxiexchange.android.model.response.ListJoinedResponse;
import com.taxiexchange.android.model.response.LoginResponse;
import com.taxiexchange.android.model.response.LogoutResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by hieu.nguyennam on 3/16/2017.
 */

public interface TaxiExchangeApi {

    @POST("/api/v1/users/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("/api/v1/users/logout")
    Call<LogoutResponse> logout();

    @POST("/api/v1/bidding/offer")
    Call<AuctionResponse> postAuction(@Header("Authorization") String token, @Body AuctionRequest auctionRequest);

    @POST("/api/v1/bidding/offer/direct")
    Call<AuctionResponse> postAuctionDirectly(@Header("Authorization") String token, @Body AuctionRequest auctionRequest);

    @GET("/api/v1/bidding/offers")
    Call<ListJoinedResponse> getJoinedList(@Header("Authorization") String token);

    @GET("/api/v1/bidding/trips")
    Call<ListAcceptedResponse> getAcceptedList(@Header("Authorization") String token);

}
