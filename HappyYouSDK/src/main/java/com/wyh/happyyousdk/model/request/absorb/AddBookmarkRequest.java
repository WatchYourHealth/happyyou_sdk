package com.wyh.happyyousdk.model.request.absorb;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddBookmarkRequest {

    @SerializedName("articleCode")
    @Expose
    private String articleCode;
    @SerializedName("isBookMarked")
    @Expose
    private Boolean isBookMarked;

    public AddBookmarkRequest(String articleCode, Boolean isBookMarked) {
        this.articleCode = articleCode;
        this.isBookMarked = isBookMarked;
    }

    public String getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(String articleCode) {
        this.articleCode = articleCode;
    }

    public Boolean getIsBookMarked() {
        return isBookMarked;
    }

    public void setIsBookMarked(Boolean isBookMarked) {
        this.isBookMarked = isBookMarked;
    }

}