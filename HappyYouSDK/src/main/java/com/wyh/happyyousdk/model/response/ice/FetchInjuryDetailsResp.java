package com.wyh.happyyousdk.model.response.ice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FetchInjuryDetailsResp {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

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


    public class Datum {

        @SerializedName("InjuryImagePath")
        @Expose
        private String injuryImagePath;
        @SerializedName("InjuryDescription")
        @Expose
        private String injuryDescription;

        public String getInjuryImagePath() {
            return injuryImagePath;
        }

        public void setInjuryImagePath(String injuryImagePath) {
            this.injuryImagePath = injuryImagePath;
        }

        public String getInjuryDescription() {
            return injuryDescription;
        }

        public void setInjuryDescription(String injuryDescription) {
            this.injuryDescription = injuryDescription;
        }

    }
}
