package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.response.playwin.DialogModel;

import java.io.Serializable;

public class AssignRewardsResponse {

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("success")
    @Expose
    private Boolean  success;

    @SerializedName("data")
    @Expose
    private SpinRewardsData data;

    public String getMsg() {
        return msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public SpinRewardsData getData() {
        return data;
    }

    public class SpinRewardsData implements Serializable {

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

        public SpinRewardsData() {
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
        @SerializedName("dialogDetail")
        @Expose
        DialogModel dialogModel;

        public DialogModel getDialogModel() {
            return dialogModel;
        }

        public void setDialogModel(DialogModel dialogModel) {
            this.dialogModel = dialogModel;
        }

        public String getClaimDate() {
            return claimDate;
        }

        public void setClaimDate(String claimDate) {
            this.claimDate = claimDate;
        }

        @SerializedName("claimDate")
      @Expose
      String  claimDate;
    }

    public class SpinRewardsDataWeb implements Serializable {

        @SerializedName("isrewardawarded")
        @Expose
        private boolean isrewardawarded;


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


        public boolean getIsrewardawarded() {
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
}
