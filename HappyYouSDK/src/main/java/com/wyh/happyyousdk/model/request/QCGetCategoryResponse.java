package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QCGetCategoryResponse {

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("data")
    @Expose
    public QcData data;

    @SerializedName("freevoucher")
    @Expose
    private String freevoucher;

    @SerializedName("enGTokens")
    @Expose
    private Object enGTokens;

    @SerializedName("rewards")
    @Expose
    private Object rewards;


    public String getMsg() {
        return msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public QcData getData() {
        return data;
    }

    public String getFreevoucher() {
        return freevoucher;
    }

    public Object getEnGTokens() {
        return enGTokens;
    }

    public Object getRewards() {
        return rewards;
    }


    public class QcData {

        @SerializedName("cid")
        @Expose
        private String cid;

        @SerializedName("categoryResponse")
        @Expose
        public CategoryResponse categoryResponse;

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public CategoryResponse getCategoryResponse() {
            return categoryResponse;
        }

        public void setCategoryResponse(CategoryResponse categoryResponse) {
            this.categoryResponse = categoryResponse;
        }
    }

    public class CategoryResponse{

        @SerializedName("id")
        @Expose
        public int id;

        @SerializedName("name")
        @Expose
        public String name;

        @SerializedName("url")
        @Expose
        public String url;

        @SerializedName("description")
        @Expose
        private String description;


    }
}




