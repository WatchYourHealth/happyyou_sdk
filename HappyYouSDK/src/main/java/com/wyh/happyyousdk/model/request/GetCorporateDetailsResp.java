package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.io.Serializable;
import java.util.List;

public class GetCorporateDetailsResp implements Serializable {
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


        public List<Data.corporateDetails> getClients() {
            return clients;
        }

        public void setClients(List<Data.corporateDetails> clients) {
            this.clients = clients;
        }

        @SerializedName("corporateDetails")
        @Expose
        private List<Data.corporateDetails> clients;

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
            @SerializedName("questionJson")
            @Expose
            private  String questionJson;
        }
    }
}
