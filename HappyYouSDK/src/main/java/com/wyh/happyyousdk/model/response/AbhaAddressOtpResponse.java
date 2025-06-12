package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AbhaAddressOtpResponse {

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("data")
    @Expose
    private AbhaAddressOtpData data;

    public String getMsg() {
        return msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public AbhaAddressOtpData getData() {
        return data;
    }

    public class AbhaAddressOtpData {

        @SerializedName("txnId")
        @Expose
        private String txnId;

        @SerializedName("authResult")
        @Expose
        private String authResult;

        @SerializedName("message")
        @Expose
        private String message;

        public String getTxnId() {
            return txnId;
        }

        public String getAuthResult() {
            return authResult;
        }

        public String getMessage() {
            return message;
        }
    }
}
