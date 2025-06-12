package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetUserTypemodel {

    @SerializedName("userMobile")
    @Expose
    private String userMobile;

    public GetUserTypemodel(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }
}
