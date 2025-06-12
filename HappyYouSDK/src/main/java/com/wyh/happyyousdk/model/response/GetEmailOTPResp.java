package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.corporateAccount.StateModel;

import java.io.Serializable;
import java.util.List;

public class GetEmailOTPResp implements Serializable {

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @SerializedName("success")
    @Expose
    private Boolean success;


    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("data")
    @Expose
    private Data data;

    public static class Data {


        public List<corporateDetails> getClients() {
            return clients;
        }

        public void setClients(List<corporateDetails> clients) {
            this.clients = clients;
        }

        @SerializedName("corporateDetails")
        @Expose
        private List<corporateDetails> clients;

        public List<StateModel> getStateList() {
            return stateList;
        }

        public void setStateList(List<StateModel> stateList) {
            this.stateList = stateList;
        }

        @SerializedName("stateList")
        @Expose
        private List<StateModel> stateList;

        public static class corporateDetails{
            public String getCorporateNames() {
                return corporateNames;
            }

            public void setCorporateNames(String corporateNames) {
                this.corporateNames = corporateNames;
            }

            public int getCorpId() {
                return corpId;
            }

            public void setCorpId(int corpId) {
                this.corpId = corpId;
            }

            @SerializedName("corporateNames")
            @Expose
            private String corporateNames;

            @SerializedName("corpId")
            @Expose
            private  int corpId;

            public String getQuestionJson() {
                return questionJson;
            }

            public void setQuestionJson(String questionJson) {
                this.questionJson = questionJson;
            }

            @SerializedName("questionJson")
            @Expose
            private  String questionJson;
        }
    }
}
