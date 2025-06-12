package com.wyh.happyyousdk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetUserWidgetsResponse {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private List<Datum> data;
    @SerializedName("freevoucher")
    @Expose
    private Object freevoucher;
    @SerializedName("enGTokens")
    @Expose
    private Object enGTokens;
    @SerializedName("rewards")
    @Expose
    private Object rewards;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Object getFreevoucher() {
        return freevoucher;
    }

    public void setFreevoucher(Object freevoucher) {
        this.freevoucher = freevoucher;
    }

    public Object getEnGTokens() {
        return enGTokens;
    }

    public void setEnGTokens(Object enGTokens) {
        this.enGTokens = enGTokens;
    }

    public Object getRewards() {
        return rewards;
    }

    public void setRewards(Object rewards) {
        this.rewards = rewards;
    }

    public static class Datum {

        @SerializedName("WidgetName")
        @Expose
        private String widgetName;
        @SerializedName("WidgetId")
        @Expose
        private Integer WidgetId;
        @SerializedName("IsSet")
        @Expose
        private Integer isSet;

        public String getWidgetName() {
            return widgetName;
        }

        public void setWidgetName(String widgetName) {
            this.widgetName = widgetName;
        }

        public Integer getIsSet() {
            return isSet;
        }

        public void setIsSet(Integer isSet) {
            this.isSet = isSet;
        }

        public Integer getWidgetId() {
            return WidgetId;
        }

        public void setWidgetId(Integer widgetId) {
            WidgetId = widgetId;
        }
    }
}
