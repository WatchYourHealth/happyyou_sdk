package com.wyh.happyyousdk.model.response.ehr;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.EnGTokensModel;
import com.wyh.happyyousdk.model.RewardsModel;

import java.util.List;

public class ConvertToBitlyResponse {

    @SerializedName("data")
    @Expose
    private List<Datum> data;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("enGTokens")
    @Expose
    private EnGTokensModel enGTokens;
    @SerializedName("freevoucher")
    @Expose
    private Object freevoucher;
    @SerializedName("rewards")
    @Expose
    private RewardsModel rewards;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

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


    public Object getFreevoucher() {
        return freevoucher;
    }

    public void setFreevoucher(Object freevoucher) {
        this.freevoucher = freevoucher;
    }

    public EnGTokensModel getEnGTokens() {
        return enGTokens;
    }

    public void setEnGTokens(EnGTokensModel enGTokens) {
        this.enGTokens = enGTokens;
    }

    public RewardsModel getRewards() {
        return rewards;
    }

    public void setRewards(RewardsModel rewards) {
        this.rewards = rewards;
    }

    public static class Datum {

        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("bitlyurl")
        @Expose
        private String bitlyurl;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getBitlyurl() {
            return bitlyurl;
        }

        public void setBitlyurl(String bitlyurl) {
            this.bitlyurl = bitlyurl;
        }

    }
}
