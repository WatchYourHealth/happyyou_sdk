package com.wyh.happyyousdk.model.response.addmember;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.io.Serializable;
import java.util.List;

public class GetFamilyRelation implements Serializable {


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


    @SerializedName("success")
    @Expose
    private Boolean success;


    @SerializedName("msg")
    @Expose
    private String msg;

    public List<MemberRelation> getData() {
        return data;
    }

    public void setData(List<MemberRelation> data) {
        this.data = data;
    }

    @SerializedName("data")
    @Expose
    private List<MemberRelation> data;

    public static class MemberRelation {
        public String getRelationName() {
            return relationName;
        }

        public void setRelationName(String relationName) {
            this.relationName = relationName;
        }

        public boolean isActive() {
            return isActive;
        }

        public void setActive(boolean active) {
            isActive = active;
        }

        public String getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(String createdOn) {
            this.createdOn = createdOn;
        }

        @SerializedName("relationName")
        @Expose
        private String relationName;
        @SerializedName("isActive")
        @Expose
        private boolean isActive;
        @SerializedName("createdOn")
        @Expose
        private String createdOn;
    }

}
