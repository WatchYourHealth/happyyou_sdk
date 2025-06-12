package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AmahaKeyResponse {

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("data")
    @Expose
    private AmahaData data;


    public String getMsg() {
        return msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public AmahaData getData() {
        return data;
    }


    public class AmahaData{

        @SerializedName("code")
        @Expose
        private String code;


        public String getCode() {
            return code;
        }
    }




}


