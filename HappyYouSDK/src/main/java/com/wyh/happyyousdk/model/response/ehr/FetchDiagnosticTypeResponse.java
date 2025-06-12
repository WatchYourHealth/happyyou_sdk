package com.wyh.happyyousdk.model.response.ehr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.EnGTokensModel;
import com.wyh.happyyousdk.model.RewardsModel;

import java.util.List;

public class FetchDiagnosticTypeResponse {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private List<Datum> data;
    @SerializedName("freevoucher")
    @Expose
    private Object freevoucher;
    @SerializedName("enGTokens")
    @Expose
    private EnGTokensModel enGTokens;
    @SerializedName("rewards")
    @Expose
    private RewardsModel rewards;

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

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
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

    public static class Datum {

        @SerializedName("TestCode")
        @Expose
        private String testCode;
        @SerializedName("TestName")
        @Expose
        private String testName;
        @SerializedName("Unit")
        @Expose
        private String unit;
        @SerializedName("MinValue")
        @Expose
        private String minvalue;
        @SerializedName("MaxValue")
        @Expose
        private String maxvalue;

        public String getTestCode() {
            return testCode;
        }

        public void setTestCode(String testCode) {
            this.testCode = testCode;
        }

        public String getTestName() {
            return testName;
        }

        public void setTestName(String testName) {
            this.testName = testName;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getMinvalue() {
            return minvalue;
        }

        public void setMinvalue(String minvalue) {
            this.minvalue = minvalue;
        }

        public String getMaxvalue() {
            return maxvalue;
        }

        public void setMaxvalue(String maxvalue) {
            this.maxvalue = maxvalue;
        }
    }
}
