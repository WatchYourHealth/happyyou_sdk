package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendAbhOtpResponse {

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("data")
    @Expose
    private abhaOTPData data;


    public String getMsg() {
        return msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public abhaOTPData getData() {
        return data;
    }

    public class abhaOTPData{
        @SerializedName("txnId")
        @Expose
        private String txnId;

        @SerializedName("message")
        @Expose
        private String message;

        public Error getError() {
            return error;
        }
        @SerializedName("error")
        @Expose
        public Error error;
        public String getTxnId() {
            return txnId;
        }

        public String getMessage() {
            return message;
        }
    }
    public static class Error{
        public String getMessage() {
            return message;
        }

        public String message;
    }
}
