package com.wyh.happyyousdk.model.response.playwin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class QuizathonRewardData implements Serializable {
    @SerializedName("isrewardawarded")
    @Expose
    private int isrewardawarded;


    @SerializedName("rewardType")
    @Expose
    private String rewardType;


    @SerializedName("rewardLogo")
    @Expose
    private String rewardLogo;


    @SerializedName("rewardTitle")
    @Expose
    private String rewardTitle;


    @SerializedName("rewardDescription")
    @Expose
    private String rewardDescription;


    @SerializedName("couponCode")
    @Expose
    private String couponCode;


    @SerializedName("isCouponCode")
    @Expose
    private String isCouponCode;


    @SerializedName("isRedemption")
    @Expose
    private String isRedemption;

    @SerializedName("partnerName")
    @Expose
    private String partnerName;

    @SerializedName("partnerLogo")
    @Expose
    private String partnerLogo;

    @SerializedName("rewardValue")
    @Expose
    private String rewardValue;

    @SerializedName("partnerUrl")
    @Expose
    private String partnerUrl;

    @SerializedName("expiredOn")
    @Expose
    private String expiryInHours;

    @SerializedName("rewardHeader1")
    @Expose
    private String rewardHeader1;

    @SerializedName("rewardHeader2")
    @Expose
    private String rewardHeader2;

    @SerializedName("transId")
    @Expose
    private String TransId;

    @SerializedName("dialogDetail")
    @Expose
    DialogModel dialogModel;

    public String getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(String claimDate) {
        this.claimDate = claimDate;
    }

    @SerializedName("claimDate")
    @Expose
    String claimDate;

    public DialogModel getDialogModel() {
        return dialogModel;
    }

    public void setDialogModel(DialogModel dialogModel) {
        this.dialogModel = dialogModel;
    }

    public String getTransId() {
        return TransId;
    }

    public void setTransId(String transId) {
        TransId = transId;
    }

    public QuizathonRewardData() {
    }

    public int getIsrewardawarded() {
        return isrewardawarded;
    }

    public String getRewardType() {
        return rewardType;
    }

    public String getRewardLogo() {
        return rewardLogo;
    }

    public String getRewardTitle() {
        return rewardTitle;
    }

    public String getRewardDescription() {
        return rewardDescription;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public String getIsCouponCode() {
        return isCouponCode;
    }

    public String getIsRedemption() {
        return isRedemption;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public String getPartnerLogo() {
        return partnerLogo;
    }

    public String getRewardValue() {
        return rewardValue;
    }

    public String getPartnerUrl() {
        return partnerUrl;
    }

    public String getExpiryInHours() {
        return expiryInHours;
    }

    public String getRewardHeader1() {
        return rewardHeader1;
    }

    public String getRewardHeader2() {
        return rewardHeader2;
    }

}
