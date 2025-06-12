package com.wyh.happyyousdk.model.response.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RewardsHistoryEnG {
    @SerializedName("eventName")
    @Expose
    private String eventName;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("eventId")
    @Expose
    private String eventId;
    @SerializedName("tokens")
    @Expose
    private Integer tokens;
    @SerializedName("transDate")
    @Expose
    private String transDate;
    @SerializedName("transStatus")
    @Expose
    private String transStatus;
    @SerializedName("amount")
    @Expose
    private Double amount;

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

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Integer getTokens() {
        return tokens;
    }

    public void setTokens(Integer tokens) {
        this.tokens = tokens;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
