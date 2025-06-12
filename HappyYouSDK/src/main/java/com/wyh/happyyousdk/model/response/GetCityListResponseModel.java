package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.EnGTokensModel;
import com.wyh.happyyousdk.model.RewardsModel;

import java.util.List;

public class GetCityListResponseModel {

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
        private List<CityDataModel> data;
        @SerializedName("feedbackDetails")
        @Expose
        private Object feedbackDetails;
        @SerializedName("freevoucher")
        @Expose
        private Object freevoucher;
        @SerializedName("enGTokens")
        @Expose
        private EnGTokensModel enGTokens;
        @SerializedName("rewards")
        @Expose
        private RewardsModel rewards;
        @SerializedName("userkey")
        @Expose
        private Object userkey;
        @SerializedName("isGoogleFit")
        @Expose
        private Boolean isGoogleFit;
        @SerializedName("userDetail")
        @Expose
        private Object userDetail;
        @SerializedName("spinTheWheelRewardsDetail")
        @Expose
        private Object spinTheWheelRewardsDetail;
        @SerializedName("quizathonRewardsDetail")
        @Expose
        private Object quizathonRewardsDetail;
        @SerializedName("quizRewardsDetail")
        @Expose
        private Object quizRewardsDetail;
        @SerializedName("feedbackRewardModel")
        @Expose
        private Object feedbackRewardModel;

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

        public Boolean getIsPoolingReq() {
            return isPoolingReq;
        }

        public void setIsPoolingReq(Boolean isPoolingReq) {
            this.isPoolingReq = isPoolingReq;
        }

        public List<CityDataModel> getData() {
            return data;
        }

        public void setData(List<CityDataModel> data) {
            this.data = data;
        }

        public Object getFeedbackDetails() {
            return feedbackDetails;
        }

        public void setFeedbackDetails(Object feedbackDetails) {
            this.feedbackDetails = feedbackDetails;
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

        public Object getUserkey() {
            return userkey;
        }

        public void setUserkey(Object userkey) {
            this.userkey = userkey;
        }

        public Boolean getIsGoogleFit() {
            return isGoogleFit;
        }

        public void setIsGoogleFit(Boolean isGoogleFit) {
            this.isGoogleFit = isGoogleFit;
        }

        public Object getUserDetail() {
            return userDetail;
        }

        public void setUserDetail(Object userDetail) {
            this.userDetail = userDetail;
        }

        public Object getSpinTheWheelRewardsDetail() {
            return spinTheWheelRewardsDetail;
        }

        public void setSpinTheWheelRewardsDetail(Object spinTheWheelRewardsDetail) {
            this.spinTheWheelRewardsDetail = spinTheWheelRewardsDetail;
        }

        public Object getQuizathonRewardsDetail() {
            return quizathonRewardsDetail;
        }

        public void setQuizathonRewardsDetail(Object quizathonRewardsDetail) {
            this.quizathonRewardsDetail = quizathonRewardsDetail;
        }

        public Object getQuizRewardsDetail() {
            return quizRewardsDetail;
        }

        public void setQuizRewardsDetail(Object quizRewardsDetail) {
            this.quizRewardsDetail = quizRewardsDetail;
        }

        public Object getFeedbackRewardModel() {
            return feedbackRewardModel;
        }

        public void setFeedbackRewardModel(Object feedbackRewardModel) {
            this.feedbackRewardModel = feedbackRewardModel;
        }

    public class CityDataModel {

        @SerializedName("cityName")
        @Expose
        private String cityName;
        @SerializedName("cityId")
        @Expose
        private Integer cityId;

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public Integer getCityId() {
            return cityId;
        }

        public void setCityId(Integer cityId) {
            this.cityId = cityId;
        }

    }


}
