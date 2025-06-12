package com.wyh.happyyousdk.model.response.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RewardsHistoryActivityData {
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("eventName")
    @Expose
    private String eventName;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("transDate")
    @Expose
    private String transDate;
    @SerializedName("transStatus")
    @Expose
    private String transStatus;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }
}
