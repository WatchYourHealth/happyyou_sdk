package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetEmailOTPReq implements Serializable {

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public GetEmailOTPReq(String email) {
        Email = email;
    }

    @SerializedName("Email")
    @Expose
    private String Email;
}
