package com.wyh.happyyousdk.model.response.heartAge;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HeartAgeConversationIdResponse {

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

    /**
     * No args constructor for use in serialization
     *
     */
    public HeartAgeConversationIdResponse() {
    }

    /**
     *
     * @param msg
     * @param freevoucher
     * @param data
     * @param success
     * @param enGTokens
     * @param rewards
     */
    public HeartAgeConversationIdResponse(String msg, Boolean success, Data data, Object freevoucher, Object enGTokens, Object rewards) {
        super();
        this.msg = msg;
        this.success = success;
        this.data = data;
        this.freevoucher = freevoucher;
        this.enGTokens = enGTokens;
        this.rewards = rewards;
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

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("customerID")
        @Expose
        private String customerID;
        @SerializedName("integrationid")
        @Expose
        private String integrationid;
        @SerializedName("conversationId")
        @Expose
        private String conversationId;
        @SerializedName("surveyVersion")
        @Expose
        private Object surveyVersion;
        @SerializedName("status")
        @Expose
        private Object status;
        @SerializedName("answerJson")
        @Expose
        private String answerJson;
        @SerializedName("createdDate")
        @Expose
        private String createdDate;
        @SerializedName("modifiedDate")
        @Expose
        private Object modifiedDate;

        /**
         * No args constructor for use in serialization
         *
         */
        public Data() {
        }

        /**
         *
         * @param createdDate
         * @param conversationId
         * @param surveyVersion
         * @param customerID
         * @param modifiedDate
         * @param integrationid
         * @param id
         * @param answerJson
         * @param status
         */
        public Data(Integer id, String customerID, String integrationid, String conversationId, Object surveyVersion, Object status, String answerJson, String createdDate, Object modifiedDate) {
            super();
            this.id = id;
            this.customerID = customerID;
            this.integrationid = integrationid;
            this.conversationId = conversationId;
            this.surveyVersion = surveyVersion;
            this.status = status;
            this.answerJson = answerJson;
            this.createdDate = createdDate;
            this.modifiedDate = modifiedDate;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCustomerID() {
            return customerID;
        }

        public void setCustomerID(String customerID) {
            this.customerID = customerID;
        }

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

        public Object getSurveyVersion() {
            return surveyVersion;
        }

        public void setSurveyVersion(Object surveyVersion) {
            this.surveyVersion = surveyVersion;
        }

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
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

        public Object getModifiedDate() {
            return modifiedDate;
        }

        public void setModifiedDate(Object modifiedDate) {
            this.modifiedDate = modifiedDate;
        }

    }


}