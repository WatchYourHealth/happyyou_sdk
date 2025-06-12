package com.wyh.happyyousdk.model.request.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NickNameRequest {

    @SerializedName("nickName")
    @Expose
    private String nickName;

    public NickNameRequest(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

}