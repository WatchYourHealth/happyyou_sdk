package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAbhaCardResponse {

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("data")
    @Expose
    private UserAbhaCardData data;

    public String getMsg() {
        return msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public UserAbhaCardData getData() {
        return data;
    }

   public class UserAbhaCardData {

        @SerializedName("bytes")
        @Expose
        private String bytes;


        public String getBytes() {
            return bytes;
        }



    }


}
