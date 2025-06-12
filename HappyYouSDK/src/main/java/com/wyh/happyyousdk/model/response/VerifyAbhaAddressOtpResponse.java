package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyAbhaAddressOtpResponse {

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("data")
    @Expose
    private VerifyAbhaAddressOtpData data;

    public String getMsg() {
        return msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public VerifyAbhaAddressOtpData getData() {
        return data;
    }

    public class VerifyAbhaAddressOtpData {

        @SerializedName("token")
        @Expose
        private String token;

        public String getMessage() {
            return message;
        }

        @SerializedName("message")
        @Expose
        private String message;
        public Error getError() {
            return error;
        }
        @SerializedName("error")
        @Expose
        public Error error;
        public String getToken() {
            return token;
        }
    }
    public static class Error{
        public String getMessage() {
            return message;
        }

        public String message;
    }


}
