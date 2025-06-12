package com.wyh.happyyousdk.model.response.rewards;

public class CommonRewardHistory {
    private String description;
    private String eventName;
    private String amount;
    private String transDate;
    private String transStatus;
    private String campaignID;
    private String startDate;
    private String endDate;
    private String header;
    private String selfStatus;

    /*public CommonRewardHistory(String description, String eventName, int amount, String transDate, String transStatus) {
        this.description = description;
        this.eventName = eventName;
        this.amount = amount;
        this.transDate = transDate;
        this.transStatus = transStatus;
    }*/

    public CommonRewardHistory() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
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

    public String getCampaignID() {
        return campaignID;
    }

    public void setCampaignID(String campaignID) {
        this.campaignID = campaignID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getSelfStatus() {
        return selfStatus;
    }

    public void setSelfStatus(String selfStatus) {
        this.selfStatus = selfStatus;
    }
}
