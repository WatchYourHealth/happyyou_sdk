package com.wyh.happyyousdk.model.response.quizathon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.response.FeedbackResponseData;
import com.wyh.happyyousdk.model.response.playwin.DialogModel;
import com.wyh.happyyousdk.model.response.postloginreward.RewardItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GetQuizScratchListResponse implements Serializable {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("isPoolingReq")
    @Expose
    private Boolean isPoolingReq;
    @SerializedName("data")
    @Expose
    private Data data;

    @SerializedName("feedbackDetails")
    @Expose
    public FeedbackResponseData feedbackDetails;
    public static class Data implements Serializable {
        public List<QuizScratchItem> getActiveList() {
            return activeList;
        }

        public void setActiveList(List<QuizScratchItem> activeList) {
            this.activeList = activeList;
        }

        public List<QuizScratchItem> getCompletedList() {
            return completedList;
        }

        public void setCompletedList(List<QuizScratchItem> completedList) {
            this.completedList = completedList;
        }

        public List<QuizScratchItem> getExpiredList() {
            return expiredList;
        }

        public void setExpiredList(List<QuizScratchItem> expiredList) {
            this.expiredList = expiredList;
        }

        @SerializedName("pending")
        @Expose
        private List<QuizScratchItem> activeList;
        @SerializedName("redeemed")
        @Expose
        private List<QuizScratchItem> completedList;
        @SerializedName("expired")
        @Expose
        private List<QuizScratchItem> expiredList;


    }
    public  static class QuizScratchItem{
        @SerializedName("rewardType")
        @Expose
        private String rewardType;
        @SerializedName("rewardTitle")
        @Expose
        private String rewardTitle;
        @SerializedName("rewardTitleIcon")
        @Expose
        private String rewardTitleIcon;
        @SerializedName("rewardDescription")
        @Expose
        private String rewardDescription;
        @SerializedName("redeemMessage")
        @Expose
        private String redeemMessage;
        @SerializedName("rewardValue")
        @Expose
        private String rewardValue;
        @SerializedName("hyCode")
        @Expose
        private String hyCode;
        @SerializedName("couponCode")
        @Expose
        private String couponCode;

        @SerializedName("partnerName")
        @Expose
        private String partnerName;
        @SerializedName("expiryInHours")
        @Expose
        private String expiryInHours;
        @SerializedName("transId")
        @Expose
        private String transId;

        @SerializedName("isClaim")
        @Expose
        private String isClaim;

        @SerializedName("reclaimBurnValue")
        @Expose
        private String reclaimBurnValue;

        @SerializedName("isScratched")
        @Expose
        private boolean isScratched;

        @SerializedName("isExpired")
        @Expose
        private boolean isExpired;

        @SerializedName("isRewardClaimed")
        @Expose
        private String isRewardClaimed;

        @SerializedName("rewardHeader1")
        @Expose
        private String rewardHeader1;

        public String getRewardHeader2() {
            return rewardHeader2;
        }

        public void setRewardHeader2(String rewardHeader2) {
            this.rewardHeader2 = rewardHeader2;
        }

        public String getRewardHeader1() {
            return rewardHeader1;
        }

        public void setRewardHeader1(String rewardHeader1) {
            this.rewardHeader1 = rewardHeader1;
        }

        @SerializedName("rewardHeader2")
        @Expose
        private String rewardHeader2;

        public DialogModel getDialogModel() {
            return dialogModel;
        }

        public void setDialogModel(DialogModel dialogModel) {
            this.dialogModel = dialogModel;
        }

        @SerializedName("dialogDetail")
        @Expose
        com.wyh.happyyousdk.model.response.playwin.DialogModel dialogModel;

        public String getClaimDate() {
            return claimDate;
        }

        public void setClaimDate(String claimDate) {
            this.claimDate = claimDate;
        }

        @SerializedName("claimDate")
        @Expose
        String claimDate;

        public String getRewardType() {
            return rewardType;
        }

        public void setRewardType(String rewardType) {
            this.rewardType = rewardType;
        }

        public String getRewardTitle() {
            return rewardTitle;
        }

        public void setRewardTitle(String rewardTitle) {
            this.rewardTitle = rewardTitle;
        }

        public String getRewardTitleIcon() {
            return rewardTitleIcon;
        }

        public void setRewardTitleIcon(String rewardTitleIcon) {
            this.rewardTitleIcon = rewardTitleIcon;
        }

        public String getRewardDescription() {
            return rewardDescription;
        }

        public void setRewardDescription(String rewardDescription) {
            this.rewardDescription = rewardDescription;
        }

        public String getRedeemMessage() {
            return redeemMessage;
        }

        public void setRedeemMessage(String redeemMessage) {
            this.redeemMessage = redeemMessage;
        }

        public String getRewardValue() {
            return rewardValue;
        }

        public void setRewardValue(String rewardValue) {
            this.rewardValue = rewardValue;
        }

        public String getHyCode() {
            return hyCode;
        }

        public void setHyCode(String hyCode) {
            this.hyCode = hyCode;
        }

        public String getCouponCode() {
            return couponCode;
        }

        public void setCouponCode(String couponCode) {
            this.couponCode = couponCode;
        }

        public String getPartnerName() {
            return partnerName;
        }

        public void setPartnerName(String partnerName) {
            this.partnerName = partnerName;
        }

        public String getExpiryInHours() {
            return expiryInHours;
        }

        public void setExpiryInHours(String expiryInHours) {
            this.expiryInHours = expiryInHours;
        }

        public String getTransId() {
            return transId;
        }

        public void setTransId(String transId) {
            this.transId = transId;
        }

        public String getIsClaim() {
            return isClaim;
        }

        public void setIsClaim(String isClaim) {
            this.isClaim = isClaim;
        }

        public String getReclaimBurnValue() {
            return reclaimBurnValue;
        }

        public void setReclaimBurnValue(String reclaimBurnValue) {
            this.reclaimBurnValue = reclaimBurnValue;
        }

        public boolean getIsScratched() {
            return isScratched;
        }

        public void setIsScratched(boolean isScratched) {
            this.isScratched = isScratched;
        }

        public boolean getIsExpired() {
            return isExpired;
        }

        public void setIsExpired(boolean isExpired) {
            this.isExpired = isExpired;
        }

        public String getIsRewardClaimed() {
            return isRewardClaimed;
        }

        public void setIsRewardClaimed(String isRewardClaimed) {
            this.isRewardClaimed = isRewardClaimed;
        }
    }
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

    public Boolean getPoolingReq() {
        return isPoolingReq;
    }

    public void setPoolingReq(Boolean poolingReq) {
        isPoolingReq = poolingReq;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public FeedbackResponseData getFeedbackDetails() {
        return feedbackDetails;
    }

    public void setFeedbackDetails(FeedbackResponseData feedbackDetails) {
        this.feedbackDetails = feedbackDetails;
    }
}
