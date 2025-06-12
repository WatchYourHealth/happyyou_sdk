package com.wyh.happyyousdk.model.response.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RefreshTokenData {

    @SerializedName("authToken")
    @Expose
    private String authToken;

    @SerializedName("authTokenIssuedOn")
    @Expose
    private String authTokenIssuedOn;

    @SerializedName("authTokenExpiresOn")
    @Expose
    private String authTokenExpiresOn;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthTokenIssuedOn() {
        return authTokenIssuedOn;
    }

    public void setAuthTokenIssuedOn(String authTokenIssuedOn) {
        this.authTokenIssuedOn = authTokenIssuedOn;
    }

    public String getAuthTokenExpiresOn() {
        return authTokenExpiresOn;
    }

    public void setAuthTokenExpiresOn(String authTokenExpiresOn) {
        this.authTokenExpiresOn = authTokenExpiresOn;
    }
}
