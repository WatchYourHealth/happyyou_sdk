package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class VerifyEmailOTPResp implements Serializable {

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @SerializedName("success")
    @Expose
    private Boolean success;


    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("data")
    @Expose
    private Data data;

    public static class Data {


        @SerializedName("corpName")
        @Expose
        private String corpName;

        @SerializedName("corpLogo")
        @Expose
        private String corpLogo;

        public int getCorpId() {
            return corpId;
        }

        public void setCorpId(int corpId) {
            this.corpId = corpId;
        }

        public String getCorpLogo() {
            return corpLogo;
        }

        public void setCorpLogo(String corpLogo) {
            this.corpLogo = corpLogo;
        }

        public String getCorpName() {
            return corpName;
        }

        public void setCorpName(String corpName) {
            this.corpName = corpName;
        }

        @SerializedName("corpId")
        @Expose
        private int corpId;
    }
}

