package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UpdateCoopResponse {

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("data")
    @Expose
    private ArrayList<coopData> data;


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

    public ArrayList<coopData> getData() {
        return data;
    }

    public class coopData{

        @SerializedName("Msg")
        @Expose
        private String Msg;


        @SerializedName("Status")
        @Expose
        private String Status;


        public String getMsg() {
            return Msg;
        }

        public String getStatus() {
            return Status;
        }
    }
}



