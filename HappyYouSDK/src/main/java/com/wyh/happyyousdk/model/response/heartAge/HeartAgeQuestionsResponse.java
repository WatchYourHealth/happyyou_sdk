package com.wyh.happyyousdk.model.response.heartAge;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HeartAgeQuestionsResponse {

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

    public Object getRewards() {
        return rewards;
    }

    public void setRewards(Object rewards) {
        this.rewards = rewards;
    }

    public class Data {

        @SerializedName("integrationid")
        @Expose
        private String integrationid;
        @SerializedName("conversationId")
        @Expose
        private String conversationId;
        @SerializedName("surveyVersion")
        @Expose
        private String surveyVersion;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("answerJson")
        @Expose
        private String answerJson;
        @SerializedName("createdDate")
        @Expose
        private String createdDate;
        @SerializedName("modifiedDate")
        @Expose
        private String modifiedDate;

        public String getIntegrationid() {
            return integrationid;
        }

        public void setIntegrationid(String integrationid) {
            this.integrationid = integrationid;
        }

        public String getConversationId() {
            return conversationId;
        }

        public void setConversationId(String conversationId) {
            this.conversationId = conversationId;
        }

        public String getSurveyVersion() {
            return surveyVersion;
        }

        public void setSurveyVersion(String surveyVersion) {
            this.surveyVersion = surveyVersion;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAnswerJson() {
            return answerJson;
        }

        public void setAnswerJson(String answerJson) {
            this.answerJson = answerJson;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getModifiedDate() {
            return modifiedDate;
        }

        public void setModifiedDate(String modifiedDate) {
            this.modifiedDate = modifiedDate;
        }

    }


}
