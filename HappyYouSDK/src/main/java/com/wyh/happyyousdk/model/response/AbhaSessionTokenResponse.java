package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AbhaSessionTokenResponse {

    @SerializedName("accessToken")
    @Expose
    private String accessToken;

    @SerializedName("expiresIn")
    @Expose
    private String expiresIn;

    @SerializedName("refreshToken")
    @Expose
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
