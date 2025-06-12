package com.wyh.happyyousdk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PolicyDetailsResponse implements Serializable {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private ArrayList<Datum> data;
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

    public ArrayList<Datum> getData() {
        return data;
    }

    public void setData(ArrayList<Datum> data) {
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

    public class Datum implements Serializable{

        @SerializedName("PolicyNo")
        @Expose
        private String policyNo;
        @SerializedName("Premium")
        @Expose
        private String premium;
        @SerializedName("PremiumDueDate")
        @Expose
        private String premiumDueDate;
        @SerializedName("PremiumStatus")
        @Expose
        private String premiumStatus;
        @SerializedName("InsuranceCover")
        @Expose
        private String insuranceCover;
        @SerializedName("IssueDate")
        @Expose
        private String issueDate;
        @SerializedName("PolicyTerm")
        @Expose
        private String policyTerm;
        @SerializedName("PremiumPayingTerm")
        @Expose
        private String premiumPayingTerm;
        @SerializedName("PremiumFrequency")
        @Expose
        private String premiumFrequency;
        @SerializedName("ECS")
        @Expose
        private String ecs;
        @SerializedName("CustomerName")
        @Expose
        private String customerName;
        @SerializedName("MobileNumber")
        @Expose
        private String mobileNumber;
        @SerializedName("PolicyStatus")
        @Expose
        private String policyStatus;
        @SerializedName("DOB")
        @Expose
        private String dob;
        @SerializedName("Relation")
        @Expose
        private String relation;
        @SerializedName("Email")
        @Expose
        private String email;

        public String getPolicyNo() {
            return policyNo;
        }

        public void setPolicyNo(String policyNo) {
            this.policyNo = policyNo;
        }

        public String getPremium() {
            return premium;
        }

        public void setPremium(String premium) {
            this.premium = premium;
        }

        public String getPremiumDueDate() {
            return premiumDueDate;
        }

        public void setPremiumDueDate(String premiumDueDate) {
            this.premiumDueDate = premiumDueDate;
        }

        public String getPremiumStatus() {
            return premiumStatus;
        }

        public void setPremiumStatus(String premiumStatus) {
            this.premiumStatus = premiumStatus;
        }

        public String getInsuranceCover() {
            return insuranceCover;
        }

        public void setInsuranceCover(String insuranceCover) {
            this.insuranceCover = insuranceCover;
        }

        public String getIssueDate() {
            return issueDate;
        }

        public void setIssueDate(String issueDate) {
            this.issueDate = issueDate;
        }

        public String getPolicyTerm() {
            return policyTerm;
        }

        public void setPolicyTerm(String policyTerm) {
            this.policyTerm = policyTerm;
        }

        public String getPremiumPayingTerm() {
            return premiumPayingTerm;
        }

        public void setPremiumPayingTerm(String premiumPayingTerm) {
            this.premiumPayingTerm = premiumPayingTerm;
        }

        public String getPremiumFrequency() {
            return premiumFrequency;
        }

        public void setPremiumFrequency(String premiumFrequency) {
            this.premiumFrequency = premiumFrequency;
        }

        public String getEcs() {
            return ecs;
        }

        public void setEcs(String ecs) {
            this.ecs = ecs;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public String getPolicyStatus() {
            return policyStatus;
        }

        public void setPolicyStatus(String policyStatus) {
            this.policyStatus = policyStatus;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getRelation() {
            return relation;
        }

        public void setRelation(String relation) {
            this.relation = relation;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

    }
}
