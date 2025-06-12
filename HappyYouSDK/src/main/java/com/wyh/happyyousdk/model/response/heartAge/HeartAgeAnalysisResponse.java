package com.wyh.happyyousdk.model.response.heartAge;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.RewardsModel;
import com.wyh.happyyousdk.model.response.AssignRewardsResponse;
import com.wyh.happyyousdk.model.response.FeedbackResponseData;

public class HeartAgeAnalysisResponse  {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("freevoucher")
    @Expose
    private Object freevoucher;
    @SerializedName("enGTokens")
    @Expose
    private Object enGTokens;
    @SerializedName("rewards")
    @Expose
    private RewardsModel rewards;

    @SerializedName("feedbackDetails")
    @Expose
    private FeedbackResponseData feedbackDetails;

    @SerializedName("spinTheWheelRewardsDetail")
    @Expose
    private AssignRewardsResponse.SpinRewardsData spinTheWheelRewardsModel;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
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

    public RewardsModel getRewards() {
        return rewards;
    }

    public void setRewards(RewardsModel rewards) {
        this.rewards = rewards;
    }


    public FeedbackResponseData getFeedbackDetails() {
        return feedbackDetails;
    }

    public AssignRewardsResponse.SpinRewardsData getSpinTheWheelRewardsModel() {
        return spinTheWheelRewardsModel;
    }

    public class Data {

        @SerializedName("uuid")
        @Expose
        private Object uuid;
        @SerializedName("conversationId")
        @Expose
        private String conversationId;
        @SerializedName("integrationId")
        @Expose
        private String integrationId;
        @SerializedName("heartAge")
        @Expose
        private float heartAge;
        @SerializedName("age")
        @Expose
        private Integer age;
        @SerializedName("heartRiskScore")
        @Expose
        private float heartRiskScore;
        @SerializedName("heartAgeMessage")
        @Expose
        private String heartAgeMessage;
        @SerializedName("heartRiskScoreMessage")
        @Expose
        private String heartRiskScoreMessage;
        @SerializedName("disclaimer")
        @Expose
        private String disclaimer;




        public Object getUuid() {
            return uuid;
        }

        public void setUuid(Object uuid) {
            this.uuid = uuid;
        }

        public String getConversationId() {
            return conversationId;
        }

        public void setConversationId(String conversationId) {
            this.conversationId = conversationId;
        }

        public String getIntegrationId() {
            return integrationId;
        }

        public void setIntegrationId(String integrationId) {
            this.integrationId = integrationId;
        }

        public float getHeartAge() {
            return heartAge;
        }

        public void setHeartAge(float heartAge) {
            this.heartAge = heartAge;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public float getHeartRiskScore() {
            return heartRiskScore;
        }

        public void setHeartRiskScore(float heartRiskScore) {
            this.heartRiskScore = heartRiskScore;
        }

        public String getHeartAgeMessage() {
            return heartAgeMessage;
        }

        public void setHeartAgeMessage(String heartAgeMessage) {
            this.heartAgeMessage = heartAgeMessage;
        }

        public String getHeartRiskScoreMessage() {
            return heartRiskScoreMessage;
        }

        public void setHeartRiskScoreMessage(String heartRiskScoreMessage) {
            this.heartRiskScoreMessage = heartRiskScoreMessage;
        }

        public String getDisclaimer() {
            return disclaimer;
        }

        public void setDisclaimer(String disclaimer) {
            this.disclaimer = disclaimer;
        }


    }


}
