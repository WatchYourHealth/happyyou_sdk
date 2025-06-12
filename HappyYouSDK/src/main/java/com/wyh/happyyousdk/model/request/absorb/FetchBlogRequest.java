package com.wyh.happyyousdk.model.request.absorb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FetchBlogRequest {

    @SerializedName("articleCode")
    @Expose
    private String articleCode;

    /**
     * No args constructor for use in serialization
     */
    public FetchBlogRequest() {
    }

    /**
     * @param articleCode
     */
    public FetchBlogRequest(String articleCode) {
        super();
        this.articleCode = articleCode;
    }

    public String getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(String articleCode) {
        this.articleCode = articleCode;
    }

}
