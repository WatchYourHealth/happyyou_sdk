package com.wyh.happyyousdk.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
public class GetClientRes {

    public Boolean getStatus() {
        return success;
    }

    public void setStatus(Boolean status) {
        this.success = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @SerializedName("success")
    private Boolean success;


    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private Data data;

    // Getters and Setters

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {

        @SerializedName("clients")
        private List<Client> clients;

        @SerializedName("crmTxnNum")
        private String crmTxnNum;

        // Getters and Setters
        public List<Client> getClients() {
            return clients;
        }

        public void setClients(List<Client> clients) {
            this.clients = clients;
        }

        public String getCrmTxnNum() {
            return crmTxnNum;
        }

        public void setCrmTxnNum(String crmTxnNum) {
            this.crmTxnNum = crmTxnNum;
        }

        public static class Client {

            @SerializedName("clientId")
            private String clientId;

            @SerializedName("clientName")
            private String clientName;

            @SerializedName("lname")
            private String lname;

            @SerializedName("dob")
            private String dob;

            @SerializedName("gender")
            private String gender;

            @SerializedName("policyId")
            private String policyId;

            // Getters and Setters
            public String getClientId() {
                return clientId;
            }

            public void setClientId(String clientId) {
                this.clientId = clientId;
            }

            public String getClientName() {
                return clientName;
            }

            public void setClientName(String clientName) {
                this.clientName = clientName;
            }

            public String getLname() {
                return lname;
            }

            public void setLname(String lname) {
                this.lname = lname;
            }

            public String getDob() {
                return dob;
            }

            public void setDob(String dob) {
                this.dob = dob;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getPolicyId() {
                return policyId;
            }

            public void setPolicyId(String policyId) {
                this.policyId = policyId;
            }
        }
    }
}
