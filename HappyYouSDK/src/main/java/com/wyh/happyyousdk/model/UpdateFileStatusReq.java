package com.wyh.happyyousdk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UpdateFileStatusReq implements Serializable {

    @SerializedName("CategoryName")
    @Expose
    private String CategoryName;

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getUserAction() {
        return UserAction;
    }

    public void setUserAction(String userAction) {
        UserAction = userAction;
    }

    public UpdateFileStatusReq(String categoryName, String userAction) {
        CategoryName = categoryName;
        UserAction = userAction;
    }

    @SerializedName("UserAction")
    @Expose
    private String UserAction;
}
